package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import ru.otus.jdbc.mapper.convention.FieldNameConventionMapper;

@Slf4j
public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData {

    private final EntityClassMetaData<T> entityClassMetaData;
    private final String tableName;
    private final String selectAll;
    private final String selectById;
    private final String insert;
    private final String update;

    public EntitySQLMetaDataImpl(EntityClassMetaData<T> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
        this.tableName = FieldNameConventionMapper.camelToSnake(entityClassMetaData.getName());
        this.selectAll = initSqlSelect();
        this.selectById = initSqlSelectById();
        this.insert = initSqlInsert();
        this.update = initSqlUpdate();
    }

    @Override
    public String getSelectAllSql() {
        return selectAll;
    }

    @Override
    public String getSelectByIdSql() {
        return selectById;
    }

    @Override
    public String getInsertSql() {
        return insert;
    }

    @Override
    public String getUpdateSql() {
        return update;
    }

    private String initSqlSelect() {
        var sql = "SELECT * FROM " + tableName;
        log.info("Sql for select all is generated: {}", sql);
        return sql;
    }

    private String initSqlSelectById() {
        var idName = FieldNameConventionMapper.camelToSnake(
                entityClassMetaData.getIdField().getName());

        var sql = "SELECT * FROM " + tableName + " WHERE " + idName + " = ?";
        log.info("Sql for select by id is generated: {}", sql);
        return sql;
    }

    private String initSqlInsert() {
        var fields = entityClassMetaData.getFieldsWithoutId();

        if (fields.isEmpty()) {
            throw new RuntimeException("No fields found in " + entityClassMetaData.getName());
        }

        var fieldsName = fields.stream()
                .map(Field::getName)
                .map(FieldNameConventionMapper::camelToSnake)
                .collect(Collectors.joining(", "));

        var placeHolders = String.join(", ", Collections.nCopies(fields.size(), "?"));

        var sql = "INSERT INTO " + tableName + " (" + fieldsName + ") VALUES (" + placeHolders + ")";
        log.info("Sql for insert is generated: {}", sql);

        return sql;
    }

    private String initSqlUpdate() {
        var fields = entityClassMetaData.getFieldsWithoutId();

        if (fields.isEmpty()) throw new RuntimeException("No fields found in " + entityClassMetaData.getName());

        var fieldsString = fields.stream()
                .map(Field::getName)
                .map(FieldNameConventionMapper::camelToSnake)
                .map(s -> s + " = ?")
                .collect(Collectors.joining(", "));

        var idString = FieldNameConventionMapper.camelToSnake(
                entityClassMetaData.getIdField().getName());

        var sql = "UPDATE " + tableName + " SET " + fieldsString + " WHERE " + idString + " = ?";
        log.info("Sql for update is generated: {}", sql);
        return sql;
    }
}
