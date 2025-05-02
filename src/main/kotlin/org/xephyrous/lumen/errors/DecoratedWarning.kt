package org.xephyrous.lumen.errors

/**
 * TODO : Document DecoratedWarning
 */
open class DecoratedWarning(type: String, message: String) : Exception(message) {
    init {
        print("\u001b[31m")
        println("\u001b[1m[\u001B[0m\u001b[31m ${type.uppercase()}-WARNING : $message \u001B[1m]\u001B[0m")
        print("\u001b[0m")
    }
}