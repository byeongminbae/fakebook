package com.example.fakebook.global.util;

import com.example.fakebook.global.exception.BusinessException;
import com.example.fakebook.global.exception.CommonException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ReflectionUtil {
    private static <T> List<Field> getAllFieldsByChildAndParent(Class<T> object) {
        List<Field> fields = new ArrayList<>();
        fields.addAll(List.of(object.getDeclaredFields()));
        fields.addAll(List.of(object.getSuperclass().getDeclaredFields()));
        return fields;
    }

    private static Field getField(List<Field> fields, String fieldName) {
        return fields.stream()
                .filter((f) -> f.getName().equals(fieldName))
                .findFirst()
                .orElseThrow(() -> new BusinessException(CommonException.REFLECTION_FIELD_NOT_FOUND_EXCEPTION));
    }

    private static <T> Object getFieldValue(T clazz, Field field) {
        try {
            field.setAccessible(true);
            return field.get(clazz);
        } catch (IllegalAccessException e) {
            throw new BusinessException(CommonException.REFLECTION_UNKNOWN_EXCEPTION);
        }
    }

    public static <T> Class<?> getFieldType(Class<T> clazz, String fieldName) {
        List<Field> fields = getAllFieldsByChildAndParent(clazz);
        return getField(fields, fieldName).getType();
    }

    public static <T> Object getFieldValue(T object, String fieldName) {
        List<Field> fields = getAllFieldsByChildAndParent(object.getClass());
        Field field = getField(fields, fieldName);
        return getFieldValue(object, field);
    }
}
