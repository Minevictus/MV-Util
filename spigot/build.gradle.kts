import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
    id("net.minecrell.plugin-yml.bukkit") version "0.3.0"
}

version = "0.2.1"

dependencies {
    compileOnly("com.destroystokyo.paper:paper-api:1.15-R0.1-SNAPSHOT")
    compileOnly("com.proximyst:mv-nms:0.+")
    api(project(":common"))

    api("co.aikar:taskchain-bukkit:3.7.2")
    api("co.aikar:acf-paper:${rootProject.ext["acfVer"]}")
    api("de.themoep:inventorygui:1.4.1-SNAPSHOT") {
        exclude("org.bukkit")
    }
}

tasks.withType<ShadowJar> {
    fun relocations(vararg pkgs: String) {
        pkgs.forEach { relocate(it, "${rootProject.group}.dependencies.$it") }
    }
    relocations(
        "co.aikar.commands",
        "co.aikar.taskchain",
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
        "com.zaxxer",
        "de.themoep.inventorygui"
    )
    mergeServiceFiles()
}

bukkit {
    name = "MV-Util"
    description = "The main core plugin for Minevictus."
    main = "us.minevict.mvutil.spigot.MinevictusUtilsSpigot"
    apiVersion = "1.15"
    authors = listOf("Proximyst")
    load = BukkitPluginDescription.PluginLoadOrder.STARTUP
    softDepend = listOf("MV-NMS")
}