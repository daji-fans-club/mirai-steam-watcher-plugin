package org.example.mirai.plugin

import net.mamoe.mirai.console.data.AutoSavePluginData
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

/**
 * 瑟图存储数据
 */
object SteamUserData : AutoSavePluginData("SteamUserData") {

    /**
     * pair.first: steamId
     * pair.right: group
     */
    @ValueDescription("监听的用户集合")
    val steamUserList: MutableList<Pair<Long, Long>> by value()

    /**
     * map.key steamId
     * map.value steamStatus
     */
    @ValueDescription("用户当前状态集合")
    val steamUserStatusMap: MutableMap<Long, SteamStatus> by value()
}