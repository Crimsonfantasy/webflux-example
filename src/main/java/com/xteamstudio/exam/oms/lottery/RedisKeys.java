package com.xteamstudio.exam.oms.lottery;

class RedisKeys {

    private static final String USER_CONFIG = "user.config";

    static String getUserConfig(String username) {
        return USER_CONFIG + "_" + username;
    }
}
