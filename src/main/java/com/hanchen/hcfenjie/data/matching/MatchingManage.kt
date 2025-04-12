package com.hanchen.hcfenjie.data.matching

import com.hanchen.hcfenjie.Main
import com.hanchen.hcfenjie.util.LoggerUtil

/**
 * 匹配管理器
 * 负责管理匹配类型的注册、获取和清空
 */
object MatchingManage {
    private val matchingMap = mutableMapOf<String, Matching>()

    /**
     * 注册匹配类型
     * @param type 匹配类型名称
     * @param matching 匹配类型对象
     */
    fun register(type: String, matching: Matching) {
        matchingMap.computeIfAbsent(type) {
            // 调试模式提示
            if (Main.instance.config.getBoolean("debug-mode", false)) {
                LoggerUtil.debug("注册匹配类型: $type")
            }
            LoggerUtil.info("注册匹配类型: $type")
            matching
        } ?: run {
            // 调试模式提示
            if (Main.instance.config.getBoolean("debug-mode", false)) {
                LoggerUtil.debug("匹配类型 $type 已存在，跳过注册")
            }
            LoggerUtil.warn("匹配类型 $type 已存在，跳过注册")
        }
    }

    /**
     * 获取匹配类型
     * @param type 匹配类型名称
     * @return 匹配类型对象或null
     */
    fun getMatching(type: String): Matching? = matchingMap[type].also {
        if (it == null) {
            LoggerUtil.warn("未注册的匹配类型: $type")
            // 调试模式提示
            if (Main.instance.config.getBoolean("debug-mode", false)) {
                LoggerUtil.debug("尝试获取未注册的匹配类型: $type")
            }
        }
    }

    /**
     * 获取所有匹配类型
     * @return 匹配类型名称集合
     */
    fun getAllMatchingTypes(): Set<String> = matchingMap.keys

    /**
     * 清空所有匹配类型
     */
    fun clear() {
        matchingMap.clear()
        // 调试模式提示
        if (Main.instance.config.getBoolean("debug-mode", false)) {
            LoggerUtil.debug("已清空所有匹配类型")
        }
        LoggerUtil.info("已清空所有匹配类型")
    }
}