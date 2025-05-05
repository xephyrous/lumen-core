package org.xephyrous.lumen.views

import org.xephyrous.lumen.storage.ImageBuffer
import java.awt.Color

/**
 * Represents an exposed view of an image
 *
 * @param image A reference to the parent class's stored image
 */
abstract class ImageDataView(image: ImageBuffer) {
    /** Holds a reference to the parent image */
    val _image: ImageBuffer = image

    /** @return The value at a given coordinate */
    abstract operator fun get(pos: Pair<Int, Int>): Color?

    /** Sets the value at a given coordinate */
    abstract operator fun set(pos: Pair<Int, Int>, value: Color)

    /** Steps the view to its next position/orientation on the image */
    abstract fun next(): Result<Unit>

    /**
     * Sets the values of a range of coordinates
     *
     * @param startIndex The starting position of the range
     * @param endIndex The ending position of the range
     * @param value The color to set the pixels to
     */
    operator fun set(startIndex: Pair<Int, Int>, endIndex: Pair<Int, Int>, value: Color) {
        (startIndex.second..endIndex.second).forEach { i ->
            (startIndex.first..endIndex.first).forEach { j ->
                set(Pair(i, j), value)
            }
        }
    }

    /**
     * Sets the color of any given coordinates
     *
     * @param indexes The indexes to set the color at
     * @param value The color to set the pixels to
     */
    operator fun set(vararg indexes: Pair<Int, Int>, value: Color) {
        indexes.forEach { set(it, value) }
    }
}