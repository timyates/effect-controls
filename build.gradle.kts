import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.22"
    application
    id("org.openjfx.javafxplugin") version "0.0.14"
}

group = "com.bloidonia"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

application {
    mainClass.set("com.bloidonia.Main")
}

javafx {
    modules("javafx.controls")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("eu.hansolo:tilesfx:17.1.17")
}
