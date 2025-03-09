package ru.otus.dataprocessor;

import java.util.List;
import ru.otus.model.Measurement;

public class ResourcesFileLoader implements Loader {
    private static final String PATH_TO_RESOURCES = "src/test/resources/";
    private final String pathToFile;
    private final JsonFileMapper<Measurement> jsonFileMapper = MeasurementJsonFileMapper.getInstance();

    public ResourcesFileLoader(String fileName) {
        pathToFile = PATH_TO_RESOURCES + fileName;
    }

    @Override
    public List<Measurement> load() {
        return jsonFileMapper.readObjectsFromJsonFile(pathToFile, Measurement.class);
    }
}
