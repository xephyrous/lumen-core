import org.xephyrous.lumen.io.ImageLoader
import org.xephyrous.lumen.pipeline.ImagePipeline
import java.io.File
import java.nio.file.Paths
import kotlin.test.Test

class PipelineTests {
    @Test
    fun testPipeline() {
        val pipeline = ImagePipeline()
        pipeline.loadImage("${Paths.get("").toAbsolutePath()}/src/test/kotlin/test_image.jpg")
        pipeline.run()
        pipeline.save("${Paths.get("").toAbsolutePath()}/src/test/kotlin/test_image_output.jpg")
    }

    @Test
    fun testImageLoader() {
        ImageLoader.loadImage(File("${Paths.get("").toAbsolutePath()}/src/test/kotlin/test_image.jpg"))
    }
}