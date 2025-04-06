package ru.otus.jdbc.mapper.processor;

import java.lang.reflect.Field;
import java.util.Arrays;
import ru.otus.crm.annotation.Id;

// Тоже сделал через статику для простоты
public class IdAnnotationProcessor {
    public static <T> Field getIdFieldWithId(Class<T> clazz) {
        var field = Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(Id.class))
                .toList();

        if (field.isEmpty()) {
            throw new RuntimeException("No Id field found in class " + clazz.getName());
        }

        if (field.size() > 1) {
            throw new RuntimeException("Multiple Id fields found in class " + clazz.getName());
        }
        return field.getFirst();
    }
}
