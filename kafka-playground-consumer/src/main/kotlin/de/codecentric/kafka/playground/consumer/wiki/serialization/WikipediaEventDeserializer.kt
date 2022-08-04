package de.codecentric.kafka.playground.consumer.wiki.serialization

import com.google.gson.Gson
import de.codecentric.kafka.playground.consumer.wiki.types.WikipediaEvent
import org.apache.kafka.common.serialization.Deserializer
import java.lang.UnsupportedOperationException
import java.nio.charset.StandardCharsets

class WikipediaEventDeserializer: Deserializer<WikipediaEvent> {

    override fun deserialize(topic: String?, data: ByteArray?): WikipediaEvent {
        if (data == null) {
            throw UnsupportedOperationException("Cannot deserialize 'null' as WikipediaEvent");
        }

        return Gson().fromJson(data.toString(StandardCharsets.UTF_8), WikipediaEvent::class.java);
    }
}
