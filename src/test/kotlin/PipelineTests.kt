import org.xephyrous.lumen.io.ImageLoader
import org.xephyrous.lumen.pipeline.ImagePipeline
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.test.Test

class PipelineTests {
    @Test
    fun testPipeline() {
        val pipeline = ImagePipeline()
        pipeline.run()
        pipeline.loadImage(ImageIO.read(File("C:\\Users\\alexa\\Pictures\\bwaaa.jpg")))
    }

    @Test
    fun testImageLoader() {
        ImageLoader.loadImage(File("C:\\Users\\alexa\\Pictures\\bwaaa.jpg"))
    }
}