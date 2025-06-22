package org.xephyrous.lumen.kernels

import org.xephyrous.lumen.pipeline.ImageEffector
import org.xephyrous.lumen.pipeline.ImageEffectorType
import org.xephyrous.lumen.storage.ImageBuffer
import org.xephyrous.lumen.storage.Quadruple
import org.xephyrous.lumen.utils.components
import org.xephyrous.lumen.utils.typeRef
import java.awt.Color

abstract class ImageKernel(
    /**
     * The width and height of the kernel.
     */
    val kernelSize: Pair<Int, Int>,

    /**
     * A 2D array with [kernelSize].first entries containing [kernelSize].second float values.
     * Each entry represents the weight of a pixel in the kernel.
     */
    val kernelValues: Array<Array<Float>>
) : ImageEffector<ImageBuffer, ImageBuffer>(typeRef()) {
    override val type: ImageEffectorType = ImageEffectorType.KERNEL

    init {
        require(kernelSize.first > 0 && kernelSize.second > 0) {
            "Kernel size must be positive"
        }

        require(kernelValues.size == kernelSize.first) {
            "kernelValues must have ${kernelSize.first} rows"
        }

        require(kernelValues.all { it.size == kernelSize.second }) {
            "Each row in kernelValues must have ${kernelSize.second} columns"
        }
    }

    /**
     * A function applied to the input color before it's multiplied by its kernel weight.
     *
     * @param color The pixel color at (x, y).
     * @param x The x position of the pixel in the image.
     * @param y The y position of the pixel in the image.
     * @param kernelOffsetX The x offset in the kernel.
     * @param kernelOffsetY The y offset in the kernel.
     *
     * @return The modified color.
     */
    open fun preprocessColor(color: Color, x: Int, y: Int, kernelOffsetX: Int, kernelOffsetY: Int): Color {
        return color
    }

    /**
     * A function applied to each individual pixel value in the kernel prior to reduction.
     *
     * @param value The weight value to modify, in range `-1f` to `1f`.
     * @param x The x position of the pixel in the image.
     * @param y The y position of the pixel in the image.
     * @param kernelOffsetX The x offset in the kernel.
     * @param kernelOffsetY The y offset in the kernel.
     *
     * @return The modified weight value.
     */
    open fun preprocessWeight(value: Float, x: Int, y: Int, kernelOffsetX: Int, kernelOffsetY: Int): Float {
        return value
    }

    /**
     * Applies a final transformation to the color components of a pixel after
     * the kernel convolution has been applied.
     *
     * @param r The accumulated red component value before clamping.
     * @param g The accumulated green component value before clamping.
     * @param b The accumulated blue component value before clamping.
     * @param a The accumulated alpha component value before clamping.
     * @param x The x coordinate of the pixel in the source image.
     * @param y The y coordinate of the pixel in the source image.
     *
     * @return The processed Color object after applying clamping or other adjustments.
     */
    open fun postprocessColor(r: Int, g: Int, b: Int, a: Int, x: Int, y: Int): Color {
        return Color(
            r.coerceIn(0, 255),
            g.coerceIn(0, 255),
            b.coerceIn(0, 255),
            a.coerceIn(0, 255)
        )
    }

    /**
     * Combines a list of weighted pixel values into a single pixel output.
     *
     * @param weightedColors List of weighted RGBA colors.
     * @return The combined color as a Color object.
     */
    open fun convolve(weightedColors: List<Quadruple<Float, Float, Float, Float>>): Color {
        var r = 0f
        var g = 0f
        var b = 0f
        var a = 0f

        for (wc in weightedColors) {
            r += wc.first
            g += wc.second
            b += wc.third
            a += wc.fourth
        }

        return Color(
            r.coerceIn(0f, 255f).toInt(),
            g.coerceIn(0f, 255f).toInt(),
            b.coerceIn(0f, 255f).toInt(),
            a.coerceIn(0f, 255f).toInt()
        )
    }

    /**
     * Applies this kernel convolution effect to the given [ImageBuffer]
     *
     * For each pixel in the input image, this method:
     *  - Iterates over the kernel matrix centered on the pixel.
     *  - Applies [preprocessColor] and [preprocessWeight] to each corresponding pixel and kernel weight.
     *  - Multiplies the processed color channels by their processed weight.
     *  - Collects the weighted color contributions into a list.
     *  - Combines the contributions using [convolve].
     *  - Applies [postprocessColor] to the resulting color.
     *  - Writes the final color to the output image buffer.
     *
     * @param data The input [ImageBuffer] to process.
     * @return A new [ImageBuffer] containing the filtered image after applying the kernel.
     */
    override fun apply(data: ImageBuffer): ImageBuffer {
        val (kWidth, kHeight) = kernelSize
        val kCenterY = kHeight / 2
        val kCenterX = kWidth / 2

        val resultPixels = IntArray(data.width * data.height)
        val output = ImageBuffer(data.width, data.height, resultPixels, data.extension)

        for (y in 0 until data.height) {
            for (x in 0 until data.width) {
                val weightedColors = mutableListOf<Quadruple<Float, Float, Float, Float>>()

                for (ky in 0 until kHeight) {
                    for (kx in 0 until kWidth) {
                        val dx = x + kx - kCenterX
                        val dy = y + ky - kCenterY

                        if (dx in 0 until data.width && dy in 0 until data.height) {
                            val color = preprocessColor(data.getColor(dx, dy), dx, dy, kx, ky)
                            val weight = preprocessWeight(kernelValues[ky][kx], dx, dy, kx, ky)

                            weightedColors.add(
                                Quadruple(
                                    color.red * weight,
                                    color.green * weight,
                                    color.blue * weight,
                                    color.alpha * weight
                                )
                            )
                        }
                    }
                }

                val outColor = convolve(weightedColors)
                val (r, g, b, a) = outColor.components()
                data.setColor(x, y, postprocessColor(r, g, b, a, x, y))
            }
        }

        return data
    }
}
