package org.xephyrous.lumen.filters

import org.xephyrous.lumen.io.ImageLoader
import java.awt.Color
import java.awt.image.BufferedImage

class SepiaFilter : ImageFilter() {
    override fun pixelMod(red: Int, green: Int, blue: Int, alpha: Int): Color {
        return Color(
            ((red * .393) + (green * .769) + (blue * .189)).toInt().coerceIn(0, 255),
            ((red * .349) + (green * .686) + (blue * .168)).toInt().coerceIn(0, 255),
            ((red * .272) + (green * .534) + (blue * .131)).toInt().coerceIn(0, 255),
            alpha
        )
    }

    companion object {
        /**
         * Standalone runner function for [SepiaFilter]
         *
         * @param image The image to filter
         *
         * @see [ImageFilter.apply]
         */
        fun run(image: BufferedImage) : BufferedImage {
            val instance = SepiaFilter()
            return instance.apply(ImageLoader.loadImage(image)).toBufferedImage()
        }
    }
}