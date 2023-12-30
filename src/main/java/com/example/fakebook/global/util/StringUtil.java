package com.example.fakebook.global.util;

public class StringUtil {
    public static String removeBearerPrefix(String string) {
        return string.replace("Bearer ", "");
    }
}
