package org.xephyrous.lumen.io

import org.xephyrous.lumen.errors.DecoratedError
import org.xephyrous.lumen.storage.ImageBuffer
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO

/**
 * Simplifies loading images from different types into an [ImageBuffer]
 */
object ImageLoader {
    /** Stores the current system's available image formats.
     * This will be used in the future for system compatibility checking.
     */
    val availableFormats = ImageIO.getWriterFormatNames()

    /**
     * Contains all image formats loadable into an [ImageBuffer]
     *
     * For specifications on each of the formats please refer to the following:
     *  + [JPEG](https://jpeg.org/jpeg/)
     *  + [PNG](https://www.libpng.org/pub/png/spec/1.2/PNG-Contents.html)
     *  + [GIF](https://www.w3.org/Graphics/GIF/spec-gif89a.txt)
     *  + [BMP](https://learn.microsoft.com/en-us/windows/win32/gdi/bitmap-storage)
     *  + [WBMP](https://www.openmobilealliance.org/tech/affiliates/wap/wap-237-waemt-20010515-a.pdf)
     *
     * @param extensions All file extensions associated with the given format
     * @param base The default, or most common, extension for the given format
     */
    enum class SupportedImageType(val extensions: Array<String>, val base: String) {
        /**
         * _Joint Photographic Experts Group_ Format
         *
         * **Valid extensions:** .jpg | .jpeg | .jpe | .jfif | .jif | .jfi | .pjpeg | .pjp
         */
        JPEG(arrayOf("jpg", "jpeg", "jpe", "jfif", "jif", "jfi", "pjpeg", "pjp"), "jpg"),

        /**
         * _Portable Network Graphics_ Format
         *
         * **Valid extensions:** .png
         */
        PNG(arrayOf("png"), "png"),

        /**
         * _Graphics Interchange Format_
         *
         * **Valid Extensions:** .gif
         */
        GIF(arrayOf("gif"), "gif"),

        /**
         * _Bitmap_ Format
         *
         * **Valid Extensions:** .bmp
         */
        BMP(arrayOf("bmp"), "bmp"),

        /**
         *  _Wireless Application Protocol Bitmap Format_
         *
         *  **Valid Extensions:** .wbmp
         */
        WBMP(arrayOf("wbmp"), "wbmp");

        companion object {
            /**
             * Checks whether [ImageLoader.SupportedImageType] contains the given image extension
             *
             * @param extension The image extension to check for
             *
             * @return Whether [ImageLoader.SupportedImageType] contains the given image extension
             */
            fun contains(extension: String) : Boolean {
                for (type in entries) {
                    if (type.extensions.contains(extension)) {
                        return true
                    }
                }

                return false
            }

            /**
             * Converts an image extension string to a [ImageLoader.SupportedImageType]
             *
             * @param extension The image extension to convert
             *
             * @return A [ImageLoader.SupportedImageType] representing the given image extension if possible, null otherwise
             */
            fun from(extension: String): SupportedImageType? {
                for (type in entries) {
                    if (type.extensions.contains(extension)) {
                        return type
                    }
                }

                return null
            }
        }
    }

    /**
     * Helper function for [ImageLoader.loadImage] accepting a path
     *
     * @param path The path of the image to load
     *
     * @return An [ImageBuffer] containing the loaded image data
     */
    fun loadImage(path: String): ImageBuffer {
        try {
            return loadImage(File(path))
        } catch (e: IOException) {
            throw DecoratedError(
                "INACCESSIBLE_IMAGE_FILE", "The file '${path}' cannot be loaded",
                "Ensure the file exists, is a file, and can be read by lumen"
            )
        }
    }

    /**
     * Loads an image file's data and extension information into an [ImageBuffer]
     *
     * @param file The image file object to load
     *
     * @return An [ImageBuffer] containing the loaded image data
     */
    fun loadImage(file: File): ImageBuffer {
        if (!file.exists() || !file.isFile || !file.canRead()) {
            throw DecoratedError(
                "INACCESSIBLE_IMAGE_FILE", "The file '${file.path}' cannot be loaded",
                "Ensure the file exists, is a file, and can be read by lumen"
            )
        }

        if (!SupportedImageType.contains(file.extension)) {
            throw DecoratedError(
                "UNSUPPORTED_FILE_TYPE", "The file extension '${file.extension}' is not supported",
                "Supported extensions are ${SupportedImageType.entries}"
            )
        }

        try {
            val imageFile = ImageIO.read(file)
            val width = imageFile.width
            val height = imageFile.height
            val pixels = IntArray(width * height)
            imageFile.getRGB(0, 0, width, height, pixels, 0, width)

            return ImageBuffer(width, height, pixels, SupportedImageType.from(file.extension)!!)
        } catch (e: IOException) {
            throw DecoratedError(
                "INVALID_IMAGE_FILE", "The image file '${file.absolutePath}' could not be read",
                e.message.toString()
            )
        }
    }

    /**
     * Loads a [BufferedImage]'s data into an [ImageBuffer]
     *
     * @param image The [BufferedImage] to load
     * @param type A [SupportedImageType] that the image will be saved as
     *
     * @return An [ImageBuffer] containing the loaded image data
     */
    fun loadImage(image: BufferedImage, type: SupportedImageType = SupportedImageType.PNG): ImageBuffer {
        try {
            val width = image.width
            val height = image.height
            val pixels = IntArray(width * height)
            image.getRGB(0, 0, width, height, pixels, 0, width)

            return ImageBuffer(width, height, pixels, type)
        } catch (e: Exception) {
            throw DecoratedError(
                "INVALID_IMAGE_OBJECT", "The supplied image object could not be read",
                e.message.toString()
            )
        }
    }
}