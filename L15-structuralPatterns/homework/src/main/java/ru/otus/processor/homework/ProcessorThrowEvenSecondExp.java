package ru.otus.processor.homework;

import ru.otus.exception.EvenSecondException;
import ru.otus.model.Message;
import ru.otus.model.TimeProvider;
import ru.otus.processor.Processor;

public class ProcessorThrowEvenSecondExp implements Processor {
    private final TimeProvider timeProvider;

    public ProcessorThrowEvenSecondExp(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }

    @Override
    public Message process(Message message) {
        if (timeProvider.getLocalDateTime().getSecond() % 2 == 0) {
            throw new EvenSecondException(
                    "Second even " + timeProvider.getLocalDateTime().getSecond());
        }
        return message;
    }
}
