package ru.otus.dataprocessor;

import java.util.List;
import java.util.Map;

public interface JsonFileMapper<T> {
    List<T> readObjectsFromJsonFile(String fileName, Class<T> elementType);

    void writeToJsonFile(String fileName, Map<String, Double> data);
}
