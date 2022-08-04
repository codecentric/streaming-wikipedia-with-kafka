package de.codecentric.kafka.playground.producer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KafkaPlaygroundProducerApplication
    fun main(args: Array<String>) {
        runApplication<KafkaPlaygroundProducerApplication>(*args)
    }
