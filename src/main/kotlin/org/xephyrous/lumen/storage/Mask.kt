package org.xephyrous.lumen.storage

import org.xephyrous.lumen.utils.PositionNode
import org.xephyrous.lumen.utils.toInt
import java.awt.Dimension

/**
 * An image byte mask.
 */
typealias ImageMask = Array<Array<Byte>>

/**
 * A masked region of an image with its top-left coordinate
 *
 * @param size The size of the mask
 * @param position The position of the top-left corner of the mask
 */
class Mask(
    val size: Dimension,
    var position: PositionNode = PositionNode(0, 0)
) {
    /**
     * The array of bits in the mask
     *
     * All bits are initialized to 1 (unmasked) by default
     */
    var bits: ImageMask = Array(size.height) { Array(size.width) { 1 } }

    /**
     * Sets all bits to a value (1 or 0)
     *
     * @param value The value to set the bits to
     */
    fun setAll(value: Boolean) {
        for (y in 0..size.height) {
            for (x in 0..size.width) {
                bits[y][x] = value.toInt().toByte()
            }
        }
    }
}