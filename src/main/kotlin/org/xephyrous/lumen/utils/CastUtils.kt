package org.xephyrous.lumen.utils

import kotlin.reflect.KClass

/**
 * Casts from Any to designated type
 *
 * @param value The value to be cast
 * @param targetType The target type to cast to
 *
 * @return The inputted value cast to the target type
 */
fun anyCast(value: Any?, targetType: KClass<*>): Any {
    return when(targetType) {
        String::class -> value as String
        Boolean::class -> value as Boolean
        Int::class -> value as Int
        Short::class -> value as Short
        Long::class -> value as Long
        Double::class -> value as Double
        Char::class -> value as Char
        else -> throw IllegalArgumentException("Unsupported anyCast type conversion : ${targetType.simpleName}")
    }
}

/** Casts an ArrayList of name / value pairs to a designated map collection type */
fun mapCast(list: ArrayList<Pair<String, Any>>, mapType: KClass<*>, targetType: KClass<*>): Any {
    return when (mapType) {
        Map::class -> {
            val buildMap: MutableMap<String, Any> = mutableMapOf()
            list.forEach { buildMap[it.first] = anyCast(it.second, targetType) }
            buildMap
        }

        HashMap::class -> {
            val buildMap: HashMap<String, Any> = hashMapOf()
            list.forEach { buildMap[it.first] = anyCast(it.second, targetType) }
            buildMap
        }

        else -> throw IllegalArgumentException("Unsupported mapCast type conversion : ${targetType.simpleName}")
    }
}