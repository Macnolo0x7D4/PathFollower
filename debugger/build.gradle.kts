plugins {
    kotlin("jvm")
    application
<<<<<<< 190e36fabcb657062ec442b74e6d96a2972e0590
    id("org.openjfx.javafxplugin") version "0.0.8"
}

group = "org.wint3794"
version = "0.6.1"

application {
    applicationName = "Graphical Debugger"
    mainClassName = "org.wint3794.pathfollower.debugger.Main"
}

javafx {
    modules("javafx.controls", "javafx.fxml")
}
=======
}

application {
    applicationName = "PathFollower Debugger"
    mainClassName = "org.wint3794.pathfollower.debugger.Main"
}

group = "org.wint3794"
version = "1.0.0"
>>>>>>> Adapting JVM 1.8 for Android Support

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