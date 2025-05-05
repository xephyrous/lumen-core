package org.xephyrous.lumen.manipulations

import org.xephyrous.lumen.storage.ImageBuffer
import org.xephyrous.lumen.pipeline.ImageEffector
import org.xephyrous.lumen.pipeline.ImageEffectorType

/**
 * TODO : Document ImageManipulation
 */
abstract class ImageManipulation : ImageEffector<ImageBuffer, ImageBuffer>() {
    override var type: ImageEffectorType
        get() = ImageEffectorType.MANIPULATION
        set(_) {}
}