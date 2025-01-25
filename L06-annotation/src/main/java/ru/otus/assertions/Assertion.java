package ru.otus.assertions;

public class Assertion {
    public static void assertionStringIsEquals(String expected, String actual) {
        if (expected == null || actual == null) {
            throw new AssertionError();
        }
        if (!expected.equals(actual)) {
            throw new AssertionError(expected + " != " + actual);
        }
    }
}
