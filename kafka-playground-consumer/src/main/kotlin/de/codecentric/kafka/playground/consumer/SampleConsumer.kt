package de.codecentric.kafka.playground.consumer

import mu.KotlinLogging
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class SampleConsumer {

    private val LOG = KotlinLogging.logger {}

    @KafkaListener(topics = ["\${topic.name.consumer}"], groupId = "\${spring.kafka.consumer.group-id}" )
    fun consume(payload: ConsumerRecord<String, String>) {
        LOG.info { "Received message with key ${payload.key()}"};
        LOG.info { "Received message with payload ${payload.value()}"};
    }
}
