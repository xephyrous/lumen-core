package org.xephyrous.lumen.utils

import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KCallable
import kotlin.reflect.KClass

/**
 * Extracts the [KClass] of the return type from a given [KCallable].
 *
 * This function uses Kotlin reflection to determine the runtime class of the return type
 * of a callable (e.g., function, property).
 *
 * @param obj The callable from which to extract the return type's class.
 * @return The [KClass] representing the return type of the callable.
 *
 * @throws ClassCastException if the classifier is not a [KClass].
 */
fun extractClass(obj: KCallable<*>): KClass<*> {
    return obj.returnType.classifier as KClass<*>
}

/**
 * Extracts the [KClass] of a specific type argument from the return type of a [KCallable].
 *
 * This is useful when dealing with parameterized return types (e.g., `List<String>`) and you
 * want to reflectively determine the class of one of the generic type parameters.
 *
 * @param obj The callable whose return type has type arguments.
 * @param index The index of the type parameter to extract.
 * @return The [KClass] of the specified type parameter.
 *
 * @throws IndexOutOfBoundsException If the index is out of range for the return type's arguments.
 * @throws ClassCastException if the classifier at the index is not a [KClass].
 */
fun extractClassParam(obj: KCallable<*>, index: Int): KClass<*> {
    if (index >= obj.returnType.arguments.size) {
        throw IndexOutOfBoundsException("Invalid type parameter index of $index!")
    }

    return obj.returnType.arguments[index].type?.classifier as KClass<*>
}

/**
 * Determines whether a given property on an instance is backed by a delegated property.
 *
 * Kotlin uses backing fields named `<propertyName>$delegate` to store delegated values.
 * This function uses Java reflection to detect if such a field exists and whether it is
 * a type of [ReadWriteProperty] or [ReadOnlyProperty].
 *
 * @param instance The object instance containing the property.
 * @param name The name of the property to check (without `$delegate` suffix).
 * @return `true` if the property is delegated, `false` otherwise.
 */
fun isFieldDelegate(instance: Any, name: String): Boolean {
    val _class = instance::class.java

    return try {
        val field = _class.getDeclaredField("$name\$delegate")
        field.isAccessible = true

        val fieldType = field.type
        ReadWriteProperty::class.java.isAssignableFrom(fieldType) ||
                ReadOnlyProperty::class.java.isAssignableFrom(fieldType)
    } catch (e: NoSuchFieldException) {
        false
    }
}

/**
 * Creates a [TypeReference] instance for a [Pair] of input and output types, [TIn] and [TOut],
 * while preserving their generic type information at runtime.
 *
 * This function simplifies the creation of type references used to avoid type erasure in generics,
 * particularly in scenarios where runtime reflection is required (e.g., for validating or dispatching image effectors).
 *
 * Usage example:
 * ```
 * class MyEffector : ImageEffector<ImageBuffer, ImageBuffer>(
 *     typeRef() // Automatically resolves to TypeReference<Pair<ImageBuffer, ImageBuffer>>
 * ) { ... }
 * ```
 *
 * @param TIn The type expected as input to the effect.
 * @param TOut The type produced as output by the effect.
 * @return A [TypeReference] holding the full generic type [Pair]<[TIn], [TOut]>.
 */
inline fun <reified TIn : Any, reified TOut : Any> typeRef(): TypeReference<Pair<TIn, TOut>> {
    return object : TypeReference<Pair<TIn, TOut>>() {}
}