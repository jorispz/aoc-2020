plugins {
    kotlin("multiplatform") version "1.4.20"
}
group = "me.jopo"
version = "1.0-SNAPSHOT"
val coroutinesVersion = "1.4.1-native-mt"

repositories {
    mavenCentral()
    jcenter()
}
kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
    }
    js(IR) {
        nodejs()
    }
    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        hostOs == "Mac OS X" -> macosX64("native")
        hostOs == "Linux" -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    mingwX64("native") {
        binaries {
            executable { }
        }
    }


    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
            }
        }

        val jvmMain by getting
        val jsMain by getting
        val nativeMain by getting
    }
}