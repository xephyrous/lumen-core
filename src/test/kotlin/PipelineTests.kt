import org.xephyrous.lumen.cutters.GridCutter
import org.xephyrous.lumen.filters.*
import org.xephyrous.lumen.io.ImageLoader
import org.xephyrous.lumen.pipeline.ImageEffector
import org.xephyrous.lumen.pipeline.ImagePipeline
import java.awt.image.BufferedImage
import java.io.File
import java.nio.file.Paths
import javax.imageio.ImageIO
import kotlin.run
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
            },
            "brightness_alt" to {
                it.clearEffectors(); it.chain(BrightnessFilter(-0.9f))
            }
        )

        tests.forEach { (name, applyFilter) ->
            val outputSmall = "$outputDir/_${name}_small.jpg"
            val outputLarge = "$outputDir/_${name}_large.jpg"

            runFilterTest("$name - small", inputSmall, outputSmall, applyFilter)
            runFilterTest("$name - large", inputLarge, outputLarge, applyFilter)
        }
    }

    @Test
    fun testFiltersStandalone() {
        val basePath = Paths.get("").toAbsolutePath()
        val inputSmall = "$basePath/src/test/resources/test_image.jpg"
        val inputLarge = "$basePath/src/test/resources/test_image_large.jpg"
        val outputDir = "$basePath/src/test/output"

        val tests = listOf<Pair<String, (BufferedImage) -> BufferedImage>>(
            "sepia" to { SepiaFilter.run(it) },
            "grayscale" to { GrayscaleFilter.run(it) },
            "negative" to { NegativeFilter.run(it) },
            "brightness_add" to { BrightnessFilter.run(it, 75) },
            "brightness_sub" to { BrightnessFilter.run(it, -75) },
            "brightness_alt" to { BrightnessFilter.run(it, -0.9f) },
        )

        tests.forEach { (name, filterLambda) ->
            val outputSmall = File("$outputDir/_${name}_small.jpg")
            val outputLarge = File("$outputDir/_${name}_large.jpg")

            ImageIO.write(filterLambda.invoke(ImageLoader.loadImage(inputSmall).toBufferedImage()), "jpg", outputSmall)
            ImageIO.write(filterLambda.invoke(ImageLoader.loadImage(inputLarge).toBufferedImage()), "jpg", outputLarge)

            assert(outputSmall.exists()) { "Output file does not exist for _${name}_small.jpg" }
            assert(outputLarge.exists()) { "Output file does not exist for _${name}_large.jpg" }
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