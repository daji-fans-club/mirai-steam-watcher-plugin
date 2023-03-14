package org.example.mirai.plugin

import kotlinx.serialization.Serializable

@Serializable
data class GetPlayerSummariesResponse(val response: Response)

@Serializable
data class Response(val players: List<Player>)

@Serializable
data class Player(val steamid: String, val personaname: String, val gameextrainfo: String?)

@Serializable
data class VanityUrlResponse(val response: VanityUrlResult)

@Serializable
data class VanityUrlResult(val steamid: String, val success: Long)

@Serializable
data class SteamStatus(val personaname: String, val gameextrainfo: String, val startTime: Long)