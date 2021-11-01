val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val exposed_version: String ="0.27.1"
val h2_version: String by project
plugins {
    application
    kotlin("jvm") version "1.5.31"
                id("org.jetbrains.kotlin.plugin.serialization") version "1.5.31"
}

group = "com.example"
version = "0.0.1"
application {
    mainClass.set("com.example.ApplicationKt")
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-serialization:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlin_version")

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")

    implementation("org.jetbrains.exposed:exposed-core:$0.36.1")
    implementation ("org.jetbrains.exposed:exposed-jdbc:0.36.1")
    implementation ("org.jetbrains.exposed:exposed-dao:0.36.1")

    implementation ("com.h2database:h2:$h2_version")
    implementation ("com.zaxxer:HikariCP:3.4.2")

}