package de.codecentric.kafka.playground.streams

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KafkaPlaygroundStreamsApplication

    fun main(args: Array<String>) {
        runApplication<KafkaPlaygroundStreamsApplication>(*args)
    }
