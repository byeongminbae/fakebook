package com.example.boilerplate.global.util;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class TimeUtil {
    public static LocalDateTime now() {
        return LocalDateTime.now(ZoneOffset.UTC);
    }
}
