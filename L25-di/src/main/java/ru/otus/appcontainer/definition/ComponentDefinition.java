package ru.otus.appcontainer.definition;

import java.lang.reflect.Method;

public record ComponentDefinition(String name, int order, Class<?> clazz, Method method) {}
