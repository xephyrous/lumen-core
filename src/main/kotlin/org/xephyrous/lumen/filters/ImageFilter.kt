package org.xephyrous.lumen.filters

import org.xephyrous.lumen.storage.ImageBuffer
import org.xephyrous.lumen.pipeline.ImageEffector
import org.xephyrous.lumen.pipeline.ImageEffectorType
import java.awt.Color
import kotlin.reflect.KClass

/**
 * Applies a filter to an image, modifying pixel values individually
 */
abstract class ImageFilter : ImageEffector<ImageBuffer, ImageBuffer>() {
    override val type: ImageEffectorType = ImageEffectorType.FILTER
    override val inputType: KClass<*> = ImageBuffer::class
    override val outputType: KClass<*> = ImageBuffer::class

    /**
     * Changes a pixel color by a given function
     *
     * @param pixelValue The color (as an Int) of the current pixel
     * @return The new color of the current pixel
     */
    abstract fun pixelMod(red: Int, green: Int, blue: Int, alpha: Int): Color

    /**
     * Sequentially applies the pixel modification function over each pixel in the image
     * TODO : Parallel optimization with multithreading
     */
    override fun apply(data: ImageBuffer): ImageBuffer {
        val size = data.pixels.size
        for (i in 0 until size) {
            val currPx = data.pixels[i]

            data.setPixel(i, pixelMod(
                (currPx shr 16) and 0xFF,
                (currPx shr 8) and 0xFF,
                currPx and 0xFF,
                (currPx shr 24) and 0xFF
            ))
        }
        return data
    }
}