package de.codecentric.kafka.playground.consumer.wiki.types

import com.google.gson.annotations.SerializedName
import java.util.*

data class WikipediaEventMeta(val uri: String,
                              @SerializedName("request_id")
                              val requestId: UUID,
                              val id: UUID,
                              val dt: String,
                              val domain: String)
