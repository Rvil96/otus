package ru.otus.processor.homework;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.exception.EvenSecondException;
import ru.otus.model.Message;
import ru.otus.model.TimeProvider;

@ExtendWith(MockitoExtension.class)
class ProcessorThrowEvenSecondExpTest {
    @Mock
    private TimeProvider timeProvider;

    private static final LocalDateTime TIME = LocalDateTime.of(2025, 3, 7, 12, 30, 2);

    @Test
    void process() {
        when(timeProvider.getLocalDateTime()).thenReturn(TIME);
        var processor = new ProcessorThrowEvenSecondExp(timeProvider);

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
