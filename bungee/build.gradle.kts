import us.minevict.mvutilgradleplugin.bungee

dependencies {
    api(project(":common"))
    compileOnly(kotlin("stdlib-jdk8"))
    compileOnly("io.github.waterfallmc:waterfall-api:1.18-R0.1-SNAPSHOT")

    api("co.aikar:acf-bungee:${rootProject.ext["acfVer"]}")
    api("commons-lang:commons-lang:2.6") // Spigot has this, Bungee doesn't
    api("com.google.code.gson:gson:2.8.9") // Spigot has this, Bungee doesn't
}

bungee {
    name = "MV-Util"
    description = "The main core plugin for Minevictus."
    main = "us.minevict.mvutil.bungee.MinevictusUtilsBungee"
    author = "Proximyst & NahuLD"
}