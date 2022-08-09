import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    id("com.github.johnrengelman.shadow") version "2.0.4"
}

repositories {
    mavenLocal()
    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
}

dependencies {
    implementation("org.slf4j:slf4j-api:1.7.36")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
    implementation("org.apache.kafka:connect-api:3.2.1")
    implementation("com.launchdarkly:okhttp-eventsource:2.7.0")
}

group = "com.acme.kafka.connect"
version = "0.1.0"
java.sourceCompatibility = JavaVersion.VERSION_11

tasks.withType<ShadowJar> {
    archiveFileName.set("app.jar")
    exclude("org.apache.kafka:connect-api:3.2.1")
    exclude("org.slf4j:slf4j-api:1.7.36")
}
