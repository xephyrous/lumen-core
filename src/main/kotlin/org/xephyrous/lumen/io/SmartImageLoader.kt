package org.xephyrous.lumen.io

import org.xephyrous.lumen.errors.DecoratedError
import org.xephyrous.lumen.storage.ImageData
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO

object ImageLoader {
    val availableFormats = ImageIO.getWriterFormatNames()

    enum class SupportedImageType(val extensions: Array<String>) {
        JPEG(arrayOf("jpg", "jpeg", "jpe", "jfif", "jif")),
        PNG(arrayOf("png")),
        GIF(arrayOf("gif")),
        BMP(arrayOf("bmp")),
        WBMP(arrayOf("wbmp"));

        companion object {
            fun contains(extension: String) : Boolean {
                for (type in entries) {
                    if (type.extensions.contains(extension)) {
                        return true
                    }
                }

                return false
            }

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

    fun loadImage(file: File): ImageData {
        if (!SupportedImageType.contains(file.extension)) {
            throw DecoratedError(
                "UNSUPPORTED_FILE_TYPE", "The file extension '${file.extension}' is not supported",
                "Supported extensions are ${SupportedImageType.entries}"
            )
        }

        try {
            val imageFile = ImageIO.read(file)
            return ImageData(imageFile, SupportedImageType.from(file.extension)!!)
        } catch(e: IOException) {
            throw DecoratedError(
                "INVALID_IMAGE_FILE", "The image file '${file.absolutePath}' could not be read",
                e.message.toString()
            )
        }
    }
}