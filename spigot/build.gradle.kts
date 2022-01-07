import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import us.minevict.mvutilgradleplugin.bukkit

repositories {
    maven {
        name = "papermc-repo"
        url = uri("https://papermc.io/repo/repository/maven-public/")
    }
}

dependencies {
    api(project(":common"))
    compileOnly(kotlin("stdlib-jdk8"))
    compileOnly("io.papermc.paper:paper-api:1.18.1-R0.1-SNAPSHOT")

    implementation("co.aikar:acf-paper:${rootProject.ext["acfVer"]}")
    implementation("de.themoep:inventorygui:1.5-SNAPSHOT")
    implementation("io.papermc:paperlib:1.0.7")
    implementation("com.github.Minevictus:Skedule:v1.2.7")

    implementation("co.aikar:idb-core:1.0.0-SNAPSHOT")
    implementation("com.zaxxer:HikariCP:5.0.0")
    implementation("redis.clients:jedis:4.0.0")
    implementation("com.github.Minevictus:toml4j:v0.7.4")
    implementation("de.themoep:minedown:1.7.1-SNAPSHOT")
}

bukkit {
    name = "MV-Util"
    description = "The main core plugin for Minevictus."
    main = "us.minevict.mvutil.spigot.MinevictusUtilsSpigot"
    apiVersion = "1.18"
    authors = listOf("Proximyst", "NahuLD")
    load = BukkitPluginDescription.PluginLoadOrder.STARTUP
    softDepend = listOf("MV-NMS")
}