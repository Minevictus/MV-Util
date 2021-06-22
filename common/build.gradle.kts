dependencies {
    api("co.aikar:idb-core:1.0.0-SNAPSHOT")
    api("co.aikar:acf-core:${rootProject.ext["acfVer"]}")
    api("com.zaxxer:HikariCP:4.0.3") {
        exclude("com.h2project") // H2 is unnecessary as we run SQL & Redis
    }
    api("redis.clients:jedis:3.6.0")
    api("com.moandjiezana.toml:toml4j:0.7.4") {
        exclude("com.google.code.gson")
    }
    api("de.themoep:minedown:1.7.1-SNAPSHOT") {
        isTransitive = false
    }

    api(kotlin("stdlib-jdk8"))
    api(kotlin("reflect"))

    compileOnly("net.md-5:bungeecord-chat:1.16-R0.4-SNAPSHOT")
    compileOnly("commons-lang:commons-lang:2.6") // Spigot has this, Bungee doesn't
    compileOnly("com.google.code.gson:gson:2.8.0") // Spigot has this, Bungee doesn't
}