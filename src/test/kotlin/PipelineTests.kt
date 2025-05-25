import org.xephyrous.lumen.filters.*
import org.xephyrous.lumen.pipeline.ImagePipeline
import java.io.File
import java.nio.file.Paths
import javax.imageio.ImageIO
import kotlin.test.Test
import kotlin.time.measureTime

class PipelineTests {

    @Test
    fun testFilters() {
        val basePath = Paths.get("").toAbsolutePath()
        val inputSmall = "$basePath/src/test/resources/test_image.jpg"
        val inputLarge = "$basePath/src/test/resources/test_image_large.jpg"
        val outputDir = "$basePath/src/test/output"

        val tests = listOf<Pair<String, (ImagePipeline) -> Unit>>(
            "sepia" to { it.clearEffectors(); it.chain(SepiaFilter()) },
            "grayscale" to { it.clearEffectors(); it.chain(GrayscaleFilter()) },
            "negative" to { it.clearEffectors(); it.chain(NegativeFilter()) },
            "brightness_add" to { it.clearEffectors(); it.chain(BrightnessFilter(75)) },
            "brightness_sub" to { it.clearEffectors(); it.chain(BrightnessFilter(-75)) },
            "brightness_none" to {
                it.clearEffectors()
                it.chain(BrightnessFilter(50))
                it.chain(BrightnessFilter(-50))
            }
        )

        tests.forEach { (name, applyFilter) ->
            val outputSmall = "$outputDir/_${name}_small.jpg"
            val outputLarge = "$outputDir/_${name}_large.jpg"

            runFilterTest("$name - small", inputSmall, outputSmall, applyFilter)
            runFilterTest("$name - large", inputLarge, outputLarge, applyFilter)
        }
    }

    private fun runFilterTest(name: String, input: String, output: String, applyFilter: (ImagePipeline) -> Unit) {
        val pipeline = ImagePipeline()
        pipeline.loadImage(input)
        applyFilter(pipeline)

        // Time only the filter application
        val time = measureTime {
            pipeline.run()
        }

        pipeline.save(output)

        val file = File(output)
        assert(file.exists()) { "Output file does not exist for $name" }

        val image = ImageIO.read(File(input))
        println("[$name] : $time | ${time.inWholeNanoseconds / (image.width * image.height)}ns/px")
    }
}