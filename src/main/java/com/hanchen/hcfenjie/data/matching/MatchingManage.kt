package com.hanchen.hcfenjie.data.matching

object MatchingManage {
    private val matchingMap = HashMap<String, Matching>()

    fun register(type: String, matching: Matching) {
        if (!matchingMap.containsKey(type)) {
            matchingMap[type] = matching
            println("[HC-FenJie]§a成功注册条件匹配类型: $type")
        }
    }

    fun getMatchingMap(): MutableMap<String, Matching> {
        return matchingMap
    }

    fun getMatching(type: String): Matching? {
        return matchingMap[type].also {
            if (it == null) {
                println("[HC-FenJie]条件匹配不存在 类型: $type")
            }
        }
    }

    fun clear() {
        matchingMap.clear()
    }
}