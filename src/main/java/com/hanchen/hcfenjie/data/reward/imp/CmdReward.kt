package com.hanchen.hcfenjie.data.reward.imp

import com.hanchen.hcfenjie.Main
import com.hanchen.hcfenjie.data.reward.Reward
import com.hanchen.hcfenjie.util.LoggerUtil
import com.hanchen.hcfenjie.util.MessageUtil
import org.bukkit.Bukkit
import org.bukkit.entity.Player

/**
 * 命令奖励实现类
 * 通过执行命令来发放奖励
 */
class CmdReward : Reward {
    companion object {
        private const val PREFIX = "cmd<->"
        private const val DELIMITER = "<->"
        private const val PLACEHOLDER_PLAYER = "<player>"
        private const val PLACEHOLDER_UUID = "<uuid>"
        private const val PLACEHOLDER_WORLD = "<world>"
        private const val PLACEHOLDER_LOCATION = "<location>"
    }

    /**
     * 执行奖励逻辑
     * @param player 玩家对象
     * @param exeString 命令字符串
     */
    override fun exeReward(player: Player, exeString: String) {
        // 调试模式提示

        LoggerUtil.debug("执行命令奖励: 玩家=${player.name}, 命令=$exeString")


        // 1. 提取有效命令内容（自动处理多余前缀）
        val rawCommand = exeString.substringAfter(PREFIX)
            .replace(Regex("^cmd<->"), "") // 清理多余前缀

        // 2. 占位符替换
        val finalCommand = replacePlaceholders(rawCommand, player).takeIf { it.isNotBlank() }
            ?: run {
                LoggerUtil.warn("生成空命令，原始输入: $exeString")
                return
            }

        // 3. 执行命令
        try {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), finalCommand)
            LoggerUtil.debug("玩家 ${player.name} 执行命令成功: $finalCommand")
        } catch (e: Exception) {
            LoggerUtil.error("命令执行失败 [$finalCommand] 原因: ${e.stackTraceToString()}")
            MessageUtil.sendFormattedMessage(
                player,
                "command-error",
                "command" to finalCommand,
                "error_code" to "0xCMD-${System.currentTimeMillis() % 10000}"
            )
        }
    }

    /**
     * 替换动态占位符
     * @param raw 原始命令字符串
     * @param player 玩家对象
     * @return 替换后的命令字符串
     */
    private fun replacePlaceholders(raw: String, player: Player): String {
        return raw.replace(PLACEHOLDER_PLAYER, player.name)
            .replace(PLACEHOLDER_UUID, player.uniqueId.toString())
            .replace(PLACEHOLDER_WORLD, player.world.name)
            .replace(PLACEHOLDER_LOCATION,
                "${player.location.blockX},${player.location.blockY},${player.location.blockZ}"
            )
    }
}