import com.github.jengelman.gradle.plugins.shadow.ShadowPlugin
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import java.util.*

plugins {
    java
    `java-library`
    id("com.github.johnrengelman.shadow") version "5.2.0"
    `maven-publish`
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
    // Please remember to set this in MvUtilVersion.
    version = "3.6.1"
}

subprojects {
    apply {
        plugin<JavaPlugin>()
        plugin<JavaLibraryPlugin>()
        plugin<ShadowPlugin>()
        plugin<MavenPublishPlugin>()
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

    dependencies {
        compileOnly("org.jetbrains:annotations:19.0.0")
    }

    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = sourceCompatibility
    }

    java {
        withJavadocJar()
        withSourcesJar()
    }

    tasks.withType<JavaCompile> {
        options.compilerArgs.add("-parameters")
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
            create<MavenPublication>("shadow") {
                project.shadow.component(this)
                artifact(project.tasks.getByName("javadocJar"))
                artifact(project.tasks.getByName("sourcesJar"))
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
}

// Holy SHIT IntelliJ, I do not care about some agreement for Java 14, we're not using it!
configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = sourceCompatibility
}
