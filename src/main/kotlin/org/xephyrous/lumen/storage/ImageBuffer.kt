package org.xephyrous.lumen.storage

import org.xephyrous.lumen.io.ImageLoader.SupportedImageType
import org.xephyrous.lumen.views.ImageDataView
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import javax.imageio.ImageIO

/**
 * Custom image holder for improved pixel data access and processing
 *
 * @param width The image width
 * @param height The image height
 * @param pixels An array of pixels representing the image data
 * @param extension The image type
 */
class ImageBuffer(val width: Int, val height: Int, val pixels: IntArray, val extension: SupportedImageType) {
    companion object {
        fun from(image: BufferedImage, extension: SupportedImageType): ImageBuffer {
            val width = image.width
            val height = image.height
            val pixels = IntArray(width * height)
            image.getRGB(0, 0, width, height, pixels, 0, width)
            return ImageBuffer(width, height, pixels, extension)
        }
    }

    init {
        require(width >= 0 && height >= 0) { "Width and height must be greater than zero" }
        require(pixels.size == width * height) { "Image pixel array size does not match (width * height)" }
    }

    fun getPixel(i: Int): Int {
        checkBounds(i)
        return pixels[i]
    }

    fun getPixel(x: Int, y: Int): Int {
        checkBounds(x, y)
        return pixels[y * width + x]
    }

    fun setPixel(i: Int, color: Color) {
        checkBounds(i)
        pixels[i] = color.rgb
    }

    fun setPixel(x: Int, y: Int, argb: Int) {
        checkBounds(x, y)
        pixels[y * width + x] = argb
    }

    fun getColor(x: Int, y: Int): Color {
        return Color(getPixel(x, y), true)
    }

    fun setColor(x: Int, y: Int, color: Color) {
        setPixel(x, y, color.rgb or (color.alpha shl 24))
    }

    fun toBufferedImage(): BufferedImage {
        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        image.setRGB(0, 0, width, height, pixels, 0, width)
        return image
    }

    private fun checkBounds(i: Int) {
        require(i >= 0 && i < pixels.size) {
            "Pixel index $i out of bounds"
        }
    }

    private fun checkBounds(x: Int, y: Int) {
        require(x in 0 until width && y in 0 until height) {
            "Pixel coordinates out of bounds: ($x, $y)"
        }
    }
}