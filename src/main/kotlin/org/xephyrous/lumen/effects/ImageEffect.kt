package org.xephyrous.lumen.effects

import org.xephyrous.lumen.storage.ImageBuffer
import org.xephyrous.lumen.pipeline.ImageEffector
import org.xephyrous.lumen.pipeline.ImageEffectorType

/**
 * TODO : Document ImageEffect
 */
abstract class ImageEffect : ImageEffector<ImageBuffer, ImageBuffer>() {
    override var type: ImageEffectorType
        get() = ImageEffectorType.EFFECT
        set(_) {}
}