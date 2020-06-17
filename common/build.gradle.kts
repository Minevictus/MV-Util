dependencies {
    api("co.aikar:idb-core:1.0.0-SNAPSHOT")
    api("co.aikar:acf-core:${rootProject.ext["acfVer"]}")
    api("com.zaxxer:HikariCP:2.4.1") {
        exclude("com.h2project") // H2 is unnecessary as we run SQL & Redis
    }
    api("io.lettuce:lettuce-core:5.3.1.RELEASE")
    api("com.moandjiezana.toml:toml4j:0.7.4")

    api(kotlin("stdlib-jdk8"))
    api(kotlin("reflect"))

    compileOnly("net.md-5:bungeecord-chat:1.15-SNAPSHOT")
    compileOnly("commons-lang:commons-lang:2.6") // Spigot has this, Bungee doesn't
    compileOnly("com.google.code.gson:gson:2.8.0") // Spigot has this, Bungee doesn't
}