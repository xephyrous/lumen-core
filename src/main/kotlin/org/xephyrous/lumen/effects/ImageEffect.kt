package org.xephyrous.lumen.effects

import org.xephyrous.lumen.storage.ImageBuffer
import org.xephyrous.lumen.pipeline.ImageEffector
import org.xephyrous.lumen.pipeline.ImageEffectorType
import org.xephyrous.lumen.utils.typeRef

/**
 * TODO : Document ImageEffect
 */
abstract class ImageEffect : ImageEffector<ImageBuffer, ImageBuffer>(typeRef()) {
    override var type: ImageEffectorType
        get() = ImageEffectorType.EFFECT
        set(_) {}
}