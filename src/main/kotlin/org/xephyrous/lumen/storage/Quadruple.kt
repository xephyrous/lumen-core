package org.xephyrous.lumen.storage

/**
 * A generic container that holds four values of the same type.
 *
 * @param W The type of the [first] value
 * @param X The type of the [second] value
 * @param Y The type of the [third] value
 * @param Z The type of the [fourth] value
 * @property first The first value.
 * @property second The second value.
 * @property third The third value.
 * @property fourth The fourth value.
 */
data class Quadruple<W, X, Y, Z>(
    val first: W,
    val second: X,
    val third: Y,
    val fourth: Z
)

/**
 * A generic tuple class that holds a variable number of values.
 *
 * @param T The type of the contained values.
 * @property values An array holding the tuple's values.
 */
class Tuple<T>(vararg val values: T) {
    /**
     * Retrieves the value at the specified index.
     *
     * @param i The index of the value to retrieve.
     * @return The value at index `i`.
     * @throws ArrayIndexOutOfBoundsException if the index is invalid.
     */
    operator fun get(i: Int) = values[i]
}