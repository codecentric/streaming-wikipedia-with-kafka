package de.codecentric.kafka.playground.consumer.wiki

import mu.KotlinLogging
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class BotCountEventConsumer {

    private val LOG = KotlinLogging.logger {}

    @KafkaListener(topics = ["\${topic.name.botCount}"], groupId = "\${spring.kafka.consumer.group-id}" )
    fun consume(payload: ConsumerRecord<String, Long>) {
        if (payload.key().equals("true")) {
            LOG.info { "${payload.value()} posted by a bot" }
        } else {
            LOG.info { "${payload.value()} posted by a human" }
        }
    }
}
