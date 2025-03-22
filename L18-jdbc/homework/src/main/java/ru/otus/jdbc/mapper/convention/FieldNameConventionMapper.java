package ru.otus.jdbc.mapper.convention;

// Сдела через статику, по хорошему нужно создать интерфейс и отдельный класс, но сделал так для простоты
public class FieldNameConventionMapper {
    public static String camelToSnake(String fieldName) {
        String regex = "([a-z])([A-Z]+)";
        String replacement = "$1_$2";
        return fieldName.replaceAll(regex, replacement).toLowerCase();
    }
}
