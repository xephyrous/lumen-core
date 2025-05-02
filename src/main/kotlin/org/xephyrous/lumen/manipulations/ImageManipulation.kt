package org.xephyrous.lumen.manipulations

import org.xephyrous.lumen.pipeline.ImageData
import org.xephyrous.lumen.pipeline.ImageEffector
import org.xephyrous.lumen.pipeline.ImageEffectorType

/**
 * TODO : Document ImageManipulation
 */
abstract class ImageManipulation : ImageEffector<ImageData, ImageData>() {
    override var type: ImageEffectorType
        get() = ImageEffectorType.MANIPULATION
        set(_) {}
}