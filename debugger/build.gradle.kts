plugins {
    java
    application
    id("org.openjfx.javafxplugin") version "0.0.8"
}

application {
    applicationName = "Graphical Debugger"
    mainClassName = "org.wint3794.debugger.App"
}

javafx {
    modules("javafx.controls", "javafx.fxml")
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