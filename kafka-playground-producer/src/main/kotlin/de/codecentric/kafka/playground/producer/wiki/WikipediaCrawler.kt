package de.codecentric.kafka.playground.producer.wiki

import com.google.gson.Gson
import de.codecentric.kafka.playground.producer.wiki.types.WikipediaEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.codec.ServerSentEvent
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux

@Component
class WikipediaCrawler(@Value("\${wikipedia.base.url}") val wikipediaBaseUrl: String,
                       @Value("\${wikipedia.event.path}") val wikipediaEventPath: String,
                       @Autowired val wikipediaEventProducer: WikipediaEventProducer) {

    @EventListener(ApplicationReadyEvent::class)
    fun crawl() {
        val client: WebClient = WebClient.create(wikipediaBaseUrl);
        val type = object : ParameterizedTypeReference<ServerSentEvent<String>>() {};
        val eventStream: Flux<ServerSentEvent<String>> = client.get().uri(wikipediaEventPath).retrieve().bodyToFlux(type);
        val gson = Gson();
        val relevantWikis = listOf("https://en.wikipedia.org", "https://de.wikipedia.org");
        eventStream.subscribe { content ->
                if (content.data() != null) {
                    val event = gson.fromJson(content.data()!!, WikipediaEvent::class.java);
                    if (relevantWikis.contains(event.serverUrl)) {
                        wikipediaEventProducer.send(event);
                    }
                }
        }
    }
}
