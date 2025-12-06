plugins {
    id("java")
    id("java-library")
    id("maven-publish")
}

group = "net.stachetopia.library"
version = "1.0"

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/stachetopia/stachepackages")
            credentials {
                username = project.findProperty("gpr.applicationUser") as String?
                password = System.getenv("GITHUB_PUBLISHING_TOKEN") as String?
            }
        }
    }
    publications {
        create<MavenPublication>("gpr") {
            from(components["java"])
            artifactId = "messaging_broker_celeritas"
        }
    }
}


repositories {
    mavenCentral()
    maven {
        url = uri("https://maven.pkg.github.com/stachetopia/stachecore")
        credentials {
            username = project.findProperty("gpr.applicationUser") as String?
            password = System.getenv("GITHUB_INSTALLING_TOKEN") as String?
        }
    }
    maven {
        url = uri("https://maven.pkg.github.com/stachetopia/stachepackages")
        credentials {
            username = project.findProperty("gpr.applicationUser") as String?
            password = System.getenv("GITHUB_INSTALLING_TOKEN") as String?
        }
    }
}


dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    if (providers.gradleProperty("useLocalDeps").getOrElse("false").toBoolean()) {
        println("Using local dependencies for development")
        compileOnly(project(":stachecore"))
    } else {
        println("Using published dependencies")
        compileOnly("net.stachetopia:stachecore:1.0.4")
    }

}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<JavaCompile> {
    options.release = 21
}