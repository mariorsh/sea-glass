import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinMultiplatform
import com.vanniktech.maven.publish.MavenPublishBaseExtension

plugins {
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.androidApplication) apply false
    id("org.jetbrains.kotlinx.binary-compatibility-validator") version "0.17.0"
    id("org.jetbrains.dokka") version "1.9.20" apply false
    id("com.vanniktech.maven.publish") version "0.34.0" apply false
}

allprojects {
    group = providers.gradleProperty("GROUP").get()
    version = providers.gradleProperty("VERSION_NAME").get()
}
apiValidation { ignoredProjects.add("sample") }

// Release: ./gradlew publishAndReleaseToMavenCentral (see .github/workflows/release.yml).
// Credentials/signing come from ORG_GRADLE_PROJECT_mavenCentralUsername, ...Password,
// signingInMemoryKey, signingInMemoryKeyPassword.
subprojects {
    plugins.withId("com.vanniktech.maven.publish") {
        extensions.configure<MavenPublishBaseExtension> {
            configure(
                KotlinMultiplatform(
                    javadocJar = JavadocJar.Dokka("dokkaHtml"),
                    sourcesJar = true,
                    androidVariantsToPublish = listOf("release"),
                )
            )
            publishToMavenCentral()
            if (providers.gradleProperty("signingInMemoryKey").isPresent) signAllPublications()

            pom {
                name.set(project.name)
                description.set("Sea Glass components for Compose Multiplatform")
                url.set("https://github.com/mariorsh/sea-glass")
                developers { developer { id.set("mariorsh"); name.set("Mario Rios Holgado"); url.set("https://github.com/mariorsh") } }
                scm {
                    url.set("https://github.com/mariorsh/sea-glass")
                    connection.set("scm:git:https://github.com/mariorsh/sea-glass.git")
                    developerConnection.set("scm:git:ssh://git@github.com/mariorsh/sea-glass.git")
                }
                val licenseName = providers.gradleProperty("releaseLicenseName")
                val licenseUrl = providers.gradleProperty("releaseLicenseUrl")
                if (licenseName.isPresent && licenseUrl.isPresent) {
                    licenses { license { name.set(licenseName); url.set(licenseUrl) } }
                }
            }
        }
    }
}

tasks.register("validateLocalPublication") {
    description = "Validate staged POMs, documentation, sources, metadata and font notices without releasing."
    dependsOn(":seaglass-compose:publishAllPublicationsToStagingRepository", ":seaglass-haze:publishAllPublicationsToStagingRepository")
    doLast {
        val repo = layout.buildDirectory.dir("staging-repository").get().asFile
            .resolve(project.group.toString().replace('.', '/'))
        val poms = repo.walkTopDown().filter { it.extension == "pom" }.toList()
        check(poms.isNotEmpty()) { "Stage publications first." }
        poms.forEach { pom ->
            val xml = javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(pom)
            check(xml.getElementsByTagName("groupId").item(0).textContent == project.group.toString()) { "Wrong group in $pom" }
            listOf("name", "description", "url", "developers", "scm").forEach { tag ->
                check(xml.getElementsByTagName(tag).length > 0) { "Missing $tag in $pom" }
            }
            val stem = pom.nameWithoutExtension
            check(java.io.File(pom.parentFile, "$stem-javadoc.jar").isFile) { "Missing documentation for $pom" }
            check(java.io.File(pom.parentFile, "$stem-sources.jar").isFile) { "Missing sources for $pom" }
        }
        listOf("seaglass-compose", "seaglass-haze").forEach { module ->
            check(repo.walkTopDown().any { it.name == "$module-${project.version}.module" })
        }
        val desktop = repo.walkTopDown().first { it.name == "seaglass-compose-desktop-${project.version}.jar" }
        java.util.zip.ZipFile(desktop).use { zip ->
            listOf("OFL-BricolageGrotesque.txt", "OFL-Figtree.txt", "OFL-JetBrainsMono.txt").forEach {
                check(zip.getEntry("META-INF/licenses/$it") != null) { "Missing font notice $it" }
            }
        }
        logger.lifecycle("Staged metadata, sources, documentation and font notices verified.")
    }
}
