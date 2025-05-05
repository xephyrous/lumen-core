package org.xephyrous.lumen.cutters

import org.xephyrous.lumen.storage.ImageBuffer
import org.xephyrous.lumen.pipeline.ImageEffector
import org.xephyrous.lumen.pipeline.ImageEffectorType
import org.xephyrous.lumen.storage.Mask
import kotlin.reflect.KClass

/**
 * TODO : Document ImageCutter
 */
abstract class ImageCutter : ImageEffector<ImageBuffer, ArrayList<Mask>>() {
    override val inputType: KClass<*> = ImageBuffer::class
    override val outputType: KClass<*> = Array<Mask>::class

    override var type: ImageEffectorType
        get() = ImageEffectorType.CUTTER
        set(_) {}
}