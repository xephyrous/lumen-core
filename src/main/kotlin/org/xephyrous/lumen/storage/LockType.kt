package org.xephyrous.lumen.storage

/**
 * Protects a variable from race conditions by "locking" the value
 * It will also hold any values attempted to be set while locked and update after it is unlocked
 *
 * @param T The type of the data being stored
 * @param lockVal The data to be stored, of type [T]
 */
class LockType<T>(lockVal: T) {
    /** The current value of the variable */
    var value: T = lockVal
        get() {
            if(holdVal != null && !locked) {
                value = holdVal!!
                holdVal = null
            }

            return field
        }
        set(newVal) {
            if (locked) {
                holdVal = newVal
                return
            }

            field = newVal
        }

    /** If the variable is locked */
    private var locked: Boolean = false

    /** Any queued value */
    private var holdVal: T? = null

    /** Returns if the value is locked */
    fun locked(): Boolean {
        return locked
    }

    /** Locks the value, it cannot be modified */
    fun lock() {
        locked = true
    }

    /** Unlocks the property, it can be modified */
    fun unlock() {
        locked = false
    }
}