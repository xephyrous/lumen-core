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
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.7.3")
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

tasks.register<Jar>("fatJar") {
    group = "build"
    description = "Assembles a fat jar including all dependencies."
    archiveClassifier.set("with-deps")

    manifest {
        attributes["Implementation-Title"] = "Lumen Image Processing"
        attributes["Implementation-Version"] = version
    }

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    from(sourceSets.main.get().output)

    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith(".jar") }.map { zipTree(it) }
    })
}