package org.example.mirai.plugin

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

/**
 * steam配置
 *
 * @author reimia
 */
object SteamConfig : AutoSavePluginConfig("SteamConfig") {

    @ValueDescription("启用的bot")
    var bot: Long by value()

    @ValueDescription("steam api key")
    val apiKey: String by value()
}