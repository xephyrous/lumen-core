package org.xephyrous.lumen.utils

import org.xephyrous.lumen.storage.Quadruple
import java.awt.Color
import kotlin.math.roundToInt

/**
 * Converts this Boolean value to an integer.
 *
 * @return `1` if the Boolean is `true`, otherwise `0`.
 */
fun Boolean.toInt() = if (this) 1 else 0

/**
 * Extracts the RGBA components from this [Color] as a [Quadruple].
 *
 * @return A [Quadruple] containing the red, green, blue, and alpha components (in that order).
 */
fun Color.components(): Quadruple<Int, Int, Int, Int> {
    return Quadruple(this.red, this.green, this.blue, this.alpha)
}

/**
 * Converts a [Quadruple] of floats to a [Color], mapping `WXYZ` to `RGBA`.
 *
 * @return The converted color.
 */
fun Quadruple<Float, Float, Float, Float>.toColor() : Color {
    return Color(
        this.first.roundToInt().coerceIn(0, 255),
        this.second.roundToInt().coerceIn(0, 255),
        this.third.roundToInt().coerceIn(0, 255),
        this.fourth.roundToInt().coerceIn(0, 255)
    )
}

/**
 * Computes the average of a list of [Quadruple]s with numeric components and returns
 * a [Quadruple] of the specified numeric type [T].
 *
 * @param T The target number type (e.g., Int, Float, Double).
 *
 * @return The element-wise average of all entries in this list, cast to type [T].
 *
 * @throws IllegalArgumentException if the list is empty or if [T] is unsupported.
 */
inline fun <reified T : Number> List<Quadruple<out Number, out Number, out Number, out Number>>.average(): Quadruple<T, T, T, T> {
    if (isEmpty()) throw IllegalArgumentException("Cannot average an empty list")

    var rSum = 0.0
    var gSum = 0.0
    var bSum = 0.0
    var aSum = 0.0

    for ((r, g, b, a) in this) {
        rSum += r.toDouble()
        gSum += g.toDouble()
        bSum += b.toDouble()
        aSum += a.toDouble()
    }

    val size = this.size.toDouble()

    return Quadruple(
        convertToNumber(rSum / size, T::class),
        convertToNumber(gSum / size, T::class),
        convertToNumber(bSum / size, T::class),
        convertToNumber(aSum / size, T::class)
    )
}