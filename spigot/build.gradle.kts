import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import us.minevict.mvutilgradleplugin.bukkit
import us.minevict.mvutilgradleplugin.paperApi

dependencies {
    compileOnly(kotlin("stdlib-jdk8"))

    compileOnly(paperApi("1.15.2"))
    compileOnly("com.proximyst:mv-nms:0.+")
    api(project(":common"))

    api("co.aikar:acf-paper:${rootProject.ext["acfVer"]}")
    api("de.themoep:inventorygui:1.4.2-SNAPSHOT") {
        exclude("org.bukkit")
    }
    api("me.tom.sparse:ChatMenuAPI:1.1.1")
    api("io.papermc:paperlib:1.0.3")
    api("com.okkero.skedule:skedule:1.2.6")
}

bukkit {
    name = "MV-Util"
    description = "The main core plugin for Minevictus."
    main = "us.minevict.mvutil.spigot.MinevictusUtilsSpigot"
    apiVersion = "1.15"
    authors = listOf("Proximyst", "NahuLD")
    load = BukkitPluginDescription.PluginLoadOrder.STARTUP
    softDepend = listOf("MV-NMS")
}