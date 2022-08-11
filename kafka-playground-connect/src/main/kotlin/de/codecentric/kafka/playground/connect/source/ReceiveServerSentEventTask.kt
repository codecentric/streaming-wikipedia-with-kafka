package de.codecentric.kafka.playground.connect.source

import de.codecentric.kafka.playground.connect.source.ServerSentEventSourceConnectorConfig.Companion.SSE_URI_PARAM_CONFIG
import de.codecentric.kafka.playground.connect.source.ServerSentEventSourceConnectorConfig.Companion.TOPIC_PARAM_CONFIG
import mu.KotlinLogging
import org.apache.kafka.connect.data.Schema
import org.apache.kafka.connect.source.SourceRecord
import org.apache.kafka.connect.source.SourceTask
import java.util.*

class ReceiveServerSentEventTask : SourceTask() {
    private val log = KotlinLogging.logger {}

    private lateinit var config: ServerSentEventSourceConnectorConfig
    private lateinit var sseClient: ServerSentEventClient

    override fun version() = PropertiesUtil.connectorVersion

    override fun start(properties: Map<String, String>) {
        log.info("Starting Sample Source Task")
        config = ServerSentEventSourceConnectorConfig(properties)
        sseClient = ServerSentEventClient(config.getString(SSE_URI_PARAM_CONFIG))
        sseClient.start()
    }

    override fun poll() = sseClient.receiveEvents()
            .filter { event -> event.data.startsWith("{") || event.data.startsWith("[") } // send only events where data is valid json (starts with { or [)
            .map { event ->
                    SourceRecord(
                        emptyMap<String, Any>(),
                        emptyMap<String, Any>(),
                        config.getString(TOPIC_PARAM_CONFIG),
                        Schema.STRING_SCHEMA,
                        UUID.randomUUID().toString(),
                        Schema.STRING_SCHEMA,
                        event.data
                    )
            }
            .toList()

    override fun stop() {
        log.info("Stopping Task")
        sseClient.stop()
    }
}
