package ru.otus.appcontainer.processor.annotation;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.definition.ComponentDefinition;

public class AppComponentAnnotationProcessor implements AnnotationProcessor {
    @Override
    public List<ComponentDefinition> process(Class<?> configureClass) {
        return Arrays.stream(configureClass.getMethods())
                .filter(method -> method.isAnnotationPresent(AppComponent.class))
                .sorted(Comparator.comparingInt(
                        method -> method.getAnnotation(AppComponent.class).order()))
                .map(method -> {
                    var name = method.getAnnotation(AppComponent.class).name();
                    var order = method.getAnnotation(AppComponent.class).order();
                    var clazz = method.getReturnType();
                    return new ComponentDefinition(name, order, clazz, method);
                })
                .toList();
    }
}
