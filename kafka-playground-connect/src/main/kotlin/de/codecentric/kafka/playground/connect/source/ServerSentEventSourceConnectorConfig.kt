package de.codecentric.kafka.playground.connect.source

import org.apache.kafka.common.config.AbstractConfig
import org.apache.kafka.common.config.ConfigDef

class ServerSentEventSourceConnectorConfig(originalProps: Map<*, *>?) : AbstractConfig(CONFIG_DEF, originalProps) {
    companion object {
        const val TOPIC_PARAM_CONFIG = "topic"
        private const val TOPIC_PARAM_DOC = "This is the topic where the events will be sent to"
        const val SSE_URI_PARAM_CONFIG = "sse.uri"
        private const val SSE_URI_PARAM_DOC = "The URI where the application will fetch events"
        val CONFIG_DEF = createConfigDef()
        private fun createConfigDef(): ConfigDef {
            val configDef = ConfigDef()
            addParams(configDef)
            return configDef
        }

        private fun addParams(configDef: ConfigDef) {
            configDef
                .define(TOPIC_PARAM_CONFIG, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, TOPIC_PARAM_DOC)
                .define(SSE_URI_PARAM_CONFIG, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, SSE_URI_PARAM_DOC)
        }
    }
}
