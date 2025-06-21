package org.xephyrous.lumen.pipeline

import org.xephyrous.lumen.utils.TypeReference
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/** Represents the types of image effectors */
enum class ImageEffectorType {
    /** Applies a filter to the image, only changes individual pixel values */
    FILTER,

    /** Applies an effect to the image, modifies any pixel's value */
    EFFECT,

    /** Performs a manipulation of the image's pixels */
    MANIPULATION,

    /** Adds a cut to the image */
    CUTTER,

    /** Walks a kernel over the image */
    KERNEL
}

/**
 * Represents, at the simplest level, any modification that can be performed on an image.
 *
 * @param TIn The type to be passed as a parameter to the effector's [apply] function.
 * @param TOut The type that the effector's [apply] function returns.
 * @param typeRef A reference to [TIn] and [TOut]'s types, avoiding type erasure and allowing for reflection.
 */
abstract class ImageEffector<TIn : Any, TOut : Any> (
    typeRef: TypeReference<Pair<TIn, TOut>>
) {
    val inputType: Type
    val outputType: Type

    init {
        val pairType = typeRef.type as ParameterizedType
        inputType = pairType.actualTypeArguments[0]
        outputType = pairType.actualTypeArguments[1]
    }

    abstract val type: ImageEffectorType

    abstract fun apply(data: TIn): TOut

    fun chainIOCheck(next: ImageEffector<*, *>): Boolean {
        return this.inputType == next.inputType && this.outputType == next.outputType
    }
}