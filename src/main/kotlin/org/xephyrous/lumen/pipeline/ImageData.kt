package org.xephyrous.lumen.pipeline

import org.xephyrous.lumen.views.ImageDataView
import java.awt.Color
import java.awt.image.BufferedImage
import java.awt.image.DataBufferInt
import java.io.ByteArrayInputStream
import java.io.IOException
import javax.imageio.ImageIO
import kotlin.experimental.and

/**
 * Custom image holder for improved pixel data access and processing
 *
 * @param image The base image to convert to data
 */
class ImageData(private val image: BufferedImage) {
    // TODO("Validate this actually works (Thanks Shit-GPT)")
    private val _data: ByteArray = (image.raster.dataBuffer as DataBufferInt).data.flatMap {
        listOf(
            (it shr 24).toByte(),
            (it shr 16).toByte(),
            (it shr 8).toByte(),
            it.toByte()
        )
    }.toByteArray()

    private lateinit var _view: ImageDataView

    val width: Int = image.width
    val height: Int = image.height

    /**
     * Converts coordinates on the stored image to the corresponding position in the [ByteArray]
     *
     * @param x The x-coordinate of the pixel in the image.
     * @param y The y-coordinate of the pixel in the image,
     * @return The index of the pixel at the coordinates ([x], [y])
     */
    fun indexOf(x: Int, y: Int): Int {
        if (x !in 0..image.width || y !in 0..image.height) {
            throw IndexOutOfBoundsException("Invalid position in image!")
        }

        return (image.width * y + x)
    }

    /**
     * A helper function for [indexOf] to accept a Pair<Int, Int>
     *
     * @param pos A position on the parent image to convert to an index
     * @return The index of the pixel at the coordinate [pos]
     */
    fun indexOf(pos: Pair<Int, Int>): Int {
        return indexOf(pos.first, pos.second)
    }

    /**
     * Returns the color at a specified index in [_data]
     *
     * Automatically determines if the image has an alpha channel
     *
     * @param index The index of the pixel to get the color of
     * @return The color of the pixel
     */
    fun colorAt(index: Int): Color {
        getImage().onSuccess {
            if (it.colorModel.hasAlpha()) {
                return rgba(index)
            }

            return rgb(index)
        }.onFailure {
            throw it
        }

        // Default to rgb
        return rgb(index)
    }

    /**
     * Returns the color of a given pixel in RGB.
     *
     * @param index The index of the pixel in the image data ([_data])
     * @return The color (in RGB) of the pixel at the given index
     */
    fun rgb(index: Int): Color {
        return Color(
            _data[index].and(0xff.toByte()).toInt().shl(16),
            _data[index].and(0xff.toByte()).toInt().shl(8),
            _data[index].and(0xff.toByte()).toInt(),
        )
    }

    /**
     * Returns the color of a given pixel in RGBA.
     *
     * @param index The index of the pixel in the image data ([_data])
     * @return The color (in RGBA) of the pixel at the given index
     */
    fun rgba(index: Int): Color {
        return Color(
            _data[index].and(0xff.toByte()).toInt().shl(16),
            _data[index].and(0xff.toByte()).toInt().shl(8),
            _data[index].and(0xff.toByte()).toInt(),
            _data[index].and(0xff.toByte()).toInt().shl(24)
        )
    }

    /**
     * Returns the image data ([_data])
     *
     * @return The image data ([_data]) as a [BufferedImage]
     */
    fun getImage(): Result<BufferedImage> {
        return try {
            Result.success(ImageIO.read(ByteArrayInputStream(_data)))
        } catch (err: IOException) {
            Result.failure(IOException("Failed to read image from (possibly corrupt) data!"))
        }
    }

    /**
     * Returns the image data ([_data])
     *
     * @return The image data ([_data]) as a [ByteArray]
     */
    fun data(): ByteArray {
        return _data
    }
}