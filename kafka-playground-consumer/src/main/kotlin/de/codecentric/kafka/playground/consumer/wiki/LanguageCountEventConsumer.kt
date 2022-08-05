package de.codecentric.kafka.playground.consumer.wiki

import mu.KotlinLogging
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class LanguageCountEventConsumer {

    private val LOG = KotlinLogging.logger {}

    @KafkaListener(topics = ["\${topic.name.languageCount}"], groupId = "\${spring.kafka.consumer.group-id}" )
    fun consume(payload: ConsumerRecord<String, Long>) {
        LOG.info { "Server ${payload.key()} has changed ${payload.value()} times" }
    }
}
