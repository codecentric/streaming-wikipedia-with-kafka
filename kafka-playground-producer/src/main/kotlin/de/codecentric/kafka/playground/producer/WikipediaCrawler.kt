package de.codecentric.kafka.playground.producer

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
        eventStream.subscribe { content -> {
            if (content.data() != null && (content.data()!!.contains("https://de.") || content.data()!!.contains("https://en."))) {
                println(content.data())
                wikipediaEventProducer.send(content.data()!!)
            }
        }}
    }
}
