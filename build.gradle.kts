plugins {
    kotlin("jvm") version "2.4.0"
}

group = "net.ixct.commons"
version = "1.0.0-alpha.1+1"

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
