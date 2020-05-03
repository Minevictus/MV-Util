dependencies {
    compileOnly("io.github.waterfallmc:waterfall-api:1.15-SNAPSHOT")
    compileOnly(project(":common"))

    compileOnly("co.aikar:acf-bungee:${rootProject.ext["acfVer"]}")
}
