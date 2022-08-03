package de.codecentric.kafka.playground.consumer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KafkaPlaygroundConsumerApplication

    fun main(args: Array<String>) {
        runApplication<KafkaPlaygroundConsumerApplication>(*args)
    }
