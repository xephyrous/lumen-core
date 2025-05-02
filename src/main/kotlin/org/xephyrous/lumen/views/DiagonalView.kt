package org.xephyrous.lumen.views

import org.xephyrous.lumen.pipeline.ImageData
import java.awt.Color

/**
 * TODO : Document & Finish DiagonalView
 */
class DiagonalView(image: ImageData) : ImageDataView(image) {
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