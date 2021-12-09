plugins {
    id("java")
}

version = "1.0-SNAPSHOT"

dependencies {
    implementation("io.netty:netty-all:4.1.70.Final")

    // https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-core
    implementation("com.fasterxml.jackson.core:jackson-core:2.13.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.0")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.13.0")

    // https://mvnrepository.com/artifact/org.xerial/sqlite-jdbc
    implementation("org.xerial:sqlite-jdbc:3.36.0.3")
}

repositories {
    mavenCentral()
}