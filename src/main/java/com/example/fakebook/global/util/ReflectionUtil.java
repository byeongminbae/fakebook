package com.example.fakebook.global.util;

import com.example.fakebook.global.exception.BusinessException;
import com.example.fakebook.global.exception.CommonException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ReflectionUtil {
    public static <T> Class<?> getFieldType(Class<T> clazz, String fieldName){
        try {
            return clazz.getDeclaredField(fieldName).getType();
        }catch(NoSuchFieldException e){
            throw new BusinessException(CommonException.REFLECTION_FIELD_NOT_FOUND_EXCEPTION);
        }
    }

    public static <T> Object getFieldValue(T clazz, String fieldName) {
        List<Field> fields = new ArrayList<>();
        fields.addAll(List.of(clazz.getClass().getDeclaredFields()));
        fields.addAll(List.of(clazz.getClass().getSuperclass().getDeclaredFields()));

        Field field = fields.stream()
                .filter((f) -> f.getName().equals(fieldName))
                .findFirst()
                .orElseThrow(() -> new BusinessException(CommonException.REFLECTION_FIELD_NOT_FOUND_EXCEPTION));

        return getFieldValue(clazz, field);
    }

    public static <T> Object getFieldValue(T clazz, Field field){
        try {
            field.setAccessible(true);
            return field.get(clazz);
        } catch (IllegalAccessException e) {
            throw new BusinessException(CommonException.REFLECTION_UNKNOWN_EXCEPTION);
        }
    }
}
