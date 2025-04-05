package com.hanchen.hcfenjie.data.fenjie;

import java.util.HashMap;
import java.util.Map;

public class FenJieManage {
    private static final Map<String, FenJie> fenJieMap = new HashMap<>();

    public static void register(String fenJieName, FenJie fenJie) {
        fenJieMap.putIfAbsent(fenJieName, fenJie);
        System.out.println("[HC-FenJie]§a成功注册分解数据: " + fenJieName);
    }

    public static Map<String, FenJie> getFenJieMap() {
        return fenJieMap;
    }
}