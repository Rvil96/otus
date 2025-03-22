package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.executor.DbExecutor;
import ru.otus.jdbc.mapper.convention.FieldNameConventionMapper;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
@SuppressWarnings("java:S1068")
@Slf4j
@RequiredArgsConstructor
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor
                .executeSelect(
                        connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), this::mapResultSetToObjects)
                .map(List::getFirst);
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor
                .executeSelect(
                        connection,
                        entitySQLMetaData.getSelectAllSql(),
                        Collections.emptyList(),
                        this::mapResultSetToObjects)
                .orElse(Collections.emptyList());
    }

    @Override
    public long insert(Connection connection, T client) {
        return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), getValueFields(client, false));
    }

    @Override
    public void update(Connection connection, T client) {
        dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), getValueFields(client, true));
    }
// Тут тоже можно было бы вынести в отдельный класс
    private List<T> mapResultSetToObjects(ResultSet rs) {
        var resultList = new ArrayList<T>();
        try {
            while (rs.next()) {
                T obj = entityClassMetaData.getConstructor().newInstance();
                var fields = entityClassMetaData.getAllFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    var value = rs.getObject(FieldNameConventionMapper.camelToSnake(field.getName()));
                    field.set(obj, value);
                }
                resultList.add(obj);
            }
            return resultList;
        } catch (SQLException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("ResultSet mapping error", e);
        }
    }

    private List<Object> getValueFields(T client, boolean includeId) {
        List<Field> fields = new ArrayList<>(entityClassMetaData.getFieldsWithoutId());
        if (includeId) {
            fields.add(entityClassMetaData.getIdField());
        }

        return fields.stream()
                .peek(field -> field.setAccessible(true))
                .map(field -> {
                    try {
                        return field.get(client);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Error accessing field value", e);
                    }
                })
                .collect(Collectors.toList());
    }
}
