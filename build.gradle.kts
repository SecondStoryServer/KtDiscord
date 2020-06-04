plugins {
    kotlin("jvm") version "1.3.72"
}

group = "me.syari.discord"
version = "1.0"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    val ktorVersion = "1.3.2"
    implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}