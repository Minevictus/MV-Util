repositories {
    maven {
        name = "github"
        url = uri("https://maven.pkg.github.com/Minevictus/toml4j")
        credentials {
            username = project.findProperty("username") as String? ?: System.getenv("GITHUB_USERNAME")
            password = project.findProperty("password") as String? ?: System.getenv("GITHUB_TOKEN")
        }
    }
}

dependencies {
    implementation("co.aikar:acf-core:${rootProject.ext["acfVer"]}")
    implementation("co.aikar:idb-core:1.0.0-SNAPSHOT")
    implementation("com.zaxxer:HikariCP:5.0.0")
    implementation("redis.clients:jedis:4.0.0")
    implementation("com.moandjiezana.toml:toml4j:0.7.4")
    implementation("de.themoep:minedown:1.7.1-SNAPSHOT")

    api(kotlin("stdlib-jdk8"))
    api(kotlin("reflect"))

    compileOnly("net.md-5:bungeecord-chat:1.16-R0.4")
    compileOnly("commons-lang:commons-lang:2.6") // Spigot has this, Bungee doesn't
    compileOnly("com.google.code.gson:gson:2.8.9") // Spigot has this, Bungee doesn't
}