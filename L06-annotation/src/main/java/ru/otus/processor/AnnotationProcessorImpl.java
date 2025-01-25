package ru.otus.processor;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.annotation.After;
import ru.otus.annotation.Before;
import ru.otus.annotation.Test;

public class AnnotationProcessorImpl implements AnnotationProcessor {

    private static final Logger log = LoggerFactory.getLogger(AnnotationProcessorImpl.class);

    @Override
    public void process(Class<?> clazz) {
        if (clazz == null) {
            String msg = "Unable to process null class";
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }

        Method[] methods = Optional.of(clazz.getDeclaredMethods()).orElseThrow();

        List<Method> testAnnotationMethods = getAnnotatedMethod(methods, Test.class);
        List<Method> beforeAnnotationMethods = getAnnotatedMethod(methods, Before.class);
        List<Method> afterAnnotationMethods = getAnnotatedMethod(methods, After.class);

        for (Method method : testAnnotationMethods) {
            Object instance = getInstance(clazz);
            try {
                invokeMethods(beforeAnnotationMethods, instance);
                method.invoke(instance);
            } catch (Exception e) {
                log.info("The test method '{}' failed:((((", method.getName());
            } finally {
                invokeMethods(afterAnnotationMethods, instance);
            }
        }
    }

    private List<Method> getAnnotatedMethod(Method[] methods, Class<? extends Annotation> annotation) {
        return Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(annotation))
                .toList();
    }

    private Object getInstance(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException
                | IllegalAccessException
                | InvocationTargetException
                | NoSuchMethodException e) {
            log.error("Failed to create an instance of class: {}", clazz.getName());
            throw new RuntimeException(e);
        }
    }

    private void invokeMethods(List<Method> methods, Object instance) {
        for (Method method : methods) {
            try {
                method.invoke(instance);
            } catch (IllegalAccessException | InvocationTargetException e) {
                log.error("Failed to invoke method '{}': {}", method.getName(), e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }
}
