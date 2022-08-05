package de.codecentric.kafka.playground.streams.wiki.types

import com.google.gson.annotations.SerializedName

data class WikipediaEvent(val id: Long,
                          val type: String,
                          val title: String,
                          val comment: String,
                          val timestamp: Long,
                          val user: String,
                          val bot: Boolean,
                          @SerializedName("server_url")
                          val serverUrl: String,
                          val meta: WikipediaEventMeta
)
