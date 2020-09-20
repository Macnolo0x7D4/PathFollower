plugins {
    kotlin("jvm") version "1.3.72"
    id("com.jfrog.bintray") version "1.8.4"
    `maven-publish`
    `java-library`
}

group = "org.wint3794.ftc"
version = "0.6.2"

val artifactName = project.name
val artifactGroup = project.group.toString()
val artifactVersion = project.version.toString()

val pomUrl = "https://github.com/WinT-3794/PathFollower"
val pomIssueUrl = "$pomUrl/issues"

val githubRepo = "WinT-3794/PathFollower"
val githubReadme = "README.md"

val pomDeveloperId = "mcn2004"
val pomDeveloperName = "Manuel Diaz"

val pomLicenseName = "Apache-2.0"
val pomLicenseUrl = "https://www.apache.org/licenses/LICENSE-2.0"
val pomLicenseDist = rootProject.name

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

bintray {
    user = System.getenv("BINTRAY_USER") //project.findProperty("bintrayUser").toString()
    key = System.getenv("BINTRAY_API_KEY") // project.findProperty("bintrayKey").toString()
    publish = true

    setPublications("lib")

    pkg.apply {
        repo = "ftc"
        name = artifactName
        userOrg = "wint3794"
        githubRepo = githubRepo
        vcsUrl = pomUrl
        description = "The easiest way to create your robot program"
        setLabels("kotlin", "ftc", "robot", "first", "path", "algorithm")
        setLicenses("Apache-2.0")
        desc = description
        websiteUrl = pomUrl
        issueTrackerUrl = pomIssueUrl
        githubReleaseNotesFile = githubReadme

        version.apply {
            name = artifactVersion
            desc = pomUrl
            vcsTag = "v$artifactVersion"
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("lib") {
            groupId = artifactGroup
            artifactId = artifactName
            version = artifactVersion
            from(components["java"])

            pom.withXml {
                asNode().apply {
                    appendNode("description", pomUrl)
                    appendNode("name", rootProject.name)
                    appendNode("url", pomUrl)
                    appendNode("licenses").appendNode("license").apply {
                        appendNode("name", pomLicenseName)
                        appendNode("url", pomLicenseUrl)
                        appendNode("distribution", pomLicenseDist)
                    }
                    appendNode("developers").appendNode("developer").apply {
                        appendNode("id", pomDeveloperId)
                        appendNode("name", pomDeveloperName)
                    }
                    appendNode("scm").apply {
                        appendNode("url", pomUrl)
                    }
                }
            }
        }
    }
}