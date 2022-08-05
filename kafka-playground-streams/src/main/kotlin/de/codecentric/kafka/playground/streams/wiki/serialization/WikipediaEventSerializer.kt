package de.codecentric.kafka.playground.streams.wiki.serialization

import com.google.gson.Gson
import de.codecentric.kafka.playground.streams.wiki.types.WikipediaEvent
import org.apache.kafka.common.serialization.Serializer
import java.nio.charset.StandardCharsets

class WikipediaEventSerializer: Serializer<WikipediaEvent> {

    override fun serialize(topic: String?, data: WikipediaEvent): ByteArray {
        val gson = Gson();
        return gson.toJson(data).toByteArray(StandardCharsets.UTF_8);
    }
}
