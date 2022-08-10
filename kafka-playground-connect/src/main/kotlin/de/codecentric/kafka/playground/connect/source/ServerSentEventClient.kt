package de.codecentric.kafka.playground.connect.source

import com.launchdarkly.eventsource.EventHandler
import com.launchdarkly.eventsource.EventSource
import com.launchdarkly.eventsource.MessageEvent
import mu.KotlinLogging
import java.net.URI
import java.net.URISyntaxException
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.TimeUnit

class ServerSentEventClient(url: String) : EventHandler {
    private val log = KotlinLogging.logger {}

    private var eventSource: EventSource

    private val queue: BlockingQueue<MessageEvent> = LinkedBlockingDeque();

    init {
        eventSource = try {
            EventSource.Builder(this, URI(url))
                .build()
        } catch (ex: URISyntaxException) {
            throw IllegalArgumentException("Bad URI: $url")
        }
    }

    fun start() {
        log.info("Start to receive events")
        eventSource.start()
    }

    fun stop() {
        log.info("Stop to receive events")
        eventSource.close()
    }

    fun receiveEvents(): List<MessageEvent> {
        val records: MutableList<MessageEvent> = ArrayList()
        val event = queue.poll(1, TimeUnit.SECONDS);
        if (event == null) {
            log.info("Received null event");
            return records;
        }
        if (event.data != null) {
            records.add(event);
        }
        queue.drainTo(records);
        return records;
    }

    override fun onOpen() {
        log.info("Event handler now open")
    }

    override fun onClosed() {
        log.info("Event handler now closed")
    }

    override fun onMessage(eventName: String, messageEvent: MessageEvent) {
        log.debug { "Received event with name $eventName" }
        queue.offer(messageEvent);
    }

    override fun onComment(comment: String) {
        log.info { "Received comment on event handler: $comment"}
    }

    override fun onError(throwable: Throwable) {
        log.error("An exception occurred", throwable)
    }

}
