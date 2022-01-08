dependencies {
    api("co.aikar:acf-core:${rootProject.ext["acfVer"]}")
    api("co.aikar:idb-core:1.0.0-SNAPSHOT")
    api("com.zaxxer:HikariCP:5.0.0")
    api("redis.clients:jedis:4.0.0")
    api("com.github.Minevictus:toml4j:v0.7.4")
    api("de.themoep:minedown:1.7.1-SNAPSHOT")

    api(kotlin("stdlib-jdk8"))
    api(kotlin("reflect"))

    compileOnly("net.md-5:bungeecord-chat:1.16-R0.4")
    compileOnly("commons-lang:commons-lang:2.6") // Spigot has this, Bungee doesn't
    compileOnly("com.google.code.gson:gson:2.8.9") // Spigot has this, Bungee doesn't
}