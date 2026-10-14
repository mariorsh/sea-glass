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
    androidTarget {
        publishLibraryVariants("release")
        compilerOptions { jvmTarget.set(JvmTarget.JVM_11) }
    }

    jvm("desktop") {
        compilerOptions { jvmTarget.set(JvmTarget.JVM_11) }
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }

    sourceSets {
        commonTest.dependencies { implementation(kotlin("test")) }
        val desktopTest by getting {
            dependencies {
                implementation(compose.desktop.uiTestJUnit4)
                implementation(compose.desktop.currentOs)
            }
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.ui)
            implementation(compose.animation)
            api(compose.material3)
            api(libs.compose.material.icons.core)
            implementation(compose.components.resources)
        }
    }
}

android {
    namespace = "io.github.mariorsh.seaglass"
    sourceSets["main"].assets.srcDir(rootProject.file("licenses"))
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

compose.resources {
    publicResClass = false
    packageOfResClass = "io.github.mariorsh.seaglass.compose.resources"
    generateResClass = always
}

publishing {
    repositories {
        // ./gradlew publishAllPublicationsToGitHubPackagesRepository
        // Necesita GITHUB_ACTOR y GITHUB_TOKEN (con write:packages) y la URL de tu repo.
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/" + (System.getenv("GITHUB_REPOSITORY") ?: "TU_USUARIO/sea-glass"))
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
