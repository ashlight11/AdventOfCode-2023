import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.24"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
    testImplementation("io.kotest:kotest-assertions-core:5.8.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

/*tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.9.21"
}*/

application {
    mainClass.set("MainKt")
}