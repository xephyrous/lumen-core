package org.xephyrous.lumen.cutters

import org.xephyrous.lumen.storage.ImageBuffer
import org.xephyrous.lumen.storage.Mask
import org.xephyrous.lumen.utils.PositionNode
import java.awt.Dimension
import kotlin.math.floor
import kotlin.math.min

/**
 * Cuts an image into a grid of [gridX] x [gridY] tiles
 *
 * @param gridX The number of columns to cut
 * @param gridY The number of rows to cut
 */
class GridCutter(private var gridX: Int, private var gridY: Int) : ImageCutter() {

    /**
     *
     */
    fun setGridX(xVal: Int) {

    }

    /**
     *
     */
    fun setGridY(yVal: Int) {

    }

    /**
     * Cuts the loaded image into a grid of [gridX] x [gridY] tiles
     *
     * @param data The image to cut
     *
     * @return The cut pieces of the image as an [ArrayList] of [Mask]s
     */
    override fun apply(data: ImageBuffer): ArrayList<Mask> {
        val masks: ArrayList<Mask> = arrayListOf()

        val spacingX = floor(data.width.toDouble() / gridX).toInt()
        val spacingY = floor(data.height.toDouble() / gridY).toInt()

        for (y: Int in 0..data.width - spacingX step spacingX) {
            for (x: Int in 0..data.height - spacingY step spacingY) {
                masks.add(
                    Mask(
                        Dimension(min(spacingX, data.width - x), min(spacingY, data.height - y)),
                        PositionNode(x, y)
                    )
                )
            }
        }

        return masks
    }
}