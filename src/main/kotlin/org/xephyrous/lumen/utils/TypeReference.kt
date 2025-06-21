package org.xephyrous.lumen.utils

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

abstract class TypeReference<T> {
    val type: Type = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0]
}