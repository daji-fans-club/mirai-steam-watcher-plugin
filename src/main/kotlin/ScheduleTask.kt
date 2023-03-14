package org.example.mirai.plugin

import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Timer
import kotlin.concurrent.scheduleAtFixedRate
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.Bot

/**
 * @author reimia
 */
object ScheduleTask {

    fun start() {
        Timer().scheduleAtFixedRate(2000, 20000) {
            val bot = Bot.getInstanceOrNull(SteamConfig.bot)
            if (bot == null) {
                PluginMain.logger.info("bot is null")
            }
            val steamUsers = SteamUserData.steamUserList.map { pair -> pair.first }.distinct()
            if (steamUsers.isEmpty()) {
                return@scheduleAtFixedRate
            }
            val steamResponse = runBlocking {
                SteamCall.getPlayerSummaries(steamUsers)
            }
            PluginMain.logger.info(steamResponse.toString())
            steamResponse.forEach { curPlayerStatus ->
                val filter =
                    SteamUserData.steamUserList.filter { pair -> pair.first == curPlayerStatus.steamid.toLong() }
                val prePlayerStatus =
                    SteamUserData.steamUserStatusMap[curPlayerStatus.steamid.toLong()]
                if (curPlayerStatus.gameextrainfo != null && prePlayerStatus == null) {
                    SteamUserData.steamUserStatusMap[curPlayerStatus.steamid.toLong()] =
                        SteamStatus(
                            curPlayerStatus.personaname,
                            curPlayerStatus.gameextrainfo,
                            Instant.now().epochSecond
                        )
                    filter.forEach { pair ->
                        runBlocking {
                            PluginMain.logger.info("${curPlayerStatus.personaname} 正在玩 ${curPlayerStatus.gameextrainfo}")
                            bot?.getGroup(pair.second)
                                ?.sendMessage("${curPlayerStatus.personaname} 正在玩 ${curPlayerStatus.gameextrainfo}")
                        }
                    }

                }

                if (curPlayerStatus.gameextrainfo == null && prePlayerStatus != null) {
                    SteamUserData.steamUserStatusMap.remove(curPlayerStatus.steamid.toLong())
                    filter.forEach { pair ->
                        val playedTime = ChronoUnit.MINUTES.between(
                            Instant.ofEpochSecond(prePlayerStatus.startTime),
                            Instant.now()
                        )
                        runBlocking {
                            PluginMain.logger.info("${curPlayerStatus.personaname} 玩了 $playedTime 分钟后，不玩 ${prePlayerStatus.gameextrainfo} 了")
                            bot?.getGroup(pair.second)
                                ?.sendMessage("${curPlayerStatus.personaname} 玩了 $playedTime 分钟后，不玩 ${prePlayerStatus.gameextrainfo} 了")
                        }
                    }
                }

                if (curPlayerStatus.gameextrainfo != null && prePlayerStatus != null && curPlayerStatus.gameextrainfo != prePlayerStatus.gameextrainfo) {
                    SteamUserData.steamUserStatusMap[curPlayerStatus.steamid.toLong()] =
                        SteamStatus(
                            curPlayerStatus.personaname,
                            curPlayerStatus.gameextrainfo,
                            Instant.now().epochSecond
                        )
                    filter.forEach { pair ->
                        val playedTime = ChronoUnit.MINUTES.between(
                            Instant.ofEpochSecond(prePlayerStatus.startTime),
                            Instant.now()
                        )
                        runBlocking {
                            PluginMain.logger.info("${curPlayerStatus.personaname} 玩了 ${prePlayerStatus.gameextrainfo} $playedTime 分钟后，玩起了 ${curPlayerStatus.gameextrainfo}")
                            bot?.getGroup(pair.second)
                                ?.sendMessage("${curPlayerStatus.personaname} 玩了 ${prePlayerStatus.gameextrainfo} $playedTime 分钟后，玩起了 ${curPlayerStatus.gameextrainfo}")
                        }
                    }
                }
            }
        }
    }

}