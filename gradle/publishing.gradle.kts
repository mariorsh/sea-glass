import org.gradle.api.publish.maven.MavenPublication
import org.gradle.plugins.signing.SigningExtension

val docs = tasks.register<Jar>("javadocJar") {
    archiveClassifier.set("javadoc")
    from(tasks.named("dokkaHtml"))
}
tasks.withType<Jar>().configureEach {
    from(rootProject.file("licenses")) { into("META-INF/licenses") }
    from(rootProject.file("LICENSE")) { into("META-INF") }
}
configure<PublishingExtension> {
    repositories {
        maven { name = "Staging"; url = rootProject.layout.buildDirectory.dir("staging-repository").get().asFile.toURI() }
    }
    publications.withType<MavenPublication>().configureEach {
        artifact(docs)
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
configure<SigningExtension> {
    val key = System.getenv("SIGNING_KEY")
    if (!key.isNullOrBlank()) {
        useInMemoryPgpKeys(key, System.getenv("SIGNING_PASSWORD"))
        sign(project.extensions.getByType<PublishingExtension>().publications)
    }
}
tasks.register("validateReleasePrerequisites") {
    doLast {
        check(System.getProperty("os.name").contains("Mac")) { "A complete KMP release requires macOS and all Apple targets." }
        check(rootProject.file("LICENSE").isFile) { "Owner must choose a code license before publication." }
        check(providers.gradleProperty("releaseLicenseName").isPresent && providers.gradleProperty("releaseLicenseUrl").isPresent)
        val verifiedNamespace = System.getenv("CENTRAL_NAMESPACE_VERIFIED")?.trim()?.takeIf { it.isNotEmpty() }
        val publicationGroup = project.group.toString()
        check(verifiedNamespace != null && (publicationGroup == verifiedNamespace || publicationGroup.startsWith("$verifiedNamespace."))) {
            "Verify the publication namespace or its parent in Central Portal first."
        }
        check(!System.getenv("SIGNING_KEY").isNullOrBlank()) { "SIGNING_KEY required for release." }
    }
}
