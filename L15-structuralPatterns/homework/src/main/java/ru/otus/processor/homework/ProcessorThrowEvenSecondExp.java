package ru.otus.processor.homework;

import ru.otus.exception.EvenSecondException;
import ru.otus.model.Message;
import ru.otus.model.TimeHolder;
import ru.otus.processor.Processor;

public class ProcessorThrowEvenSecondExp implements Processor {
    private final TimeHolder timeHolder;

    public ProcessorThrowEvenSecondExp(TimeHolder timeHolder) {
        this.timeHolder = timeHolder;
    }

    @Override
    public Message process(Message message) {
        if (timeHolder.getLocalDateTime().getSecond() % 2 == 0) {
            throw new EvenSecondException("Second even " + timeHolder.getLocalDateTime().getSecond());
        }
        return message;
    }
}
