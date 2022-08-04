package de.codecentric.kafka.playground.producer.wiki

import de.codecentric.kafka.playground.producer.wiki.types.WikipediaEvent
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class WikipediaEventProducer (@Autowired val kafkaTemplate: KafkaTemplate<String, WikipediaEvent>,
                              @Value("\${topic.name.producer}") val topicName: String){

    val log = KotlinLogging.logger {}

    fun send(message: WikipediaEvent) {
        val key = message.meta.uri;
        log.info { "Sending event due to edit of $key" }
        kafkaTemplate.send(topicName, key, message);
    }
}
