package ru.otus.dataprocessor;

import static java.util.Objects.*;

import java.util.Map;
import ru.otus.model.Measurement;

public class FileSerializer implements Serializer {
    private final String fileName;
    private final JsonFileMapper<Measurement> jsonFileMapper = MeasurementJsonFileMapper.getInstance();

    public FileSerializer(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void serialize(Map<String, Double> data) {
        if (isNull(data) || data.isEmpty()) throw new FileProcessException("Map is empty or null");
        jsonFileMapper.writeToJsonFile(fileName, data);
    }
}
