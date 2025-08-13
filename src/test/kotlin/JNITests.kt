import kotlin.test.Test

class JNITests {
    external fun nativeCJNITest(value: Int): Int

    companion object {
        init {
            System.loadLibrary("lumen_native_backend")
        }
    }

    @Test
    fun testJNI() {
        val input = 20
        println("Calling nativeCJNITest with input = $input")
        val result = nativeCJNITest(input)
        println("input: $input\noperation: input * 2\nresult: $result")
    }
}