package org.example.mirai.plugin

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

/**
 * @author reimia
 */
object SteamCall {
    private val client = PluginMain.httpClient

    @OptIn(ExperimentalSerializationApi::class)
    val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }


    suspend fun getPlayerSummaries(steamIds: List<Long>): List<Player> {
        val url =
            "http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/"
        val response: String =
            client.get(url) {
                parameter("key", SteamConfig.apiKey)
                parameter("steamids", steamIds)
            }
                .body()
        return json.decodeFromString<GetPlayerSummariesResponse>(response).response.players
    }

    suspend fun resolveVanityURL(steamId: String): Long? {
        val url =
            "http://api.steampowered.com/ISteamUser/ResolveVanityURL/v0001/"
        val response: String =
            client.get(url) {
                parameter("key", SteamConfig.apiKey)
                parameter("vanityurl", steamId)
            }
                .body()
        val urlResponse = json.decodeFromString<VanityUrlResponse>(response)
        return if (urlResponse.response.success == 1L) {
            urlResponse.response.steamid.toLong()
        } else {
            null
        }
    }

}