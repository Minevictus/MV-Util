import us.minevict.mvutilgradleplugin.bungee

dependencies {
    api(project(":common"))
    compileOnly(kotlin("stdlib-jdk8"))
    compileOnly("io.github.waterfallmc:waterfall-api:1.18-R0.1-SNAPSHOT")

    api("co.aikar:acf-bungee:${rootProject.ext["acfVer"]}")
    api("commons-lang:commons-lang:2.6") // Spigot has this, Bungee doesn't
    api("com.google.code.gson:gson:2.8.9") // Spigot has this, Bungee doesn't

    implementation("co.aikar:idb-core:1.0.0-SNAPSHOT")
    implementation("com.zaxxer:HikariCP:5.0.0")
    implementation("redis.clients:jedis:4.0.0")
    implementation("com.github.Minevictus:toml4j:v0.7.4")
    implementation("de.themoep:minedown:1.7.1-SNAPSHOT")
}

bungee {
    name = "MV-Util"
    description = "The main core plugin for Minevictus."
    main = "us.minevict.mvutil.bungee.MinevictusUtilsBungee"
    author = "Proximyst & NahuLD"
}