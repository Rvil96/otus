package ru.otus.tested;

import static ru.otus.assertions.Assertion.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.annotation.After;
import ru.otus.annotation.Before;
import ru.otus.annotation.Test;

public class TestedClass {
    private final Logger log = LoggerFactory.getLogger(TestedClass.class);

    private String expected;

    private String actual;

    private String notExpected;

    @Before
    public void setExpected() {
        expected = "Hello World";
        log.info("Before setExpected");
    }

    @Before
    public void setNotExpected() {
        notExpected = "Goodbye World";
        log.info("Before setNotExpected");
    }

    @Before
    public void setActual() {
        actual = "Hello World";
        log.info("Before setActual");
    }

    @Test
    public void shouldBeSuccessful() {
        assertionStringIsEquals(expected, actual);
        log.info("Successful assertion");
    }

    @Test
    public void shouldBeUnsuccessful() {
        assertionStringIsEquals(notExpected, actual);
        log.info("Unsuccessful assertion");
    }

    @After
    public void cleanNotExpected() {
        notExpected = null;
        log.info("After cleanNotExpected");
    }

    @After
    public void cleanExpected() {
        expected = null;
        log.info("After cleanExpected");
    }

    @After
    public void cleanActual() {
        actual = null;
        log.info("After cleanActual");
    }
}
