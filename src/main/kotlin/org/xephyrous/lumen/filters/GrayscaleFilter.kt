package org.xephyrous.lumen.filters

import java.awt.Color

class GrayscaleFilter : ImageFilter() {
    override fun pixelMod(red: Int, green: Int, blue: Int, alpha: Int): Color {
        val avg = ((red + blue + green) / 3).toInt().coerceAtMost(255)
        return Color(avg, avg, avg)
    }
}