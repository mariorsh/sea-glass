import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}
kotlin {
    androidTarget { compilerOptions { jvmTarget.set(JvmTarget.JVM_11) } }
    jvm("desktop")
    listOf(iosX64(), iosArm64(), iosSimulatorArm64()).forEach {
        it.binaries.framework { baseName = "SeaGlassSample"; isStatic = true }
    }
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs { browser(); binaries.executable() }
    sourceSets {
        commonMain.dependencies {
            implementation(project(":seaglass-haze"))
            implementation(compose.components.resources)
        }
        androidMain.dependencies { implementation("androidx.activity:activity-compose:1.10.1") }
        val desktopMain by getting { dependencies { implementation(compose.desktop.currentOs) } }
    }
}
android {
    namespace = "io.github.mariorsh.seaglass.sample"
    compileSdk = 35
    defaultConfig { applicationId = "io.github.mariorsh.seaglass.sample"; minSdk = 24; targetSdk = 35; versionCode = 2; versionName = project.version.toString() }
    compileOptions { sourceCompatibility = JavaVersion.VERSION_11; targetCompatibility = JavaVersion.VERSION_11 }
}
compose.desktop { application { mainClass = "io.github.mariorsh.seaglass.sample.MainKt" } }
