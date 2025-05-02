package org.xephyrous.lumen.pipeline

import java.awt.image.BufferedImage
import kotlin.reflect.KClass

/**
 * Pure image provider class, non-abstract because it is only used as the starting effector
 */
class ImageProvider : ImageEffector<BufferedImage, ImageData>() {
    override val type: ImageEffectorType = ImageEffectorType.PROVIDER
    override val inputType: KClass<*> = Unit::class
    override val outputType: KClass<*> = Unit::class

    override fun apply(data: BufferedImage): ImageData {
        return ImageData(data)
    }
}