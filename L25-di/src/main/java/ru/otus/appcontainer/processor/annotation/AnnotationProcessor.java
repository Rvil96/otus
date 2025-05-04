package ru.otus.appcontainer.processor.annotation;

import java.util.List;
import ru.otus.appcontainer.definition.ComponentDefinition;

public interface AnnotationProcessor {
    List<ComponentDefinition> process(Class<?> clazz);
}
