plugins {
    kotlin("jvm") version "1.3.72"
    `maven-publish`
}

group = "me.syari.discord"
version = "1.0"

val mavenRepoUploadURL: String by extra
val mavenRepoUploadUser: String by extra
val mavenRepoUploadPassword: String by extra

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.google.code.gson:gson:2.8.0")
    implementation("io.ktor:ktor-client-okhttp:1.3.2")
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

val sourceJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allJava.srcDirs)
}

publishing {
    repositories {
        maven {
            url = uri(mavenRepoUploadURL)
            credentials {
                username = mavenRepoUploadUser
                password = mavenRepoUploadPassword
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            artifact(sourceJar.get())
        }
    }
}