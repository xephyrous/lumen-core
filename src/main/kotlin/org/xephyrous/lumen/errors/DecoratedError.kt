package org.xephyrous.lumen.errors

/**
 * TODO : Document DecoratedError
 */
open class DecoratedError(type: String, message: String, code: Int? = null) : Throwable(message) {
    init {
        print(
            "\n\u001b[31m\u001b[1m[\u001b[0m\u001b[31m ${
                type.uppercase().replace(" ", "-")
            }-ERROR : $message \u001b[1m]"
        )

        if (code != null) {
            print(" <Code $code>")
        }

        print("\u001b[0m\n\n")
    }
}