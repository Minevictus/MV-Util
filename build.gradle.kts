import com.github.jengelman.gradle.plugins.shadow.ShadowPlugin
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.dokka.gradle.DokkaPlugin
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.*

plugins {
    java
    `java-library`
    id("com.github.johnrengelman.shadow") version "5.2.0"
    `maven-publish`
    kotlin("jvm") version "1.3.72"
    id("org.jetbrains.dokka") version "0.10.1"
}

run {
    val props = Properties()
    rootDir.listFiles { file -> file.extension == "properties" && file.nameWithoutExtension != "gradle" }
        ?.forEach {
            println("Loading ${it.name}...")
            it.inputStream().use {
                props.load(it)
            }
        }
    props.forEach {
        project.ext[it.key.toString()] = it.value
    }
}

ext["acfVer"] = "0.5.0-SNAPSHOT"

allprojects {
    group = "us.minevict.mvutil"
    version = "5.0.1"
}

repositories {
    maven {
        name = "kotlin-dev"
        url = uri("https://dl.bintray.com/kotlin/kotlin-dev/")
    }

    jcenter()
    mavenCentral()
}

subprojects {
    apply {
        plugin<JavaPlugin>()
        plugin<JavaLibraryPlugin>()
        plugin<ShadowPlugin>()
        plugin<MavenPublishPlugin>()
//        plugin("org.jetbrains.kotlin.multiplatform") // Why. Can. I. Not. Do. This. Via. Class. Referencing.
        plugin("org.jetbrains.kotlin.jvm")
        plugin<DokkaPlugin>()
    }

    repositories {
        maven {
            name = "spigotmc"
            url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")

            content {
                includeGroup("org.bukkit")
                includeGroup("org.spigotmc")
            }
        }

        maven {
            name = "sonatype"
            url = uri("https://oss.sonatype.org/content/repositories/snapshots")

            content {
                includeGroup("net.md-5")
            }
        }

        maven {
            name = "papermc-snapshots"
            url = uri("https://papermc.io/repo/repository/maven-snapshots/")

            content {
                includeGroup("com.destroystokyo.paper")
                includeGroup("io.github.waterfallmc")
                includeGroup("io.papermc")
            }
        }

        maven {
            name = "papermc"
            url = uri("https://papermc.io/repo/repository/maven-public/")

            content {
                includeGroup("com.destroystokyo.paper")
                includeGroup("io.github.waterfallmc")
                includeGroup("io.papermc")
            }
        }

        maven {
            name = "aikar-repo"
            url = uri("https://repo.aikar.co/content/groups/aikar/")

            content {
                includeGroup("co.aikar")
            }
        }

        maven {
            name = "minebench"
            url = uri("https://repo.minebench.de/")

            content {
                includeGroup("de.themoep")
            }
        }

        maven {
            name = "okkero"
            url = uri("http://nexus.okkero.com/repository/maven-releases")

            content {
                includeGroup("com.okkero.skedule")
            }
        }

        maven {
            name = "proxi-nexus"
            url = uri("https://nexus.proximyst.com/repository/maven-public/")
        }

        maven {
            name = "bintray-chatmenuapi"
            url = uri("https://dl.bintray.com/nahuld/minevictus/")
        }

        jcenter()
        mavenCentral()
    }

    val dokka by tasks.getting(DokkaTask::class) {
        outputDirectory = "$buildDir/dokka"
        outputFormat = "html"
    }

    val dokkaJar by tasks.creating(Jar::class) {
        group = JavaBasePlugin.DOCUMENTATION_GROUP
        description = "Assembles Kotlin docs with Dokka"
        archiveClassifier.set("javadoc")
        dependsOn(dokka)
        from("$buildDir/dokka")
    }

    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = sourceCompatibility
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "11"
            javaParameters = true
        }
    }

    tasks.withType<ShadowJar> {
        this.archiveClassifier.set(null as String?)

        archiveBaseName.set(
            if (this@subprojects == rootProject) {
                project.name
            } else {
                "${rootProject.name}-${project.name}"
            }
        )

        this.destinationDirectory.set(rootProject.tasks.shadowJar.get().destinationDirectory.get())
    }

    publishing {
        publications {
            create<MavenPublication>("maven") {
                from(components["java"])
                artifact(dokkaJar)
            }
        }
        repositories {
            maven {
                name = "proxi-nexus"
                url = uri("https://nexus.proximyst.com/repository/maven-any/")
                credentials {
                    val proxiUser: String? by project
                    val proxiPassword: String? by project
                    username = proxiUser
                    password = proxiPassword
                }
            }
        }
    }

    tasks.withType<Jar> {
        manifest {
            attributes(
                "Implementation-Version" to project.version.toString()
            )
        }
    }
}

// Holy SHIT IntelliJ, I do not care about some agreement for Java 14, we're not using it!
configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = sourceCompatibility
}
