package com.acme.kafka.connect.sample

import com.acme.kafka.connect.sample.SampleSourceConnectorConfig.Companion.SSE_URI_PARAM_CONFIG
import com.acme.kafka.connect.sample.SampleSourceConnectorConfig.Companion.TOPIC_PARAM_CONFIG
import mu.KotlinLogging
import org.apache.kafka.connect.data.Schema
import org.apache.kafka.connect.source.SourceRecord
import org.apache.kafka.connect.source.SourceTask

class SampleSourceTask : SourceTask() {
    private val log = KotlinLogging.logger {}

    private lateinit var config: SampleSourceConnectorConfig
    private lateinit var sseClient: ServerSentEventClient

    override fun version() = PropertiesUtil.connectorVersion

    override fun start(properties: Map<String, String>) {
        log.info("Starting Sample Source Task")
        config = SampleSourceConnectorConfig(properties)
        sseClient = ServerSentEventClient(config.getString(SSE_URI_PARAM_CONFIG))
        sseClient.start()
    }

    override fun poll() = sseClient.receiveEvents()
            .map { event ->
                    SourceRecord(
                        emptyMap<String, Any>(),
                        emptyMap<String, Any>(),
                        config.getString(TOPIC_PARAM_CONFIG),
                        null,
                        null,
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
