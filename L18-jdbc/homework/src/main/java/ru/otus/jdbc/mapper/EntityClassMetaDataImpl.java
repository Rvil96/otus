package ru.otus.jdbc.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.otus.crm.annotation.Id;
import ru.otus.jdbc.mapper.processor.IdAnnotationProcessor;

@RequiredArgsConstructor
@Slf4j
public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private final Class<T> clazz;

    @Override
    public String getName() {
        return clazz.getSimpleName();
    }

    /**
     * @return конструктор без параметров
     * @throws RuntimeException если у класс нет публичного конструктора без параметров
     */
    @Override
    public Constructor<T> getConstructor() {
        try {
            return clazz.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            log.error("Constructor not found");
            throw new RuntimeException(e);
        }
    }

    @Override
    public Field getIdField() {
        return IdAnnotationProcessor.getIdFieldWithId(clazz);
    }

    @Override
    public List<Field> getAllFields() {
        return Arrays.stream(clazz.getDeclaredFields()).toList();
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> !field.isAnnotationPresent(Id.class))
                .toList();
    }
}
