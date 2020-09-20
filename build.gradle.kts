plugins {
    kotlin("jvm") version "1.3.72"
    id("com.jfrog.bintray") version "1.7.3"
    `maven-publish`
    `java-library`
}

group = "org.wint3794"
version = "0.6.2"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    testImplementation("junit:junit:4.12")

    api("org.apache.commons:commons-math3:3.6.1")
    implementation("com.google.guava:guava:29.0-jre")
    implementation("com.googlecode.json-simple:json-simple:1.1")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

tasks.jar {
    manifest {
        attributes["Implementation-Title"] = project.name
        attributes["Implementation-Version"] = project.version
    }
}

bintray {
    user = System.getenv("BINTRAY_USER")
    key = System.getenv("BINTRAY_API_KEY")
    pkg.apply {
        repo = "PathFollower"
        name = "PathFollower"
        userOrg = "wint3794"
        setLicenses("Apache-2.0")
        vcsUrl = "https://github.com/WinT-3794/PathFollower"
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}
