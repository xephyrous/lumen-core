import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.xephyrous.lumen.cutters.GridCutter
import org.xephyrous.lumen.cutters.ImageCutter
import org.xephyrous.lumen.effects.ImageEffect
import org.xephyrous.lumen.filters.BrightnessFilter
import org.xephyrous.lumen.filters.GrayscaleFilter
import org.xephyrous.lumen.filters.ImageFilter
import org.xephyrous.lumen.filters.NegativeFilter
import org.xephyrous.lumen.filters.SepiaFilter
import org.xephyrous.lumen.kernels.BoxBlurKernel
import org.xephyrous.lumen.kernels.ImageKernel
import org.xephyrous.lumen.manipulations.ImageManipulation
import kotlin.reflect.KClass
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.isSubclassOf

class ValidationTests {
    fun <T : Any> enforceRunMethod(
        baseClass: KClass<T>,
        classesToCheck: List<KClass<*>>
    ): List<KClass<*>> {
        return classesToCheck.filter { clazz ->
            val hasRun = clazz.declaredFunctions.any { it.name == "run" } ||
                    clazz.companionObject?.declaredFunctions?.any { it.name == "run" } == true

            clazz.isSubclassOf(baseClass) && !hasRun
        }
    }

    object ClassRegistry {
        val cutters: List<KClass<out ImageCutter>> = listOf(
            GridCutter::class
        )

        val effects: List<KClass<out ImageEffect>> = listOf()

        val filters: List<KClass<out ImageFilter>> = listOf(
            BrightnessFilter::class,
            GrayscaleFilter::class,
            NegativeFilter::class,
            SepiaFilter::class
        )

        val manipulations: List<KClass<out ImageManipulation>> = listOf()

        val kernels: List<KClass<out ImageKernel>> = listOf(
            BoxBlurKernel::class
        )
    }

    @Test
    fun validateRunMethodImplementations() {
        val missingRunClasses = mutableListOf<KClass<*>>()

        missingRunClasses += enforceRunMethod(ImageCutter::class, ClassRegistry.cutters)
        missingRunClasses += enforceRunMethod(ImageEffect::class, ClassRegistry.effects)
        missingRunClasses += enforceRunMethod(ImageFilter::class, ClassRegistry.filters)
        missingRunClasses += enforceRunMethod(ImageManipulation::class, ClassRegistry.manipulations)
        missingRunClasses += enforceRunMethod(ImageKernel::class, ClassRegistry.kernels)

        if (missingRunClasses.isNotEmpty()) {
            fail(
                "The following classes are missing 'run' method implementation:\n" +
                        missingRunClasses.joinToString(separator = "\n") { " - " + (it.qualifiedName ?: it.simpleName ?: "Unknown") }
            )
        }
    }
}