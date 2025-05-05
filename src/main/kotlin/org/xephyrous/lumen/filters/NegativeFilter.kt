package org.xephyrous.lumen.filters

import java.awt.Color

class NegativeFilter : ImageFilter() {
    override fun pixelMod(red: Int, green: Int, blue: Int, alpha: Int): Color {
        return Color(
            255 - red,
            255 - blue,
            255 - green,
            alpha
        )
    }
}