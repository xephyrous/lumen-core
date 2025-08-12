package org.xephyrous.lumen.filters

import org.xephyrous.lumen.storage.ImageBuffer
import org.xephyrous.lumen.pipeline.ImageEffector
import org.xephyrous.lumen.pipeline.ImageEffectorType
import org.xephyrous.lumen.utils.typeRef
import java.awt.Color

/**
 * Applies a filter to an image, modifying pixel values individually
 */
abstract class ImageFilter : ImageEffector<ImageBuffer, ImageBuffer>(typeRef()) {
    override val type: ImageEffectorType = ImageEffectorType.FILTER

    /**
     * Changes a pixel color by a given function
     *
     * @param red The red channel value of the current pixel
     * @param green The green channel value of the current pixel
     * @param blue The blue channel value of the current pixel
     * @param alpha The alpha channel value of the current pixel
     * @param x The x position of the current pixel
     * @param y The y position of the current pixel
     *
     * @return The new color of the current pixel
     */
    abstract fun pixelMod(red: Int, green: Int, blue: Int, alpha: Int, x: Int, y: Int): Color

    /**
     * Sequentially applies the pixel modification function over each pixel in the image
     * TODO : Parallel optimization with multithreading
     */
    override fun apply(data: ImageBuffer): ImageBuffer {
        val size = data.pixels.size
        for (i in 0 until size) {
            val x = i % data.width
            val y = i / data.width
            val currPx = data.pixels[i]

            data.setPixel(i, pixelMod(
                (currPx shr 16) and 0xFF,
                (currPx shr 8) and 0xFF,
                currPx and 0xFF,
                (currPx shr 24) and 0xFF,
                x,
                y
            ))
        }
        return data
    }

    companion object {
        class ImageFilterInline(val block: (Int, Int, Int, Int, Int, Int) -> Color) : ImageFilter() {
            override fun pixelMod(red: Int, green: Int, blue: Int, alpha: Int, x: Int, y: Int): Color = block(red, green, blue, alpha, x, y)
        }

        fun inline(data: ImageBuffer, block: (Int, Int, Int, Int, Int, Int) -> Color): ImageBuffer {
           return ImageFilterInline(block).apply(data)
        }
    }
}