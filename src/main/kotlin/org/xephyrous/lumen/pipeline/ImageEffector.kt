package org.xephyrous.lumen.pipeline

import kotlin.reflect.KClass

/** Represents the types of image effectors */
enum class ImageEffectorType {
    /** Provides an ImageData object, always the first effector in the chain */
    PROVIDER,

    /** Applies a filter to the image, only changes individual pixel values */
    FILTER,

    /** Applies an effect to the image, modifies any pixel's value */
    EFFECT,

    /** Performs a manipulation of the image's pixels */
    MANIPULATION,

    /** Adds a cut to the image */
    CUTTER
}

/**
 * TODO : Document ImageEffector
 */
abstract class ImageEffector<TIn, TOut> {
    abstract val type: ImageEffectorType
    abstract val inputType: KClass<*>
    abstract val outputType: KClass<*>

    abstract fun apply(data: TIn): TOut

    protected fun chainIOCheck(next: ImageEffector<*, *>): Boolean {
        return this.inputType == next.inputType && this.outputType == next.outputType
    }
}