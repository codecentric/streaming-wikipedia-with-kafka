package de.codecentric.kafka.playground.streams.wiki.serialization

import de.codecentric.kafka.playground.streams.wiki.types.WikipediaEvent
import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.common.serialization.Serializer

class WikipediaEventSerDe: Serde<WikipediaEvent> {
    override fun serializer(): Serializer<WikipediaEvent> {
        return WikipediaEventSerializer()
    }

    override fun deserializer(): Deserializer<WikipediaEvent> {
        return WikipediaEventDeserializer();
    }
}
