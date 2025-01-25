package ru.otus.assertions;

import ru.otus.exception.AssertException;

public class Assertion {
    public static void assertionStringIsEquals(String expected, String actual) {
        if (expected == null || actual == null) {
            throw new AssertionError();
        }
        if (!expected.equals(actual)) {
            throw new AssertException(expected + " != " + actual);
        }
    }
}
