package de.codecentric.kafka.playground.consumer.wiki

import de.codecentric.kafka.playground.consumer.wiki.types.WikipediaEvent
import mu.KotlinLogging
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class WikipediaEventConsumer {

    private val LOG = KotlinLogging.logger {}

    @KafkaListener(topics = ["\${topic.name.consumer}"], groupId = "\${spring.kafka.consumer.group-id}" )
    fun consume(payload: ConsumerRecord<String, WikipediaEvent>) {
        LOG.info { "Received message with key ${payload.key()}"};
        LOG.info { "Received message with payload ${payload.value()}"};
    }
}
