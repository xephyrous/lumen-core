package org.xephyrous.lumen.kernels

import org.xephyrous.lumen.storage.Quadruple
import org.xephyrous.lumen.utils.average
import org.xephyrous.lumen.utils.toColor
import java.awt.Color

class BoxBlurKernel(
    width: Int = 3,
    height: Int = 3
) : ImageKernel(
    kernelSize = width to height,
    kernelValues = Array(height) { Array(width) { 1.0f / (width * height) } }
) {
    constructor(size: Int) : this(size, size)
}