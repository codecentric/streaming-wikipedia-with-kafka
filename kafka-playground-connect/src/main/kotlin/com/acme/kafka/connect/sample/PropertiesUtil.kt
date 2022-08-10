package com.acme.kafka.connect.sample

import mu.KotlinLogging
import java.util.*

object PropertiesUtil {
    private val log = KotlinLogging.logger {}

    private const val CONNECTOR_VERSION = "connector.version"
    private const val propertiesFile = "/kafka-connect-sample.properties"
    private lateinit var properties: Properties

    init {
        try {
            PropertiesUtil::class.java.getResourceAsStream(propertiesFile).use { stream ->
                properties = Properties()
                properties.load(stream)
            }
        } catch (ex: Exception) {
            log.warn("Error while loading properties: ", ex)
        }
    }

    val connectorVersion: String
        get() = properties.getProperty(CONNECTOR_VERSION)
}
