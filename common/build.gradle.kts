dependencies {
    api("co.aikar:idb-core:1.0.0-SNAPSHOT")
    api("co.aikar:acf-core:${rootProject.ext["acfVer"]}")
    api("com.zaxxer:HikariCP:3.4.2") {
        exclude("com.h2project") // H2 is unnecessary as we run SQL & Redis
    }
    api("io.lettuce:lettuce-core:5.2.2.RELEASE")
}