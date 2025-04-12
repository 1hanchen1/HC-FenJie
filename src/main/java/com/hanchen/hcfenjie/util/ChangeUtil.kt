package com.hanchen.hcfenjie.util

import kotlin.random.Random

/**
 * 概率工具类
 * 提供概率检查功能
 */
object ChangeUtil {
    private const val PRECISION = 1_000_000

    /**
     * 更精确的概率检查方法
     * @param probability 0.0-1.0之间的概率值
     * @return 是否通过概率检查
     */
    fun checkProbability(probability: Double): Boolean {
        require(probability in 0.0..1.0) { "概率值必须在0-1之间" }

        // 调试模式提示
        LoggerUtil.debug("概率检查: 概率值=$probability")

        val scaledProb = (probability * PRECISION).toInt()
        return Random.nextInt(PRECISION) < scaledProb
    }
}