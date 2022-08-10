package com.acme.kafka.connect.sample

import com.acme.kafka.connect.sample.SampleSourceConnectorConfig.Companion.SSE_URI_PARAM_CONFIG
import com.acme.kafka.connect.sample.SampleSourceConnectorConfig.Companion.TOPIC_PARAM_CONFIG
import mu.KotlinLogging
import org.apache.kafka.common.config.Config
import org.apache.kafka.connect.connector.Task
import org.apache.kafka.connect.errors.ConnectException
import org.apache.kafka.connect.source.SourceConnector

class SampleSourceConnector : SourceConnector() {
    private val log = KotlinLogging.logger {}
    private lateinit var originalProps: Map<String, String>

    override fun version() = PropertiesUtil.connectorVersion

    override fun config() = SampleSourceConnectorConfig.CONFIG_DEF

    override fun taskClass(): Class<out Task?> = SampleSourceTask::class.java

    override fun validate(connectorConfigs: Map<String, String>): Config {
        val config = super.validate(connectorConfigs)
        val configValues = config.configValues()
        var missingTopicDefinition = true
        var missingSseUriDefinition = true

        for (configValue in configValues) {
            when(configValue.name()) {
                TOPIC_PARAM_CONFIG -> if (configValue.value() != null) missingTopicDefinition = false
                SSE_URI_PARAM_CONFIG -> if (configValue.value() != null) missingSseUriDefinition = false
            }

            if (!missingTopicDefinition && !missingSseUriDefinition) {
                break
            }
        }
        if (missingTopicDefinition || missingSseUriDefinition) {
            throw ConnectException(
                String.format(
                    "Properties '%s' and '%s' must be set in the configuration.",
                    TOPIC_PARAM_CONFIG,
                    SSE_URI_PARAM_CONFIG
                )
            )
        }
        return config
    }

    override fun start(originalProps: Map<String, String>) {
        this.originalProps = originalProps
        SampleSourceConnectorConfig(originalProps)
        log.info("Starting connector")
    }

    override fun taskConfigs(maxTasks: Int) = listOf<Map<String, String>>(HashMap(originalProps))

    override fun stop() {
        log.info("Stopping connector")
    }
}
