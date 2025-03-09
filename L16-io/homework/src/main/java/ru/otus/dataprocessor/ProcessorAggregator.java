package ru.otus.dataprocessor;

import static java.util.Objects.*;
import static java.util.stream.Collectors.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import ru.otus.model.Measurement;

public class ProcessorAggregator implements Processor {

    @Override
    public Map<String, Double> process(List<Measurement> data) {
        return isNull(data) || data.isEmpty()
                ? Collections.emptyMap()
                : data.stream().collect(toMap(Measurement::name, Measurement::value, Double::sum, TreeMap::new));
    }
}
