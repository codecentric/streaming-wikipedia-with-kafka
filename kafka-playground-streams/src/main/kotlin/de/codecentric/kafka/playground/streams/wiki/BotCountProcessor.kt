package de.codecentric.kafka.playground.streams.wiki

import de.codecentric.kafka.playground.streams.wiki.types.WikipediaEvent
import mu.KotlinLogging
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KeyValue
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.kstream.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.Duration


@Component
class BotCountProcessor(
    @Value("\${topic.name.consumer}") val inputTopicName: String,
    @Value("\${topic.name.botCount}") val outputTopicName: String
) : AbstractStreamProcessor() {

    private val LOG = KotlinLogging.logger { }

    @Autowired
    fun buildBotCountPipeline(streamsBuilder: StreamsBuilder) {
        val messageStream: KStream<String, WikipediaEvent> =
            streamsBuilder.stream(inputTopicName, Consumed.with(KEY_SERDE, VALUE_SERDE))


        val botCounts: KTable<Windowed<String>, Long> = messageStream
            .groupBy { _, wikiEvent -> wikiEvent.bot.toString() }
            .windowedBy(TimeWindows.ofSizeAndGrace(Duration.ofMinutes(1), Duration.ofSeconds(10)))
            .count()

        botCounts.toStream()
            .map { key, value -> KeyValue(key.key(), value) }
            .peek { key, value -> LOG.info { "Key $key has value $value" } }
            .to(outputTopicName, Produced.with(Serdes.String(), Serdes.Long()))
    }


}
