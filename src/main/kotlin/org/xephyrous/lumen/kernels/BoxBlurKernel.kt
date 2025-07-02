package org.xephyrous.lumen.kernels

class BoxBlurKernel(
    width: Int = 3,
    height: Int = 3
) : ImageKernel(
    kernelSize = width to height,
    kernelValues = Array(height) { Array(width) { 1.0f / (width * height) } }
) {
    constructor(size: Int) : this(size, size)
}