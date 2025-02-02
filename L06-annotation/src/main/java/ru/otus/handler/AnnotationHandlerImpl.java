package ru.otus.handler;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.annotation.After;
import ru.otus.annotation.Before;
import ru.otus.annotation.Test;

public class AnnotationHandlerImpl implements AnnotationHandler {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerImpl.class);

    @Override
    public void handle(Class<?> clazz) {
        if (clazz == null) {
            String msg = "Unable to process null class";
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }

        Method[] methods = clazz.getDeclaredMethods();

        List<Method> testAnnotationMethods = getAnnotatedMethod(methods, Test.class);
        List<Method> beforeAnnotationMethods = getAnnotatedMethod(methods, Before.class);
        List<Method> afterAnnotationMethods = getAnnotatedMethod(methods, After.class);

        if (testAnnotationMethods.isEmpty()) {
            String msg = "Unable to process null test method";
            log.error(msg);
        }
        int countSuccess = 0;
        int countFail = 0;
        for (Method method : testAnnotationMethods) {
            Object instance = getInstance(clazz);
            try {
                invokeMethods(beforeAnnotationMethods, instance);
                method.invoke(instance);
                countSuccess++;
            } catch (Exception e) {
                log.info("The test method '{}' failed:((((", method.getName());
                countFail++;
            } finally {
                invokeMethods(afterAnnotationMethods, instance);
            }
        }
        String report = String.format(
                "%nTests started: %d%nTests finished: %d%nTest failed: %d%n",
                testAnnotationMethods.size(), countSuccess, countFail);
        log.info(report);
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
