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
    listOf(iosArm64(), iosSimulatorArm64()).forEach {
        it.binaries.framework { baseName = "SeaGlassSample"; isStatic = true }
    }
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs { browser(); binaries.executable() }
    sourceSets {
        commonMain.dependencies {
            implementation(project(":seaglass-haze"))
            implementation(compose.components.resources)
            implementation(libs.compose.material.icons.core)
        }
        androidMain.dependencies { implementation("androidx.activity:activity-compose:1.10.1") }
        val desktopMain by getting { dependencies { implementation(compose.desktop.currentOs) } }
    }
}
android {
    namespace = "io.github.mariorsh.seaglass.sample"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        applicationId = "io.github.mariorsh.seaglass.sample"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.compileSdk.get().toInt()
        versionCode = 2
        versionName = project.version.toString()
    }
    compileOptions { sourceCompatibility = JavaVersion.VERSION_11; targetCompatibility = JavaVersion.VERSION_11 }
}
compose.desktop { application { mainClass = "io.github.mariorsh.seaglass.sample.MainKt" } }
