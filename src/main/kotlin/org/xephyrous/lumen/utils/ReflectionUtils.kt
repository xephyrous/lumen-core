package org.xephyrous.lumen.utils

import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KCallable
import kotlin.reflect.KClass

/**
 * TODO : Document
 */
fun extractClass(obj: KCallable<*>): KClass<*> {
    return obj.returnType.classifier as KClass<*>
}

/**
 * TODO : Document
 */
fun extractClassParam(obj: KCallable<*>, index: Int): KClass<*> {
    if (index >= obj.returnType.arguments.size) {
        throw IndexOutOfBoundsException("Invalid type parameter index of $index!")
    }

    return obj.returnType.arguments[index].type?.classifier as KClass<*>
}

/**
 * TODO : Document
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