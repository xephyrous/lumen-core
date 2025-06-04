plugins {
    kotlin("jvm") version "2.1.10"
}

group = "org.xephyrous.lumen"
version = "1.0.0-alpha"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlin:kotlin-reflect:2.1.10")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

tasks.register("clearOutput") {
    val outputDir = file("src/test/output")

    doLast {
        if (outputDir.exists()) {
            outputDir.deleteRecursively()
            outputDir.mkdirs()
            println("Output folder cleared.")
        } else {
            println("Output folder does not exist - creating.")
            outputDir.mkdirs()
        }
    }
}