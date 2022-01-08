import nl.javadude.gradle.plugins.license.LicensePlugin
import us.minevict.mvutilgradleplugin.MvUtilPlugin
import java.util.*
import org.gradle.api.publish.maven.MavenPublication

plugins {
    id("us.minevict.mvutil") version "0.4.0"
    id("com.github.hierynomus.license") version "0.15.0"
    id("org.gradle.maven-publish")
}

ext["acfVer"] = "0.5.0-SNAPSHOT"

allprojects {
    group = "us.minevict.mvutil"
    version = "6.3.2"
}

subprojects {
    apply {
        plugin<MvUtilPlugin>()
        plugin<LicensePlugin>()
    }

    license {
        header = rootProject.file("LICENCE-HEADER")
        ext["year"] = Calendar.getInstance().get(Calendar.YEAR)
        ext["name"] = "Mariell Hoversholm, Nahuel Dolores"
        include("**/*.kt")
    }

    afterEvaluate {
        publishing {
            publications {
                val mavenJava by creating(MavenPublication::class) {
                    from(components["java"])
                }
            }
        }
    }
}