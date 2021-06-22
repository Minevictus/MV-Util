import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import us.minevict.mvutilgradleplugin.bukkit
import us.minevict.mvutilgradleplugin.paperApi

dependencies {
    compileOnly(kotlin("stdlib-jdk8"))

    compileOnly(paperApi("1.16.5"))
    compileOnly("com.proximyst:mv-nms:0.+")
    api(project(":common"))

    api("co.aikar:acf-paper:${rootProject.ext["acfVer"]}")
    api("de.themoep:inventorygui:1.4.2-SNAPSHOT") {
        exclude("org.bukkit")
    }
    api("me.tom.sparse:ChatMenuAPI:1.1.2")
    api("io.papermc:paperlib:1.0.6")
    api("com.okkero.skedule:skedule:1.2.6")
}

bukkit {
    name = "MV-Util"
    description = "The main core plugin for Minevictus."
    main = "us.minevict.mvutil.spigot.MinevictusUtilsSpigot"
    apiVersion = "1.16"
    authors = listOf("Proximyst", "NahuLD")
    load = BukkitPluginDescription.PluginLoadOrder.STARTUP
    softDepend = listOf("MV-NMS")
}