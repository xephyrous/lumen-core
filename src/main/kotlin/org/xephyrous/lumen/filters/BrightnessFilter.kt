package org.xephyrous.lumen.filters

import org.xephyrous.lumen.io.ImageLoader
import java.awt.Color
import java.awt.image.BufferedImage

class BrightnessFilter(val value: Int) : ImageFilter() {
    constructor(value: Float) : this((255 * value).toInt())

    init {
        require(value in -255..255) { "Brightness value must be between -255 and 255, was $value" }
    }

    override fun pixelMod(red: Int, green: Int, blue: Int, alpha: Int): Color {
        return Color(
            (red + value).coerceIn(0, 255),
            (green + value).coerceIn(0, 255),
            (blue + value).coerceIn(0, 255),
            alpha
        )
    }

    companion object {
        /**
         * Standalone runner function for [SepiaFilter]
         *
         * @param image The image to filter
         * @param value The brightness value, ranging from `-255` to `255`, to apply to the image
         *
         * @see [ImageFilter.apply]
         */
        fun run(image: BufferedImage, value: Int) : BufferedImage {
            val instance = BrightnessFilter(value)
            return instance.apply(ImageLoader.loadImage(image)).toBufferedImage()
        }

        /**
         * Standalone runner function for [SepiaFilter]
         *
         * @param image The image to filter
         * @param value The brightness value, ranging from `-1f` to `1f`, to apply to the image
         *
         * @see [ImageFilter.apply]
         */
        fun run(image: BufferedImage, value: Float) : BufferedImage {
            val instance = BrightnessFilter(value)
            return instance.apply(ImageLoader.loadImage(image)).toBufferedImage()
        }
    }
}