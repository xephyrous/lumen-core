package org.xephyrous.lumen.io

import org.xephyrous.lumen.errors.DecoratedError
import org.xephyrous.lumen.storage.ImageBuffer
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO

/**
 * TODO : Document
 */
object ImageLoader {
    /** TODO : Document */
    val availableFormats = ImageIO.getWriterFormatNames()

    /**
     * TODO : Document
     */
    enum class SupportedImageType(val extensions: Array<String>, val base: String) {
        JPEG(arrayOf("jpg", "jpeg", "jpe", "jfif", "jif"), "jpg"),
        PNG(arrayOf("png"), "png"),
        GIF(arrayOf("gif"), "gif"),
        BMP(arrayOf("bmp"), "bmp"),
        WBMP(arrayOf("wbmp"), "wbmp");

        companion object {
            /**
             * TODO : Document
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
             * TODO : Document
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
     * TODO : Document
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
}