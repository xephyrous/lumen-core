import org.xephyrous.lumen.filters.BrightnessFilter
import org.xephyrous.lumen.filters.GrayscaleFilter
import org.xephyrous.lumen.filters.NegativeFilter
import org.xephyrous.lumen.filters.SepiaFilter
import org.xephyrous.lumen.pipeline.ImagePipeline
import java.io.File
import java.nio.file.Paths
import javax.imageio.ImageIO
import kotlin.test.Test
import kotlin.time.measureTime

class PipelineTests {
    @Test
    fun testFilters() {
        val inputSmall = "${Paths.get("").toAbsolutePath()}/src/test/resources/test_image.jpg"
        val inputLarge = "${Paths.get("").toAbsolutePath()}/src/test/resources/test_image_large.jpg"

        val filters = arrayOf(
            Pair(::sepiaFilterTest, "sepiaFilterTest"),
            Pair(::grayscaleFilterTest, "grayscaleFilterTest"),
            Pair(::negativeFilterTest, "negativeFilterTest"),
            Pair(::brightnessAddFilterTest, "brightnessAddFilterTest"),
            Pair(::brightnessSubFilterTest, "brightnessSubFilterTest"),
            Pair(::brightnessNoneFilterTest, "brightnessNoneFilterTest"),
        )

        filters.forEach { filter ->
            timeTest(
                inputSmall,
                "${Paths.get("").toAbsolutePath()}/src/test/output/_${filter.second}_small.jpg",
                filter.first, "${filter.second} - small"
            )

            timeTest(
                inputLarge,
                "${Paths.get("").toAbsolutePath()}/src/test/output/_${filter.second}_large.jpg",
                filter.first, "${filter.second} - large"
            )
        }
    }

    fun sepiaFilterTest(input: String, output: String) {
        val pipeline = ImagePipeline()
        pipeline.loadImage(input)
        pipeline.chain(SepiaFilter())
        pipeline.run()

        val output = File(output)
        pipeline.save(output.absolutePath)

        assert(output.exists()) { "Output file does not exist" }
    }

    fun grayscaleFilterTest(input: String, output: String) {
        val pipeline = ImagePipeline()
        pipeline.loadImage(input)
        pipeline.chain(GrayscaleFilter())
        pipeline.run()

        val output = File(output)
        pipeline.save(output.absolutePath)

        assert(output.exists()) { "Output file does not exist" }
    }

    fun negativeFilterTest(input: String, output: String) {
        val pipeline = ImagePipeline()
        pipeline.loadImage(input)
        pipeline.chain(NegativeFilter())
        pipeline.run()

        val output = File(output)
        pipeline.save(output.absolutePath)

        assert(output.exists()) { "Output file does not exist" }
    }

    fun brightnessAddFilterTest(input: String, output: String) {
        val pipeline = ImagePipeline()
        pipeline.loadImage(input)
        pipeline.chain(BrightnessFilter(75))
        pipeline.run()

        val output = File(output)
        pipeline.save(output.absolutePath)

        assert(output.exists()) { "Output file does not exist" }
    }

    fun brightnessSubFilterTest(input: String, output: String) {
        val pipeline = ImagePipeline()
        pipeline.loadImage(input)
        pipeline.chain(BrightnessFilter(-75))
        pipeline.run()

        val output = File(output)
        pipeline.save(output.absolutePath)

        assert(output.exists()) { "Output file does not exist" }
    }

    fun brightnessNoneFilterTest(input: String, output: String) {
        val pipeline = ImagePipeline()
        pipeline.loadImage(input)
        pipeline.chain(BrightnessFilter(50))
        pipeline.chain(BrightnessFilter(-50))
        pipeline.run()

        val output = File(output)
        pipeline.save(output.absolutePath)

        assert(output.exists()) { "Output file does not exist" }
    }

    fun timeTest(input: String, output: String, func: (String, String) -> Unit, name: String) {
        val time = measureTime {
            func(input, output)
        }

        val image = ImageIO.read(File(input))
        println("[$name] : $time - ${time.inWholeNanoseconds / (image.width * image.height)}ns/px")
    }
}