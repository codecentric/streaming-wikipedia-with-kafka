package de.codecentric.kafka.playground.producer

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.UUID

@Component
class WikipediaEventProducer (@Autowired val kafkaTemplate: KafkaTemplate<String, String>, @Value("\${topic.name.producer}") val topicName: String){

    // @Scheduled(fixedDelay = 10000)
    fun send(message: String) {
        val key: String = UUID.randomUUID().toString();
        kafkaTemplate.send(topicName, key, message);
    }
}
