package de.codecentric.kafka.playground.streams.wiki

import de.codecentric.kafka.playground.streams.wiki.serialization.WikipediaEventSerDe
import de.codecentric.kafka.playground.streams.wiki.types.WikipediaEvent
import mu.KotlinLogging
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.kstream.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component


@Component
class BotHumanSplitStream(
    @Value("\${topic.name.consumer}") val inputTopicName: String,
    @Value("\${topic.name.bots}") val botTopic: String,
    @Value("\${topic.name.humans}") val humanTopic: String
) : AbstractStreamProcessor() {

    private val LOG = KotlinLogging.logger { }

    @Autowired
    fun splitBotAndHumanEvents(streamsBuilder: StreamsBuilder) {
        val messageStream: KStream<String, WikipediaEvent> =
            streamsBuilder.stream(inputTopicName, Consumed.with(KEY_SERDE, VALUE_SERDE))

        val isBot = Predicate { _: String, wikiEvent: WikipediaEvent -> wikiEvent.bot}
        val isHuman = Predicate { _: String, wikiEvent: WikipediaEvent -> wikiEvent.bot.not()}

        // posted by a bot
        messageStream
            .filter(isBot)
            .peek { key, value -> LOG.info { "Key $key has value $value and was posted by bot" } }
            .to(botTopic, Produced.with(Serdes.String(), WikipediaEventSerDe()))

        // posted by a human
        messageStream
            .filter(isHuman)
            .peek { key, value -> LOG.info { "Key $key has value $value ans was posted by human" } }
            .to(humanTopic, Produced.with(Serdes.String(), WikipediaEventSerDe()))
    }
}
