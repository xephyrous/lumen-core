package org.xephyrous.lumen.filters

import org.xephyrous.lumen.storage.ImageData
import org.xephyrous.lumen.pipeline.ImageEffector
import org.xephyrous.lumen.pipeline.ImageEffectorType
import java.awt.Color
import kotlin.reflect.KClass

/**
 * Applies a filter to an image, modifying pixel values individually
 */
abstract class ImageFilter : ImageEffector<ImageData, Unit>() {
    override val type: ImageEffectorType = ImageEffectorType.FILTER
    override val inputType: KClass<*> = ImageData::class
    override val outputType: KClass<*> = Unit::class

    /**
     * Changes a pixel color by a given function
     *
     * @param pixelValue The color of the current pixel
     * @return The new color of the current pixel
     */
    abstract fun pixelMod(pixelValue: Color): Color

    /**
     * Sequentially applies the pixel modification function over each pixel in the image
     * TODO : Parallel optimization with multithreading
     */
    override fun apply(data: ImageData) {
        for (i in 0..data.data().size) {
            data.getImage().onSuccess {
                if (it.colorModel?.hasAlpha() == true) {
                    pixelMod(data.rgba(i))
                }
            }

            pixelMod(data.rgb(i))
        }
    }
}