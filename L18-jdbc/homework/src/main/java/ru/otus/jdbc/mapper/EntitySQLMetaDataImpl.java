package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import ru.otus.jdbc.mapper.convention.FieldNameConventionMapper;

@Slf4j
public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData {

    private final EntityClassMetaData<T> entityClassMetaData;
    private final String TABLE_NAME;
    private final String SELECT_ALL;
    private final String SELECT_BY_ID;
    private final String INSERT;
    private final String UPDATE;

    public EntitySQLMetaDataImpl(EntityClassMetaData<T> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
        this.TABLE_NAME = FieldNameConventionMapper.camelToSnake(entityClassMetaData.getName());
        this.SELECT_ALL = initSqlSelect();
        this.SELECT_BY_ID = initSqlSelectById();
        this.INSERT = initSqlInsert();
        this.UPDATE = initSqlUpdate();
    }

    @Override
    public String getSelectAllSql() {
        return SELECT_ALL;
    }

    @Override
    public String getSelectByIdSql() {
        return SELECT_BY_ID;
    }

    @Override
    public String getInsertSql() {
        return INSERT;
    }

    @Override
    public String getUpdateSql() {
        return UPDATE;
    }

    private String initSqlSelect() {
        var sql = "SELECT * FROM " + TABLE_NAME;
        log.info("Sql for select all is generated: {}", sql);
        return sql;
    }

    private String initSqlSelectById() {
        var idName = FieldNameConventionMapper.camelToSnake(
                entityClassMetaData.getIdField().getName());

        var sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + idName + " = ?";
        log.info("Sql for select by id is generated: {}", sql);
        return sql;
    }

    private String initSqlInsert() {
        var fields = entityClassMetaData.getFieldsWithoutId();

        if (fields.isEmpty()) throw new RuntimeException("No fields found in " + entityClassMetaData.getName());

        var fieldsName = fields.stream()
                .map(Field::getName)
                .map(FieldNameConventionMapper::camelToSnake)
                .collect(Collectors.joining(", "));

        var placeHolders = String.join(", ", Collections.nCopies(fields.size(), "?"));

        var sql = "INSERT INTO " + TABLE_NAME + " (" + fieldsName + ") VALUES (" + placeHolders + ")";
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

        var sql = "UPDATE " + TABLE_NAME + " SET " + fieldsString + " WHERE " + idString + " = ?";
        log.info("Sql for update is generated: {}", sql);
        return sql;
    }
}
