package de.codecentric.kafka.playground.streams.wiki

import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.StreamsConfig.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.annotation.EnableKafkaStreams
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration
import org.springframework.kafka.config.KafkaStreamsConfiguration

@Configuration
@EnableKafka
@EnableKafkaStreams
class KafkaStreamingConfig (@Value("\${spring.kafka.consumer.bootstrap-servers}") val bootstrapAddress: String) {

    @Bean(name = [KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME])
    fun kafkaStreamsConfig(): KafkaStreamsConfiguration {
        val properties: MutableMap<String, Any> = HashMap();
        properties.put(APPLICATION_ID_CONFIG, "wiki-streams-consumer");
        properties.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        properties.put(DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().javaClass.name);
        properties.put(DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().javaClass.name);
        return KafkaStreamsConfiguration(properties);
    }
}
