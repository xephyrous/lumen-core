package org.xephyrous.lumen.kernels

import org.xephyrous.lumen.pipeline.ImageEffector
import org.xephyrous.lumen.pipeline.ImageEffectorType
import org.xephyrous.lumen.storage.ImageBuffer
import org.xephyrous.lumen.utils.typeRef

abstract class ImageKernel : ImageEffector<ImageBuffer, ImageBuffer>(typeRef()) {
    override val type: ImageEffectorType = ImageEffectorType.KERNEL
}