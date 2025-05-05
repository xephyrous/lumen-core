package org.xephyrous.lumen.filters

import java.awt.Color

class SepiaFilter : ImageFilter() {
    override fun pixelMod(red: Int, green: Int, blue: Int, alpha: Int): Color {
        return Color(
            ((red * .393) + (green * .769) + (blue * .189)).toInt().coerceIn(0, 255),
            ((red * .349) + (green * .686) + (blue * .168)).toInt().coerceIn(0, 255),
            ((red * .272) + (green * .534) + (blue * .131)).toInt().coerceIn(0, 255),
            alpha
        )
    }
}