import org.xephyrous.lumen.io.ImageLoader
import java.io.File
import java.nio.file.Paths
import kotlin.test.Test

class IOTests {
    @Test
    fun testImageLoader() {
        ImageLoader.loadImage(File("${Paths.get("").toAbsolutePath()}/src/test/resources/test_image.jpg"))
    }
}