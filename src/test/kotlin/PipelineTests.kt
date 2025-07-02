import org.xephyrous.lumen.filters.*
import org.xephyrous.lumen.io.ImageLoader
import org.xephyrous.lumen.kernels.BoxBlurKernel
import org.xephyrous.lumen.pipeline.ImagePipeline
import org.xephyrous.lumen.pipeline.Pipeline
import java.awt.image.BufferedImage
import java.io.File
import java.nio.file.Paths
import javax.imageio.ImageIO
import kotlin.test.Test
import kotlin.time.measureTime

class PipelineTests {
    val basePath: String = Paths.get("").toAbsolutePath().toString()
    val inputSmall = "$basePath/src/test/resources/test_image.jpg"
    val inputLarge = "$basePath/src/test/resources/test_image_large.jpg"
    val outputDir = "$basePath/src/test/output"

    @Test
    fun testFilters() {
        val tests = listOf<Pair<String, (Pipeline) -> Unit>>(
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
            "brightness_alt" to { it.clearEffectors(); it.chain(BrightnessFilter(-0.9f)) },
            "contrast_min" to { it.clearEffectors(); it.chain(ContrastFilter(-128)) },
            "contrast_max" to { it.clearEffectors(); it.chain(ContrastFilter(128)) },
            "contrast_alt" to { it.clearEffectors(); it.chain(ContrastFilter(0.5f)) }
        )

        tests.forEach { (name, applyFilter) ->
            val outputSmall = "$outputDir/_${name}_small.jpg"
            val outputLarge = "$outputDir/_${name}_large.jpg"

            runEffectorTest("$name - small", inputSmall, outputSmall, applyFilter)
            runEffectorTest("$name - large", inputLarge, outputLarge, applyFilter)
        }
    }

    @Test
    fun testFiltersStandalone() {
        val tests = listOf<Pair<String, (BufferedImage) -> BufferedImage>>(
            "sepia" to { SepiaFilter.run(it) },
            "grayscale" to { GrayscaleFilter.run(it) },
            "negative" to { NegativeFilter.run(it) },
            "brightness_add" to { BrightnessFilter.run(it, 75) },
            "brightness_sub" to { BrightnessFilter.run(it, -75) },
            "brightness_alt" to { BrightnessFilter.run(it, -0.9f) }
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

    @Test
    fun testKernelsSmall() {
        val tests = listOf<Pair<String, (Pipeline) -> Unit>>(
            "box_blur" to { it.clearEffectors(); it.chain(BoxBlurKernel(10)) },
        )

        tests.forEach { (name, applyFilter) ->
            val outputSmall = "$outputDir/_${name}_small.jpg"
            runEffectorTest("$name - small", inputSmall, outputSmall, applyFilter)
        }
    }

    @Test
    fun testKernelsLarge() {
        val tests = listOf<Pair<String, (Pipeline) -> Unit>>(
            "box_blur" to { it.clearEffectors(); it.chain(BoxBlurKernel(100)) },
        )

        tests.forEach { (name, applyFilter) ->
            val outputLarge = "$outputDir/_${name}_large.jpg"
            runEffectorTest("$name - large", inputLarge, outputLarge, applyFilter)
        }
    }

    private fun runEffectorTest(name: String, input: String, output: String, applyEffector: (Pipeline) -> Unit) {
        val pipeline = ImagePipeline { loadImage(File(input)) }
        applyEffector(pipeline)

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