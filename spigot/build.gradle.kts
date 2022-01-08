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

    api("co.aikar:acf-paper:${rootProject.ext["acfVer"]}")
    api("de.themoep:inventorygui:1.5-SNAPSHOT")
    api("io.papermc:paperlib:1.0.7")
    api("com.github.Minevictus:Skedule:v1.2.7")
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