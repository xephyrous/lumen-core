package org.xephyrous.lumen.errors

/**
 * TODO : Document DecoratedError
 */
open class DecoratedError(
    type: String,
    message: String,
    suggestion: String,
    code: Int? = null
) : Throwable(
    buildString {
        append(type.uppercase().replace(" ", "-"))
        append(" : ")
        append(message)
        if (code != null) {
            append(" <Code $code>")
        }
        append("\n + $suggestion\n")
    }
)