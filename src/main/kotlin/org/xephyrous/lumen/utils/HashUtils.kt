package org.xephyrous.lumen.utils

import java.security.MessageDigest

/**
 * Generates a stable/static hash for a given object
 * @param obj The object to be hashed
 * @return The hash as a [String]
 */
fun getStableHash(obj: Any): String {
    val messageDigest = MessageDigest.getInstance("SHA-1")
    val bytes = obj.toString().toByteArray()
    val hashBytes = messageDigest.digest(bytes)
    return hashBytes.joinToString("") { String.format("%02x", it) }
}