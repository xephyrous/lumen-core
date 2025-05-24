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
 *
 */
enum class PipelineErrorType {
    CHAIN_ERROR,
    IMAGE_ERROR,
    IO_ERROR
}

/**
 * TODO : Document PipelineError
 */
class PipelineError(message: String, suggestion: String, code: PipelineErrorType)
    : DecoratedError("PIPELINE_${code.name}", message, suggestion, code.ordinal)

/**
 *
 */
class ImagePipeline {
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
     * TODO : Document
     */
    fun rechain(sourcePos: Int, destPos: Int) {
        
    }

    /**
     * Applies all effectors in the [_effectorChain] to the image loaded in the pipeline
     */
    fun run() {
        if (_image.value == null) {
            throw PipelineError(
                "No image loaded into pipeline!",
                "Did you call ImagePipeline.loadImage()?",
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
     * TODO : Document, add handling for failure
     */
    private fun updateImage() {
        _image.value = _data.value!!.toBufferedImage()
    }

    /**
     * TODO : Document
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