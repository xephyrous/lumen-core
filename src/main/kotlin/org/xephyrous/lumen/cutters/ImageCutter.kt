package org.xephyrous.lumen.cutters

import org.xephyrous.lumen.pipeline.ImageData
import org.xephyrous.lumen.pipeline.ImageEffector
import org.xephyrous.lumen.pipeline.ImageEffectorType
import org.xephyrous.lumen.storage.Mask
import kotlin.reflect.KClass

/**
 * TODO : Document ImageCutter
 */
abstract class ImageCutter : ImageEffector<ImageData, ArrayList<Mask>>() {
    override val inputType: KClass<*> = ImageData::class
    override val outputType: KClass<*> = Array<Mask>::class

    override var type: ImageEffectorType
        get() = ImageEffectorType.CUTTER
        set(_) {}
}