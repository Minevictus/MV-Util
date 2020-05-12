import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

plugins {
    id("net.minecrell.plugin-yml.bungee") version "0.3.0"
}

dependencies {
    api(project(":common"))
    compileOnly(kotlin("stdlib-jdk8"))
    compileOnly("io.github.waterfallmc:waterfall-api:1.15-SNAPSHOT")

    api("co.aikar:acf-bungee:${rootProject.ext["acfVer"]}")
    api("commons-lang:commons-lang:2.6") // Spigot has this, Bungee doesn't
}

tasks.withType<ShadowJar> {
    val body = HttpClient.newHttpClient().send(
        HttpRequest.newBuilder()
            .uri(uri("https://raw.githubusercontent.com/Minevictus/MV-Util/relocations/relocations.bungee.json"))
            .build(),
        HttpResponse.BodyHandlers.ofString()
    ).body()
    val json = groovy.json.JsonSlurper().parseText(body) as Map<String, String>
    json.forEach { (k, v) -> relocate(k, v) }
    mergeServiceFiles()
}

bungee {
    name = "MV-Util"
    description = "The main core plugin for Minevictus."
    main = "us.minevict.mvutil.bungee.MinevictusUtilsBungee"
    author = "Proximyst"
}