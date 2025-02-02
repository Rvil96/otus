package ru.otus;

import ru.otus.handler.AnnotationHandler;
import ru.otus.handler.AnnotationHandlerImpl;
import ru.otus.tested.TestedClass;

public class Main {
    public static void main(String[] args) {
        AnnotationHandler p = new AnnotationHandlerImpl();
        p.handle(TestedClass.class);
    }
}
