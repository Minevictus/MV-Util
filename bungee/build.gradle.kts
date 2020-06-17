import us.minevict.mvutilgradleplugin.bungee
import us.minevict.mvutilgradleplugin.waterfallApi

dependencies {
    api(project(":common"))
    compileOnly(kotlin("stdlib-jdk8"))
    compileOnly(waterfallApi("1.15"))

    api("co.aikar:acf-bungee:${rootProject.ext["acfVer"]}")
    api("commons-lang:commons-lang:2.6") // Spigot has this, Bungee doesn't
    api("com.google.code.gson:gson:2.8.0") // Spigot has this, Bungee doesn't
}

bungee {
    name = "MV-Util"
    description = "The main core plugin for Minevictus."
    main = "us.minevict.mvutil.bungee.MinevictusUtilsBungee"
    author = "Proximyst & NahuLD"
}