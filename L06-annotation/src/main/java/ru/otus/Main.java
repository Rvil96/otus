package ru.otus;

import ru.otus.processor.AnnotationProcessorImpl;
import ru.otus.tested.TestedClass;

public class Main {
    public static void main(String[] args) {
        AnnotationProcessorImpl p = new AnnotationProcessorImpl();
        p.process(TestedClass.class);
    }
}
