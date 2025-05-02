package org.xephyrous.lumen.effects

import org.xephyrous.lumen.pipeline.ImageData
import org.xephyrous.lumen.pipeline.ImageEffector
import org.xephyrous.lumen.pipeline.ImageEffectorType

/**
 * TODO : Document ImageEffect
 */
abstract class ImageEffect : ImageEffector<ImageData, ImageData>() {
    override var type: ImageEffectorType
        get() = ImageEffectorType.EFFECT
        set(_) {}
}