package org.xephyrous.lumen.filters

import org.xephyrous.lumen.io.ImageLoader
import java.awt.Color
import java.awt.image.BufferedImage

class GrayscaleFilter : ImageFilter() {
    override fun pixelMod(red: Int, green: Int, blue: Int, alpha: Int): Color {
        val avg = ((red + blue + green) / 3).toInt().coerceAtMost(255)
        return Color(avg, avg, avg)
    }

    companion object {
        /**
         * Standalone runner function for [GrayscaleFilter]
         *
         * @param image The image to filter
         *
         * @see [ImageFilter.apply]
         */
        fun run(image: BufferedImage) : BufferedImage {
            val instance = GrayscaleFilter()
            return instance.apply(ImageLoader.loadImage(image)).toBufferedImage()
    }
}
}