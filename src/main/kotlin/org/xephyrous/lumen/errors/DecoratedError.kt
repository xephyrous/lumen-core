package org.xephyrous.lumen.errors

/**
 * A custom error class with highlighted text, typing, and suggestions.
 *
 * Error types are
 *
 * @param type The type of error
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