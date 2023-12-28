package com.example.boilerplate.global.util;

public class StringUtil {
    public static String removeBearerPrefix(String string){
        return string.replace("Bearer ", "");
    }
}
