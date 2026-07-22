import org.jetbrains.changelog.Changelog
import org.jetbrains.changelog.markdownToHTML
import org.jetbrains.intellij.platform.gradle.TestFrameworkType
import org.jetbrains.intellij.platform.gradle.tasks.VerifyPluginTask.FailureLevel

plugins {
    id("java") // Java support
    alias(libs.plugins.kotlin) // Kotlin support
    alias(libs.plugins.intelliJPlatform) // IntelliJ Platform Gradle Plugin
    alias(libs.plugins.changelog) // Gradle Changelog Plugin
}

group = providers.gradleProperty("pluginGroup").get()
val pluginVersionProvider = providers.environmentVariable("LD_VERSION").orElse(providers.gradleProperty("pluginVersion"))
version = pluginVersionProvider.get()

// IntelliJ Platform 2026.2 runs on Java 25.
kotlin {
    jvmToolchain(25)
}

// Configure project's dependencies
repositories {
    mavenLocal()
    maven("https://jitpack.io")
    mavenCentral()

    // IntelliJ Platform Gradle Plugin Repositories Extension - read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin-repositories-extension.html
    intellijPlatform {
        defaultRepositories()
    }
}

// Dependencies are managed with Gradle version catalog - read more: https://docs.gradle.org/current/userguide/platforms.html#sub:version-catalog
dependencies {
    api("com.alibaba:fastjson:1.2.83")
    api("org.jsoup:jsoup:1.21.1")
    api("org.apache.commons:commons-lang3:3.20.0")
    // Bundle Velocity explicitly instead of borrowing the IDE's copy; logging goes through the platform slf4j.
    api("org.apache.velocity:velocity-engine-core:2.4.1") {
        exclude(group = "org.slf4j")
    }
    api("com.vladsch.flexmark:flexmark:0.62.2")
    api("com.vladsch.flexmark:flexmark-ext-attributes:0.62.2")
    api("io.github.biezhi:TinyPinyin:2.0.3.RELEASE")
    // api(fileTree(mapOf("dir" to "src/main/resources/lib", "include" to listOf("*.jar"))))

    testImplementation(libs.junit)
    testImplementation(libs.opentest4j)

    // IntelliJ Platform Gradle Plugin Dependencies Extension - read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin-dependencies-extension.html
    intellijPlatform {
        val localIdePath = providers.gradleProperty("localIdePath")
        if (localIdePath.isPresent) {
            local(localIdePath)
        } else {
            intellijIdea(providers.gradleProperty("platformVersion"))
        }
        bundledPlugin("com.intellij.modules.jcef")

        // Plugin Dependencies. Uses `platformBundledPlugins` property from the gradle.properties file for bundled IntelliJ Platform plugins.
        //bundledPlugins(providers.gradleProperty("platformBundledPlugins").map { it.split(',') })

        // Plugin Dependencies. Uses `platformPlugins` property from the gradle.properties file for plugin from JetBrains Marketplace.
        //plugins(providers.gradleProperty("platformPlugins").map { it.split(',') })

        // Module Dependencies. Uses `platformBundledModules` property from the gradle.properties file for bundled IntelliJ Platform modules.
        //bundledModules(providers.gradleProperty("platformBundledModules").map { it.split(',') })

        testFramework(TestFrameworkType.Platform)
    }
}

// Configure IntelliJ Platform Gradle Plugin - read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin-extension.html
intellijPlatform {
    pluginConfiguration {
        name = providers.gradleProperty("pluginDisplayName")
        version = project.version.toString()

        // Extract the <!-- Plugin description --> section from README.md and provide for the plugin's manifest
        description = providers.fileContents(layout.projectDirectory.file(providers.gradleProperty("pluginDescription").get())).asText.get()

        val changelog = project.changelog // local variable for configuration cache compatibility
        // Get the latest available change notes from the changelog file
        changeNotes = with(changelog) {
            renderItem(
                (getOrNull(project.version.toString()) ?: getUnreleased())
                    .withHeader(false)
                    .withEmptySections(false),
                Changelog.OutputType.HTML,
            )
        }

        ideaVersion {
            sinceBuild = providers.gradleProperty("pluginSinceBuild")
            // No upper bound: the Gradle plugin would otherwise default until-build to "<since>.*".
            untilBuild = provider { null }
        }
    }

    publishing {
        token = providers.environmentVariable("PUBLISH_TOKEN")
        // The pluginVersion is based on the SemVer (https://semver.org) and supports pre-release labels, like 2.1.7-alpha.3
        // Specify pre-release label to publish the plugin in a custom Release Channel automatically. Read more:
        // https://plugins.jetbrains.com/docs/intellij/deployment.html#specifying-a-release-channel
        channels = pluginVersionProvider.map { listOf(it.substringAfter('-', "").substringBefore('.').ifEmpty { "default" }) }
    }

    pluginVerification {
        // Gate releases on real compatibility breakage only; internal/override-only API usages
        // inherited from upstream are still reported in the verifier output but do not fail the build.
        failureLevel = listOf(
            FailureLevel.COMPATIBILITY_PROBLEMS,
            FailureLevel.INVALID_PLUGIN,
        )
        ides {
            recommended()
        }
    }
}

// Configure Gradle Changelog Plugin - read more: https://github.com/JetBrains/gradle-changelog-plugin
changelog {
    groups.empty()
    repositoryUrl = providers.gradleProperty("pluginRepositoryUrl")
}


tasks {
    processResources {
        from(rootProject.file("LICENSE")) {
            into("META-INF")
        }
        from(rootProject.file("NOTICE")) {
            into("META-INF")
        }
    }

    withType<JavaCompile>().configureEach {
        options.compilerArgs.addAll(listOf("-Xlint:deprecation", "-Xlint:unchecked"))
    }

    wrapper {
        gradleVersion = providers.gradleProperty("gradleVersion").get()
    }
}

intellijPlatformTesting {
    runIde {
        register("runIdeForUiTests") {
            task {
                jvmArgumentProviders += CommandLineArgumentProvider {
                    listOf(
                        "-Dfile.encoding=utf-8"
                    )
                }
            }

            plugins {
                robotServerPlugin()
            }
        }
    }
}
