dependencies {
    implementation("co.aikar:acf-core:${rootProject.ext["acfVer"]}")
    implementation("co.aikar:idb-core:1.0.0-SNAPSHOT")
    implementation("com.zaxxer:HikariCP:5.0.0")
    implementation("redis.clients:jedis:4.0.0")
    implementation("com.github.Minevictus:toml4j:v0.7.4")
    implementation("de.themoep:minedown:1.7.1-SNAPSHOT")

    api(kotlin("stdlib-jdk8"))
    api(kotlin("reflect"))

    compileOnly("net.md-5:bungeecord-chat:1.16-R0.4")
    compileOnly("commons-lang:commons-lang:2.6") // Spigot has this, Bungee doesn't
    compileOnly("com.google.code.gson:gson:2.8.9") // Spigot has this, Bungee doesn't
}