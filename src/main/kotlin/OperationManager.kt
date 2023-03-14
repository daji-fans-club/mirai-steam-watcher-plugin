package org.example.mirai.plugin

import java.time.Instant
import java.time.temporal.ChronoUnit
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.MemberCommandSender
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.util.ConsoleExperimentalApi

/**
 * 瑟图配置命令管理
 */
object OperationManager : CompositeCommand(PluginMain, "steam") {

    @ExperimentalCommandDescriptors
    @ConsoleExperimentalApi
    override val prefixOptional: Boolean = true

    @SubCommand("添加", "add")
    suspend fun MemberCommandSender.add(id: String) {
        val groupId = user.group.id
        val realId = SteamCall.resolveVanityURL(id)
        if (realId != null) {
            SteamUserData.steamUserList.add(Pair(realId, groupId))
            sendMessage("添加成功, steamId是${realId}")
        } else {
            sendMessage("不合法的id，${id}")
        }

    }

    @SubCommand("删除", "delete")
    suspend fun MemberCommandSender.delete(id: String) {
        val groupId = user.group.id
        val realId = SteamCall.resolveVanityURL(id)
        if (realId != null) {
            SteamUserData.steamUserList.remove(Pair(realId, groupId))
            SteamUserData.steamUserStatusMap.remove(realId)
            sendMessage("删除成功")
        } else {
            sendMessage("不合法的id，${id}")
        }
    }

    @SubCommand("列表", "list")
    suspend fun MemberCommandSender.list() {
        val groupId = user.group.id
        val steamUsers = SteamUserData.steamUserList.filter { pair -> pair.second == groupId }
            .map { pair -> pair.first }.distinct()
        if (steamUsers.isEmpty()) {
            sendMessage("本群没有任何监听")
        } else {
            var message = ""
            SteamUserData.steamUserStatusMap.filter { entry -> entry.key in steamUsers }
                .forEach { (_, u) ->
                    message = message.plus(
                        "${u.personaname} 正在玩 ${u.gameextrainfo}, 已经过了${
                            ChronoUnit.MINUTES.between(
                                Instant.ofEpochSecond(u.startTime),
                                Instant.now()
                            )
                        }分钟\n"
                    )
                }
            SteamUserData.steamUserStatusMap.filter { entry -> entry.key !in steamUsers }
                .forEach { (_, u) ->
                    message = message.plus(
                        "${u.personaname} 没有在玩任何游戏"
                    )
                }
            sendMessage(message)
        }
    }

    @SubCommand("帮助", "help")
    suspend fun MemberCommandSender.help() {
        sendMessage(
            """
            steam add <steamId>： 添加一个偷窥
            steam delete <steamId>： 删除一个偷窥
            steam list： 偷窥一下所有人
        """.trimIndent()
        )
    }
}