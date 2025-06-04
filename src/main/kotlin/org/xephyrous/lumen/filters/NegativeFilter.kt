package org.xephyrous.lumen.filters

import org.xephyrous.lumen.io.ImageLoader
import java.awt.Color
import java.awt.image.BufferedImage

class NegativeFilter : ImageFilter() {
    override fun pixelMod(red: Int, green: Int, blue: Int, alpha: Int): Color {
        return Color(
            255 - red,
            255 - blue,
            255 - green,
            alpha
        )
    }

    companion object {
        /**
         * Standalone runner function for [NegativeFilter]
         *
         * @param image The image to filter
         *
         * @see [ImageFilter.apply]
         */
        fun run(image: BufferedImage) : BufferedImage {
            val instance = NegativeFilter()
            return instance.apply(ImageLoader.loadImage(image)).toBufferedImage()
        }
    }
}