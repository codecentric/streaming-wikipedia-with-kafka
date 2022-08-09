import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
//    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("com.github.johnrengelman.shadow") version "2.0.4"
}

repositories {
    mavenLocal()
    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
}

dependencies {
    implementation("org.apache.cxf:cxf-rt-rs-client:3.5.3")
    implementation("org.apache.cxf:cxf-rt-rs-sse:3.5.3")
    implementation("org.slf4j:slf4j-api:1.7.36")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
    implementation("org.apache.kafka:connect-api:3.2.1")

}

group = "com.acme.kafka.connect"
version = "0.1.0"
java.sourceCompatibility = JavaVersion.VERSION_11

tasks.withType<ShadowJar> {
    archiveFileName.set("app.jar")
    exclude("org.apache.kafka:connect-api:3.2.1")
    exclude("org.slf4j:slf4j-api:1.7.36")
}
//shadowJar() {
//    dependsOn clean
//        dependencies {
//            // exclude dependencies provided in the kafka connect classpath
//            exclude dependency("org.apache.kafka:connect-api:$kafka_version")
//            exclude dependency("org.apache.kafka:kafka-clients:$kafka_version")
//            exclude dependency('net.jpountz.lz4:.*:.*')
//            exclude dependency('org.xerial.snappy:.*:.*')
//            exclude dependency('org.slf4j:.*:.*')
//
//            // the stream module shouldn't be packaged up with connector
//            exclude(project(':stream'))
//        }
//}
