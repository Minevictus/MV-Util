import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

plugins {
    id("net.minecrell.plugin-yml.bukkit") version "0.3.0"
}

dependencies {
    compileOnly(kotlin("stdlib-jdk8"))

    compileOnly("com.destroystokyo.paper:paper-api:1.15-R0.1-SNAPSHOT")
    compileOnly("com.proximyst:mv-nms:0.+")
    api(project(":common"))

    api("co.aikar:taskchain-bukkit:3.7.2")
    api("co.aikar:acf-paper:${rootProject.ext["acfVer"]}")
    api("de.themoep:inventorygui:1.4.1-SNAPSHOT") {
        exclude("org.bukkit")
    }
    api("me.tom.sparse:ChatMenuAPI:1.1.1")
    api("io.papermc:paperlib:1.0.2")
    api("com.okkero.skedule:skedule:1.2.6")
}

tasks.withType<ShadowJar> {
    val body = HttpClient.newHttpClient().send(
        HttpRequest.newBuilder()
            .uri(uri("https://raw.githubusercontent.com/Minevictus/MV-Util/relocations/relocations.spigot.json"))
            .build(),
        HttpResponse.BodyHandlers.ofString()
    ).body()
    val json = groovy.json.JsonSlurper().parseText(body) as Map<String, String>
    json.forEach { (k, v) -> relocate(k, v) }
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