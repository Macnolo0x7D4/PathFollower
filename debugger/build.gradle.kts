plugins {
    java
    application
    id("org.openjfx.javafxplugin") version "0.0.8"
}

group = "org.wint3794"
version = "0.5.4"

repositories {
    mavenCentral()
}

dependencies {
    testCompile("junit", "junit", "4.12")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}