package org.xephyrous.lumen.views

import org.xephyrous.lumen.storage.ImageBuffer
import java.awt.Color

/**
 * TODO : Document & Finish RowView
 */
class RowView(image: ImageBuffer) : ImageDataView(image) {
    /** Override of [ImageDataView.get] */
    override fun get(pos: Pair<Int, Int>): Color? {
        TODO("Not yet implemented")
    }

    /** Override of [ImageDataView.set] */
    override fun set(pos: Pair<Int, Int>, value: Color) {
        TODO("Not yet implemented")
    }

    /** Override of [ImageDataView.next] */
    override fun next(): Result<Unit> {
        TODO("Not yet implemented")
    }
}