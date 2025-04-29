package ru.otus.appcontainer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.*;
import org.reflections.Reflections;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.appcontainer.definition.ComponentDefinition;
import ru.otus.appcontainer.processor.annotation.AnnotationProcessor;
import ru.otus.appcontainer.processor.annotation.AppComponentAnnotationProcessor;

@SuppressWarnings("squid:S1068")
public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();
    private final AnnotationProcessor annotationProcessors;
    private final Reflections reflections;

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        reflections = new Reflections();
        annotationProcessors = new AppComponentAnnotationProcessor();
        processConfig(initialConfigClass);
    }

    public AppComponentsContainerImpl(Class<?>... initialConfigClasses) {
        reflections = new Reflections();
        annotationProcessors = new AppComponentAnnotationProcessor();
        processConfig(initialConfigClasses);
    }

    public AppComponentsContainerImpl(String packageName) {
        reflections = new Reflections(packageName);
        annotationProcessors = new AppComponentAnnotationProcessor();
        processConfig();
    }

    private void processConfig() {
        var configClassesArray = Optional.ofNullable(
                        reflections.getTypesAnnotatedWith(AppComponentsContainerConfig.class))
                .map(configClasses -> configClasses.stream()
                        .sorted(Comparator.comparingInt(clazz -> clazz.getAnnotation(AppComponentsContainerConfig.class)
                                .order()))
                        .toArray(Class<?>[]::new))
                .orElseThrow(() -> new RuntimeException("Not found config classes"));
        processConfig(configClassesArray);
    }

    private void processConfig(Class<?>... configClasses) {
        for (Class<?> configClass : configClasses) {
            processConfig(configClass);
        }
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);

        var definitions = annotationProcessors.process(configClass);

        Object configInstance;
        try {
            configInstance = configClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException
                | IllegalAccessException
                | InvocationTargetException
                | NoSuchMethodException e) {
            throw new RuntimeException("No correct config class", e);
        }
        for (ComponentDefinition definition : definitions) {
            if (appComponentsByName.containsKey(definition.name())) {
                throw new RuntimeException("Been with name: " + definition.name() + " is exist!");
            }
            var parameters = definition.method().getParameters();
            if (parameters.length == 0) {
                var instance = createInstance(definition, configInstance, null);
                putInContext(definition.name(), instance);
            } else {
                var params = new ArrayList<>();
                for (Parameter parameter : parameters) {
                    var paramObject = getAppComponent(parameter.getType());
                    params.add(paramObject);
                }
                var instance = createInstance(definition, configInstance, params.toArray());
                putInContext(definition.name(), instance);
            }
        }
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        List<Object> matches =
                appComponents.stream().filter(componentClass::isInstance).toList();

        if (matches.isEmpty()) {
            throw new RuntimeException("No component found for class: " + componentClass);
        }

        if (matches.size() > 1) {
            throw new RuntimeException("Multiple components found for class: " + componentClass);
        }

        return componentClass.cast(matches.getFirst());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C> C getAppComponent(String componentName) {
        var instance = appComponentsByName.get(componentName);
        if (instance == null) {
            throw new RuntimeException("Component with name " + componentName + " not found");
        }
        return (C) instance;
    }

    private void putInContext(String name, Object instance) {
        if (instance == null) {
            throw new RuntimeException("Config method returned null");
        }
        appComponents.add(instance);
        appComponentsByName.put(name, instance);
    }

    private Object createInstance(ComponentDefinition definition, Object configInstance, Object[] params) {
        try {
            return definition.method().invoke(configInstance, params);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
