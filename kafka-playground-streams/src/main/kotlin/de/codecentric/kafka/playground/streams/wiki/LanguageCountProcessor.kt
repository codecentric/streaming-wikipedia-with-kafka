package de.codecentric.kafka.playground.streams.wiki

import de.codecentric.kafka.playground.streams.wiki.types.WikipediaEvent
import mu.KotlinLogging
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.kstream.Consumed
import org.apache.kafka.streams.kstream.KStream
import org.apache.kafka.streams.kstream.KTable
import org.apache.kafka.streams.kstream.Produced
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class LanguageCountProcessor(@Value("\${topic.name.consumer}") val inputTopicName: String,
                             @Value("\${topic.name.languageCount}") val outputTopicName: String): AbstractStreamProcessor() {

    private val LOG = KotlinLogging.logger {  };

    @Autowired
    fun buildLanguageCountPipeline(streamsBuilder: StreamsBuilder) {
        val messageStream: KStream<String, WikipediaEvent> =
            streamsBuilder.stream(inputTopicName, Consumed.with(KEY_SERDE, VALUE_SERDE));

        val languageCounts: KTable<String, Long> = messageStream.groupBy { key, wikiEvent -> wikiEvent.serverUrl }
                                                                .count();

        languageCounts.toStream()
                      .peek { key, value -> LOG.info { "Key $key has value $value" } }
                      .to(outputTopicName, Produced.with(Serdes.String(), Serdes.Long()));
    }


}
