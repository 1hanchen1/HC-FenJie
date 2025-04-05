package com.hanchen.hcfenjie.data.fenjie

object FenJieManage {
    private val fenJieMap = mutableMapOf<String, FenJie>()

    fun register(fenJieName: String, fenJie: FenJie) {
        if (!fenJieMap.containsKey(fenJieName)) {
            fenJieMap[fenJieName] = fenJie
            println("[HC-FenJie]§a成功注册分解数据: $fenJieName")
        }
    }

    fun getFenJieMap(): Map<String, FenJie> {
        return fenJieMap
    }

    fun clear() {
        fenJieMap.clear()
    }
}