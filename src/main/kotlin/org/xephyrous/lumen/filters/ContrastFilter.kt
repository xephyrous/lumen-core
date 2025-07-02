package org.xephyrous.lumen.filters

import org.xephyrous.lumen.io.ImageLoader
import java.awt.Color
import java.awt.image.BufferedImage

class ContrastFilter(val value: Int) : ImageFilter() {
    constructor(value: Float) : this((128 * value).toInt())

    override fun pixelMod(red: Int, green: Int, blue: Int, alpha: Int): Color {
        val correctionFactor: Float = (259 * (value + 255)) / (255 * (259 - value)).toFloat()
        return Color(
            (correctionFactor * (red - 128) + 128).toInt().coerceIn(0, 255),
            (correctionFactor * (green - 128) + 128).toInt().coerceIn(0, 255),
            (correctionFactor * (blue - 128) + 128).toInt().coerceIn(0, 255)
        )
    }

    companion object {
        /**
         * Standalone runner function for [ContrastFilter]
         *
         * @param image The image to filter
         * @param value The contrast value, ranging from `-128f` to `128f`, to apply to the image
         *
         * @return The modified image
         *
         * @see [ImageFilter.apply]
         */
        fun run(image: BufferedImage, value: Int): BufferedImage {
            val instance = ContrastFilter(value)
            return instance.apply(ImageLoader.loadImage(image)).toBufferedImage()
        }

        /**
         * Standalone runner function for [ContrastFilter]
         *
         * @param image The image to filter
         * @param value The contrast value, as a percentage, to apply to the image
         *
         * @return The modified image
         *
         * @see [ImageFilter.apply]
         */
        fun run(image: BufferedImage, value: Float): BufferedImage {
            val instance = ContrastFilter(value)
            return instance.apply(ImageLoader.loadImage(image)).toBufferedImage()
        }
    }
}