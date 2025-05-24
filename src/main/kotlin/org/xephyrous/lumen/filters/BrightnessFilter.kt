package org.xephyrous.lumen.filters

import java.awt.Color

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
}