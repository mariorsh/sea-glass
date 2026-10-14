import org.gradle.api.publish.PublishingExtension

// Maven Central publishing itself is configured in the root build.gradle.kts (vanniktech plugin).
tasks.withType<Jar>().configureEach {
    from(rootProject.file("licenses")) { into("META-INF/licenses") }
    from(rootProject.file("LICENSE")) { into("META-INF") }
}
// Local staging repository used by CI (validateLocalPublication) to verify what would be published.
extensions.configure<PublishingExtension> {
    repositories {
        maven { name = "Staging"; url = rootProject.layout.buildDirectory.dir("staging-repository").get().asFile.toURI() }
    }
}
