import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
    java
    id("com.github.johnrengelman.shadow") version "2.0.4"
    kotlin("jvm") version "1.6.21"
}

repositories {
    mavenLocal()
    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
}

dependencies {
    implementation("io.github.microutils:kotlin-logging-jvm:2.0.11")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
    implementation("org.apache.kafka:connect-api:3.2.1")
    implementation("com.launchdarkly:okhttp-eventsource:2.7.0")
}

group = "com.acme.kafka.connect"
version = "0.1.0"
java.sourceCompatibility = JavaVersion.VERSION_11

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<ShadowJar> {
    archiveFileName.set("app.jar")
    exclude("org.apache.kafka:connect-api:3.2.1")
}
