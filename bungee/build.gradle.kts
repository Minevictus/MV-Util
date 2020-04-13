import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("net.minecrell.plugin-yml.bungee") version "0.3.0"
}

version = "0.1.0"

dependencies {
    compileOnly("io.github.waterfallmc:waterfall-api:1.15-SNAPSHOT")
    api(project(":common"))

    api("co.aikar:acf-bungee:${rootProject.ext["acfVer"]}")
}

tasks.withType<ShadowJar> {
    fun relocations(vararg pkgs: String) {
        pkgs.forEach { relocate(it, "${rootProject.group}.dependencies.$it") }
    }
    relocations(
        "co.aikar.commands",
        "co.aikar.util",
        "co.aikar.locales",
        "co.aikar.idb",
        "net.jodah.expiringmap",
        "org.intellij.lang.annotations",
        "org.jetbrains.annotations",
        "org.reactivestreams",
        "reactor",
        "io.netty",
        "io.lettuce",
        "org.slf4j",
        "com.zaxxer"
    )
    mergeServiceFiles()
}

bungee {
    name = "MV-Util"
    description = "The main core plugin for Minevictus."
    main = "us.minevict.mvutil.bungee.MinevictusUtilsBungee"
    author = "Proximyst"
}