package org.xephyrous.lumen.cutters

import org.xephyrous.lumen.storage.ImageBuffer
import org.xephyrous.lumen.pipeline.ImageEffector
import org.xephyrous.lumen.pipeline.ImageEffectorType
import org.xephyrous.lumen.storage.Mask
import org.xephyrous.lumen.utils.typeRef

/**
 * TODO : Document ImageCutter
 */
abstract class ImageCutter : ImageEffector<ImageBuffer, ArrayList<Mask>>(typeRef()) {
    override var type: ImageEffectorType
        get() = ImageEffectorType.CUTTER
        set(_) {}
}