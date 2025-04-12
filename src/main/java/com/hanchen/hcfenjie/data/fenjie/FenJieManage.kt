package com.hanchen.hcfenjie.data.fenjie

import com.hanchen.hcfenjie.util.LoggerUtil

/**
 * 分解管理器
 * 负责管理分解配置的注册、获取和清空
 */
object FenJieManage {
    private val fenJieMap = mutableMapOf<String, FenJie>()

    /**
     * 注册分解配置
     * @param fenJieName 分解配置名称
     * @param fenJie 分解配置对象
     */
    fun register(fenJieName: String, fenJie: FenJie) {
        fenJieMap.computeIfAbsent(fenJieName) {
            LoggerUtil.info("注册分解配置: $fenJieName")
            fenJie
        } ?: run {
            // 调试模式提示
            LoggerUtil.debug("分解配置 $fenJieName 已存在，跳过注册")
            LoggerUtil.warn("分解配置 $fenJieName 已存在，跳过注册")
        }
    }

    /**
     * 获取所有分解配置
     * @return 分解配置映射
     */
    fun getFenJieMap(): Map<String, FenJie> = fenJieMap.toMap()

    /**
     * 清空所有分解配置
     */
    fun clear() {
        fenJieMap.clear()
        LoggerUtil.info("已清空所有分解配置")
    }
}