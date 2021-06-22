import nl.javadude.gradle.plugins.license.LicensePlugin
import us.minevict.mvutilgradleplugin.MvUtilPlugin
import java.util.*

plugins {
    id("us.minevict.mvutil") version "0.2.5"
    id("com.github.hierynomus.license") version "0.15.0"
}

ext["acfVer"] = "0.5.0-SNAPSHOT"

allprojects {
    group = "us.minevict.mvutil"
    version = "6.2.0"
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
}