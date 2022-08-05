package de.codecentric.kafka.playground.streams.wiki

import de.codecentric.kafka.playground.streams.wiki.serialization.WikipediaEventSerDe
import de.codecentric.kafka.playground.streams.wiki.types.WikipediaEvent
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.common.serialization.Serdes

abstract class AbstractStreamProcessor {

    open val KEY_SERDE: Serde<String> = Serdes.String();
    open val VALUE_SERDE: Serde<WikipediaEvent> = WikipediaEventSerDe();
}
