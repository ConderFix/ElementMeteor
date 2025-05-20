plugins {
    kotlin("jvm") version "2.1.21-RC2"
    id("com.gradleup.shadow") version "8.3.0"
    id("xyz.jpenilla.run-paper") version "2.3.1"
}

group = "ru"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc-repo"
    }
    maven("https://oss.sonatype.org/content/groups/public/") {
        name = "sonatype"
    }
    maven("https://repo.panda-lang.org/releases")
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("com.destroystokyo.paper:paper-api:1.16.5-R0.1-SNAPSHOT")
    implementation("dev.rollczi:litecommands-bukkit:3.9.7")
    implementation("fr.mrmicky:FastInv:3.1.1")
    implementation("com.zaxxer:HikariCP:5.1.0")
    implementation("org.mariadb.jdbc:mariadb-java-client:3.5.3")
    compileOnly("com.projectkorra:projectkorra:1.12.0")
    compileOnly("org.projectlombok:lombok:1.18.34")
}

tasks {
    runServer {
        // Configure the Minecraft version for our task.
        // This is the only required configuration besides applying the plugin.
        // Your plugin's jar (or shadowJar if present) will be used automatically.
        minecraftVersion("1.16")
    }
}

val targetJavaVersion = 16
kotlin {
    jvmToolchain(targetJavaVersion)
}

tasks.build {
    dependsOn("shadowJar")
}

tasks.processResources {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand(props)
    }
}
