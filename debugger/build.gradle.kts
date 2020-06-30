import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    application
    id("org.openjfx.javafxplugin") version "0.0.8"
    kotlin("jvm") version "1.3.72"
}

application {
    applicationName = "Graphical Debugger"
    mainClassName = "org.wint3794.debugger.Main"
}

javafx {
    modules("javafx.controls", "javafx.fxml")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "org.wint3794.debugger.Main"
    }
}

repositories {
    mavenCentral()
}

dependencies {
    testCompile("junit", "junit", "4.12")
    implementation(kotlin("stdlib-jdk8"))
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}