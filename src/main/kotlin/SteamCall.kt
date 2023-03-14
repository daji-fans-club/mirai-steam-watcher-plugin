package org.example.mirai.plugin

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

/**
 * @author reimia
 */
class SteamCall(private val client: HttpClient) {

    @OptIn(ExperimentalSerializationApi::class)
    val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }


    suspend fun steamRequest(steamIds: List<Long>): List<Player> {
        val url =
            "http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/"
        val response: String =
            client.get(url) {
                parameter("key", SteamConfig.apiKey)
                parameter("steamids", steamIds)
            }
                .body()
        return json.decodeFromString<SteamResponse>(response).response.players
    }

}