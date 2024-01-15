package com.example.fakebook.global.util;

public class StringUtil {
    public static String removeBearerPrefix(String string) {
        return string.replace("Bearer ", "");
    }

    public static String replaceFirstStringToLowercase(String string){
        return string.substring(0, 1).toLowerCase() + string.substring(1);
    }
}
