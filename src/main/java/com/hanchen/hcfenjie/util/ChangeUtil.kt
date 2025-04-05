package com.hanchen.hcfenjie.util

import java.text.DecimalFormat
import kotlin.random.Random

object ChangeUtil {
    fun check(d: Double): Boolean {
        val df = DecimalFormat("######0.000")
        return Random.nextDouble() - df.format(d).toDouble() < 0.0
    }
}