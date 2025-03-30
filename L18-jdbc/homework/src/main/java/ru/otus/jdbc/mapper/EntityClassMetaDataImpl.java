package ru.otus.jdbc.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import ru.otus.crm.annotation.Id;
import ru.otus.jdbc.mapper.processor.IdAnnotationProcessor;

@Slf4j
public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private final Class<T> clazz;
    private Constructor<T> constructor;
    private Field idField;
    private List<Field> allField;
    private List<Field> allFieldsWithoutId;

    public EntityClassMetaDataImpl(Class<T> clazz) {
        this.clazz = clazz;
        initConstructor();
        initIdField();
        initAllFields();
        initAllFieldsWithoutId();
    }

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
        return constructor;
    }

    @Override
    public Field getIdField() {
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        return allField;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return allFieldsWithoutId;
    }

    private void initConstructor() {
        try {
            this.constructor = clazz.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            log.error("Constructor not found");
            throw new RuntimeException(e);
        }
    }

    private void initIdField() {
        this.idField = IdAnnotationProcessor.getIdFieldWithId(clazz);
    }

    private void initAllFields() {
        this.allField = Arrays.stream(clazz.getDeclaredFields()).toList();
    }

    private void initAllFieldsWithoutId() {
        this.allFieldsWithoutId = Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> !field.isAnnotationPresent(Id.class))
                .toList();
    }
}
