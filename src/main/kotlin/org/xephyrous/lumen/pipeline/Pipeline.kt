package org.xephyrous.lumen.pipeline

import org.xephyrous.lumen.cutters.ImageCutter
import org.xephyrous.lumen.effects.ImageEffect
import org.xephyrous.lumen.errors.DecoratedError
import org.xephyrous.lumen.filters.ImageFilter
import org.xephyrous.lumen.io.ImageLoader
import org.xephyrous.lumen.manipulations.ImageManipulation
import org.xephyrous.lumen.storage.ImageBuffer
import org.xephyrous.lumen.storage.LockType
import org.xephyrous.lumen.storage.Mask
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO

/**
 * DSL marker annotation to scope DSL functions related to image pipelines
 */
@DslMarker
annotation class PipelineDsl

/**
 * Represents types of errors that can occur during pipeline operations
 */
enum class PipelineErrorType {
    CHAIN_ERROR,
    IMAGE_ERROR,
    IO_ERROR
}

/**
 * Represents a decorated error specific to the [Pipeline] system
 *
 * @param message The error message to display
 * @param suggestion Suggested actions or hints to resolve the error
 * @param code The specific error type code
 */
class PipelineError(message: String, suggestion: String, code: PipelineErrorType)
    : DecoratedError("PIPELINE_${code.name}", message, suggestion, code.ordinal)

/**
 * Builds and executes an [ImagePipeline] using a DSL-style block
 *
 * @param shouldRun Whether the pipeline should be run following completion of [block]
 * @param block A lambda with receiver that modifies the [ImagePipeline]
 *
 * @return The [ImagePipeline] instance
 */
fun ImagePipeline(shouldRun: Boolean = true, block: Pipeline.() -> Unit): Pipeline {
    val pipeline = Pipeline()
    pipeline.shouldRun = shouldRun
    pipeline.block()
    if (pipeline.shouldRun) pipeline.run()
    return pipeline
}

/**
 * An image processing pipeline that applies a chain of effectors to process images
 */
@PipelineDsl
class Pipeline {
    /**
     * The current state of the pipeline's image
     */
    private var _image: LockType<BufferedImage?> = LockType(null)

    /**
     * The pure data of the image being stored
     */
    private var _data: LockType<ImageBuffer?> = LockType(null)

    /**
     * The cut areas of the image as [Mask]s
     */
    private var _pieces: LockType<Array<Mask>?> = LockType(null)

    /**
     * A list of linkable image effector to be enacted on the image
     */
    private var _effectorChain: ArrayList<ImageEffector<*, *>> = arrayListOf()

    /**
     * Whether the pipeline should automatically run when built via DSL
     */
    internal var shouldRun: Boolean = true

    /**
     * The position of the last error in the [_effectorChain]
     */
    var errorPos: Int? = null

    /**
     * Loads and image and its data into the pipeline
     *
     * @param imageFile The image file to be loaded
     */
    fun loadImage(imageFile: File) {
        _data.value = ImageLoader.loadImage(imageFile)
        _image.value = _data.value!!.toBufferedImage()
    }

    /**
     * Loads and image and its data into the pipeline
     * Helper function for [loadImage]
     *
     * @param imagePath The path of the image file to be loaded
     */
    fun loadImage(imagePath: String) {
        loadImage(File(imagePath))
    }

    /**
     * Builds the current state of the image data into an image and returns it
     *
     * @return On a successful read of the image data, the stored image as a [BufferedImage].
     *
     * @throws IOException On a failed read of the image data.
     */
    fun getImage(): BufferedImage {
        updateImage()
        return _image.value!!
    }

    /**
     * Adds any number of ImageEffectors to the pipeline
     *
     * @param effectors Any number of [ImageEffector]s to add to the end of the pipeline chain
     *
     * @return A result object that contains the success state of the function
     * along with a message, used for UI integration/callbacks.
     */
    fun chain(vararg effectors: ImageEffector<*, *>): Result<Unit> {
        effectors.forEach { effector ->
            if (_effectorChain.isEmpty()) {
                if (effector.inputType != ImageBuffer::class) {
                    PipelineError(
                        "Mismatched effector types : ${ImageBuffer::class.qualifiedName} -> ${effector.inputType::class.qualifiedName}",
                        "Are you missing an intermediate effector?",
                        PipelineErrorType.CHAIN_ERROR
                    )
                }

                _effectorChain.add(effector)
                return@forEach
            }

            val lastEffector = _effectorChain.last()
            if (!lastEffector.chainIOCheck(effector)) {
                return Result.failure(
                    PipelineError(
                        "Mismatched effector types : ${lastEffector.outputType::class.qualifiedName} -> ${effector.inputType::class.qualifiedName}",
                        "Are you missing an intermediate effector?",
                        PipelineErrorType.CHAIN_ERROR
                    )
                )
            }

            _effectorChain.add(effector)
        }

        return Result.success(Unit)
    }

    /**
     * Clears all effectors loaded into the pipeline's chain
     */
    fun clearEffectors() {
        _effectorChain.clear()
    }

    /**
     * Unloads any image data loaded into the pipeline
     */
    fun unloadImage() {
        _image.value = null
        _data.value = null
    }

    /**
     * Clears the pipeline of all loaded data. Unloads the pipeline image and clears all effectors
     */
    fun clear() {
        unloadImage()
        clearEffectors()
    }

    /**
     * Applies all effectors in the [_effectorChain] to the image loaded in the pipeline
     */
    fun run() {
        if (_image.value == null) {
            throw PipelineError(
                "No image loaded into pipeline!",
                "Did you call ImagePipeline.loadImage() or ImagePipeline.unloadImage()?",
                PipelineErrorType.IMAGE_ERROR
            )
        }

        var data: Any = _data.value!!

        _effectorChain.forEach { effector ->
            when (effector.type) {
                ImageEffectorType.EFFECT -> {
                    data = (effector as ImageEffect).apply(data as ImageBuffer)
                }

                ImageEffectorType.FILTER -> {
                    data = (effector as ImageFilter).apply(data as ImageBuffer)
                }

                ImageEffectorType.CUTTER -> {
                    data = (effector as ImageCutter).apply(data as ImageBuffer)
                }

                ImageEffectorType.MANIPULATION -> {
                    data = (effector as ImageManipulation).apply(data as ImageBuffer)
                }

                ImageEffectorType.KERNEL -> TODO()
            }
        }
    }

    /**
     * Forces the pipeline's internal image snapshot to be updated from the image data
     */
    private fun updateImage() {
        _image.value = _data.value!!.toBufferedImage()
    }

    /**
     * Saves the current image snapshot to a file. The internal snapshot is updated prior to saving
     *
     * @param path The path to save to
     */
    fun save(path: String) {
        try {
            updateImage()
            ImageIO.write(_image.value, _data.value!!.extension.base, File(path))
        } catch (e: IOException) {
            throw PipelineError(
                "Failed to save image to $path", "Does the path exist, and is it reachable?",
                PipelineErrorType.IO_ERROR
            )
        } catch (e: Exception) {
            throw PipelineError(
                "Error saving pipeline image to $path", e.message.toString(),
                PipelineErrorType.IO_ERROR
            )
        }
    }
}