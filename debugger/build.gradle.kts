plugins {
    kotlin("jvm")
    application
    id("org.openjfx.javafxplugin") version "0.0.8"
}

group = "org.wint3794"
version = "0.6.0"

application {
    applicationName = "Graphical Debugger"
    mainClassName = "org.wint3794.pathfollower.debugger.Main"
}

javafx {
    modules("javafx.controls", "javafx.fxml")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}