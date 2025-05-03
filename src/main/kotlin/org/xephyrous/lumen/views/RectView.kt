package org.xephyrous.lumen.views

import org.xephyrous.lumen.storage.ImageData
import java.awt.Color

/**
 * A rect of [_size] dimensions that moves over the image
 *
 * @param image A reference to the parent image
 * @param snap Whether the rect should move by one pixel each step (`false`), or snap to the edge of the last rect (`true`).
 * (Defaults to `false`)
 */
class RectView(
    private val image: ImageData,
    size: Int, private val snap: Boolean = false
) : ImageDataView(image) {
    /** The size of the rect (width, height) */
    private val _size: Pair<Int, Int> = Pair(3, 3)

    /** The top-left corner of the current rect (x, y) */
    private val _pos: Pair<Int, Int> = Pair(0, 0)

    /**
     * Checks if a given position is within the current rect, and does not extend past the image's bounds
     *
     * @param pos The coordinates to check
     * @return Whether the given position is withing the current rect & image bounds
     */
    private fun boundsCheck(pos: Pair<Int, Int>): Boolean {
        return pos.first in _pos.first..(_pos.first + _size.first).coerceIn(0.._image.width)
                && pos.second in _pos.second..(_pos.second + _size.second).coerceIn(0.._image.height)
    }

    /** Override of [ImageDataView.get] */
    override fun get(pos: Pair<Int, Int>): Color? {
        if (!boundsCheck(pos)) {
            return null
        }

        return _image.colorAt(_image.indexOf(pos))
    }

    /** Override of [ImageDataView.set] */
    override fun set(pos: Pair<Int, Int>, value: Color) {
        if (!boundsCheck(pos)) {
            throw IndexOutOfBoundsException("Invalid position in image!")
        }

        // image.data()[image.indexOf()]
    }

    /** Override of [ImageDataView.next] */
    override fun next(): Result<Unit> {
        TODO("Not yet implemented")
    }
}