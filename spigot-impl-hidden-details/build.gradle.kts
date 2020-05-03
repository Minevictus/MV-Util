dependencies {
    compileOnly("com.destroystokyo.paper:paper-api:1.15-R0.1-SNAPSHOT")
    compileOnly(project(":common"))

    compileOnly("co.aikar:acf-paper:${rootProject.ext["acfVer"]}")
}
