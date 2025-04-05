package com.hanchen.hcfenjie.data.matching;

import java.util.HashMap;
import java.util.Map;

public class MatchingManage {
    private static final Map<String, Matching> matchingMap = new HashMap<>();

    public static void register(String type, Matching matching) {
        matchingMap.putIfAbsent(type, matching);
        System.out.println("[HC-FenJie]§a成功注册条件匹配类型: " + type);
    }

    public static Map<String, Matching> getMatchingMap() {
        return matchingMap;
    }

    public static Matching getMatching(String type) {
        if (matchingMap.get(type) != null) {
            return matchingMap.get(type);
        }
        System.out.println("[HC-FenJie]条件匹配不存在 类型: " + type);
        return null;
    }
}