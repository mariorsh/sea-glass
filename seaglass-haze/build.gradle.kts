import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    id("com.vanniktech.maven.publish")
    id("org.jetbrains.dokka")
}
apply(from = rootProject.file("gradle/publishing.gradle.kts"))
kotlin {
    androidTarget { publishLibraryVariants("release"); compilerOptions { jvmTarget.set(JvmTarget.JVM_11) } }
    jvm("desktop") { compilerOptions { jvmTarget.set(JvmTarget.JVM_11) } }
    iosArm64(); iosSimulatorArm64()
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs { browser() }
    sourceSets {
        commonMain.dependencies {
            api(project(":seaglass-compose"))
            api(libs.haze)
            api(libs.haze.blur)
            implementation(compose.foundation)
        }
    }
}
android {
    namespace = "io.github.mariorsh.seaglass.haze"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig { minSdk = libs.versions.android.minSdk.get().toInt() }
    compileOptions { sourceCompatibility = JavaVersion.VERSION_11; targetCompatibility = JavaVersion.VERSION_11 }
}
