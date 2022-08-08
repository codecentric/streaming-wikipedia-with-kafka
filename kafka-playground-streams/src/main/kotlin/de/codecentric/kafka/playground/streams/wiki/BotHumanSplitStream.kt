package de.codecentric.kafka.playground.streams.wiki

import de.codecentric.kafka.playground.streams.wiki.types.WikipediaEvent
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

    @Autowired
    fun splitBotAndHumanEvents(streamsBuilder: StreamsBuilder) {
        val messageStream: KStream<String, WikipediaEvent> =
            streamsBuilder.stream(inputTopicName, Consumed.with(KEY_SERDE, VALUE_SERDE))

        val isBot = Predicate { _: String, wikiEvent: WikipediaEvent -> wikiEvent.bot}
        val isHuman = Predicate { _: String, wikiEvent: WikipediaEvent -> wikiEvent.bot.not()}

        messageStream
            .split()
            .branch(isBot, Branched.withConsumer{ ks -> ks.to(botTopic) })
            .branch(isHuman, Branched.withConsumer{ ks -> ks.to(humanTopic) })
            .noDefaultBranch()
    }
}
