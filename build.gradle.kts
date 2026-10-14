plugins {
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.androidApplication) apply false
    id("org.jetbrains.kotlinx.binary-compatibility-validator") version "0.17.0"
    id("org.jetbrains.dokka") version "1.9.20" apply false
}

allprojects {
    group = providers.gradleProperty("GROUP").get()
    version = providers.gradleProperty("VERSION_NAME").get()
}
apiValidation { ignoredProjects.add("sample") }

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
        logger.lifecycle("Staged metadata, sources, documentation and font notices verified. Central release still requires owner license and verified namespace.")
    }
}

tasks.register<Zip>("prepareCentralBundle") {
    dependsOn("validateLocalPublication")
    dependsOn(":seaglass-compose:validateReleasePrerequisites", ":seaglass-haze:validateReleasePrerequisites")
    dependsOn(":seaglass-compose:publishAllPublicationsToStagingRepository", ":seaglass-haze:publishAllPublicationsToStagingRepository")
    archiveFileName.set("sea-glass-${project.version}-central.zip")
    destinationDirectory.set(layout.buildDirectory.dir("central"))
    val groupPath = project.group.toString().replace('.', '/')
    from(layout.buildDirectory.dir("staging-repository/$groupPath")) { into(groupPath) }
    exclude("**/maven-metadata.xml*")
    doFirst {
        val repo = layout.buildDirectory.dir("staging-repository").get().asFile.resolve(groupPath)
        val files = repo.walkTopDown().filter { it.isFile }.toList()
        listOf("seaglass-compose", "seaglass-haze").forEach { module ->
            listOf("iosarm64", "iosx64", "iossimulatorarm64", "android", "desktop", "wasm-js").forEach { target ->
                check(files.any { it.name == "$module-$target-${project.version}.pom" }) { "Missing $module $target publication" }
            }
        }
        files.filter { it.extension in setOf("pom", "jar", "aar", "klib", "module") }.forEach {
            check(java.io.File(it.path + ".asc").isFile) { "Unsigned artifact: $it" }
        }
        files.filter { it.extension !in setOf("md5", "sha1", "sha256", "sha512") }.forEach { artifact ->
            listOf("MD5" to "md5", "SHA-1" to "sha1").forEach { (algorithm, suffix) ->
                val digest = java.security.MessageDigest.getInstance(algorithm).digest(artifact.readBytes())
                java.io.File(artifact.path + ".$suffix").writeText(digest.joinToString("") { "%02x".format(it) })
            }
        }
    }
}
