plugins {
    kotlin("jvm")
    application
}

application {
    applicationName = "PathFollower Debugger"
    mainClassName = "org.wint3794.ftc.pathfollower.debugger.Main"
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
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}