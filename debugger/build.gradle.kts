plugins {
    kotlin("jvm")
    application
    id("org.openjfx.javafxplugin") version "0.0.9"
}

application {
    applicationName = "PathFollower Debugger"
    mainClassName = "org.wint3794.ftc.pathfollower.debugger.Main"
}

javafx {
    version = "15.0.1"
    modules = listOf("javafx.controls")
}

group = "org.wint3794"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "11"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "11"
    }
}
