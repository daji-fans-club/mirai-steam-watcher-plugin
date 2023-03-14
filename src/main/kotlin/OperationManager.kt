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
    suspend fun MemberCommandSender.add(id: Long) {
        val groupId = user.group.id
        SteamUserData.steamUserList.add(Pair(id, groupId))
        sendMessage("添加成功")
    }

    @SubCommand("删除", "delete")
    suspend fun MemberCommandSender.delete(id: Long) {
        val groupId = user.group.id
        SteamUserData.steamUserList.remove(Pair(id, groupId))
        SteamUserData.steamUserStatusMap.remove(id)
        sendMessage("删除成功")
    }

    @SubCommand("列表", "list")
    suspend fun MemberCommandSender.list() {
        val string = ""
        val groupId = user.group.id
        val steamUsers = SteamUserData.steamUserList.filter { pair -> pair.second == groupId }
            .map { pair -> pair.first }.distinct()
        SteamUserData.steamUserStatusMap.filter { entry -> entry.key in steamUsers }
            .forEach { (_, u) ->
                string.plus(
                    "${u.personaname} 正在玩 ${u.gameextrainfo}, 过了${
                        ChronoUnit.MINUTES.between(
                            Instant.ofEpochSecond(u.startTime),
                            Instant.now()
                        )
                    }分钟\n"
                )
            }
        sendMessage(string)
    }
}