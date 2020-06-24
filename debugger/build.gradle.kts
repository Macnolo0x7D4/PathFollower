plugins {
    java
    application
    id("org.openjfx.javafxplugin") version "0.0.8"
    id("org.beryx.jlink") version "2.12.0"
}

application {
    applicationName = "Graphical Debugger"
    mainClassName = "org.wint3794.debugger.App"
}

javafx {
    modules("javafx.controls", "javafx.fxml")
}

jlink {
    launcher {
        name = "Graphical Debugger"
    }
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "org.wint3794.debugger.App"
    }
}

repositories {
    mavenCentral()
}

dependencies {
    testCompile("junit", "junit", "4.12")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}