package ru.otus.processor.homework;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import ru.otus.exception.EvenSecondException;
import ru.otus.model.Message;
import ru.otus.model.TimeHolder;

class ProcessorThrowEvenSecondExpTest {

    @Test
    void process() {
        TimeHolder timeHolder;
        while (true) {
            var time = LocalDateTime.now();
            if (time.getSecond() % 2 == 0) {
                timeHolder = new TimeHolder(time);
                break;
            }
        }
        var processor = new ProcessorThrowEvenSecondExp(timeHolder);

        var message = new Message.Builder(1L)
                .field1("field1")
                .field2("field2")
                .field3("field3")
                .field6("field6")
                .field10("field10")
                .build();

        assertThrows(EvenSecondException.class, () -> processor.process(message));
    }
}
