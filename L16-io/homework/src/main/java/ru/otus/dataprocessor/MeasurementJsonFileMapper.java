package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import ru.otus.model.Measurement;

@Slf4j
public class MeasurementJsonFileMapper implements JsonFileMapper<Measurement> {
    private final ObjectMapper objectMapper;
    private static final MeasurementJsonFileMapper INSTANCE = new MeasurementJsonFileMapper();

    public MeasurementJsonFileMapper() {
        objectMapper = new ObjectMapper();
    }

    @Override
    public List<Measurement> readObjectsFromJsonFile(String fileName, Class<Measurement> elementType) {
        try {
            List<Measurement> list = objectMapper.readValue(
                    new File(fileName), objectMapper.getTypeFactory().constructCollectionType(List.class, elementType));
            log.info("Objects {} is success read for file.", elementType.getName());
            return list;
        } catch (IOException e) {
            log.error("Objects {} is failed read for file", elementType.getName());
            throw new FileProcessException(e);
        }
    }

    @Override
    public void writeToJsonFile(String fileName, Map<String, Double> data) {
        try {
            objectMapper.writeValue(new File(fileName), data);
            log.info("Data is success write");
        } catch (IOException e) {
            log.error("Data is failed write");
            throw new FileProcessException(e);
        }
    }

    public static MeasurementJsonFileMapper getInstance() {
        return INSTANCE;
    }
}
