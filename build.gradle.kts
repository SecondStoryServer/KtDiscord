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

    implementation("com.google.code.gson:gson:2.8.0")

    val ktorVersion = "1.3.2"
    implementation("io.ktor:ktor-client-okhttp:$ktorVersion")

    testImplementation("org.slf4j:slf4j-simple:1.7.30")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}