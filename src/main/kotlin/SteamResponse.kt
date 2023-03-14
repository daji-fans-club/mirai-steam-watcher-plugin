package org.example.mirai.plugin

import kotlinx.serialization.Serializable

/**
 * @author reimia
 */
@Serializable
data class SteamResponse(val response: Response)

@Serializable
data class Response(val players: List<Player>)

@Serializable
data class Player(val steamid: String, val personaname: String, val gameextrainfo: String?)

@Serializable
data class SteamStatus(val personaname: String, val gameextrainfo: String, val startTime: Long)