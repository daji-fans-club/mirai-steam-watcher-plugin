plugins {
    val kotlinVersion = "1.6.10"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion

    id("net.mamoe.mirai-console") version "2.13.2"
}

group = "org.example"
version = "0.1.0"

repositories {
    if (System.getenv("CI")?.toBoolean() != true) {
        maven("https://maven.aliyun.com/repository/public") // 阿里云国内代理仓库
    }
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
    implementation("io.ktor:ktor-client-serialization:2.2.4")
    implementation("io.ktor:ktor-client-okhttp:2.2.4")
    testImplementation(kotlin("test"))
}
