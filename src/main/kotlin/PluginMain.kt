package org.example.mirai.plugin

import io.ktor.client.HttpClient
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.unregister
import net.mamoe.mirai.console.permission.AbstractPermitteeId
import net.mamoe.mirai.console.permission.PermissionService.Companion.cancel
import net.mamoe.mirai.console.permission.PermissionService.Companion.permit
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.utils.info

/**
 * 使用 kotlin 版请把
 * `src/main/resources/META-INF.services/net.mamoe.mirai.console.plugin.jvm.JvmPlugin`
 * 文件内容改成 `org.example.mirai.plugin.PluginMain` 也就是当前主类全类名
 *
 * 使用 kotlin 可以把 java 源集删除不会对项目有影响
 *
 * 在 `settings.gradle.kts` 里改构建的插件名称、依赖库和插件版本
 *
 * 在该示例下的 [JvmPluginDescription] 修改插件名称，id和版本，etc
 *
 * 可以使用 `src/test/kotlin/RunMirai.kt` 在 ide 里直接调试，
 * 不用复制到 mirai-console-loader 或其他启动器中调试
 */

object PluginMain : KotlinPlugin(
    JvmPluginDescription(
        id = "org.reimia.steam",
        name = "steam game status监听",
        version = "0.1.0"
    )
) {
    lateinit var httpClient: HttpClient

    override fun onEnable() {
        SteamConfig.reload()
        SteamUserData.reload()
        SteamUserData.steamUserStatusMap.clear()
        OperationManager.register()
        AbstractPermitteeId.AnyContact.permit(OperationManager.permission)
        httpClient = HttpClient()
        ScheduleTask.start()
        logger.info { "steam game status watcher Plugin loaded" }
    }

    override fun onDisable() {
        OperationManager.unregister()
        AbstractPermitteeId.AnyContact.cancel(OperationManager.permission, true)
        logger.info("steam game status watcher Plugin unloaded")
    }
}
