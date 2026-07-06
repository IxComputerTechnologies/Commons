plugins {
    kotlin("jvm") version "2.4.0"
    `maven-publish`
}

group = "net.ixct.commons"
version = "1.0.0-alpha.1-5"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(25)
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["kotlin"])
            artifactId = "Commons"
        }
    }
}
