package ru.otus;

import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.handler.ComplexProcessor;
import ru.otus.listener.ListenerPrinterConsole;
import ru.otus.listener.homework.HistoryListener;
import ru.otus.model.Message;
import ru.otus.model.TimeHolder;
import ru.otus.processor.homework.ProcessorSwapField11AndField12;
import ru.otus.processor.homework.ProcessorThrowEvenSecondExp;

public class HomeWork {

    /*
    Реализовать to do:
      1. Добавить поля field11 - field13 (для field13 используйте класс ObjectForMessage)
      2. Сделать процессор, который поменяет местами значения field11 и field12
      3. Сделать процессор, который будет выбрасывать исключение в четную секунду (сделайте тест с гарантированным результатом)
            Секунда должна определяьться во время выполнения.
            Тест - важная часть задания
            Обязательно посмотрите пример к паттерну Мементо!
      4. Сделать Listener для ведения истории (подумайте, как сделать, чтобы сообщения не портились)
         Уже есть заготовка - класс HistoryListener, надо сделать его реализацию
         Для него уже есть тест, убедитесь, что тест проходит
    */
    private static final Logger logger = LoggerFactory.getLogger(HomeWork.class);

    public static void main(String[] args) {
        TimeHolder timeHolder;
        while (true) {
            var time = LocalDateTime.now();
            if (time.getSecond() % 2 == 0) {
                timeHolder = new TimeHolder(time);
                break;
            }
        }
        var processors = List.of(new ProcessorThrowEvenSecondExp(timeHolder), new ProcessorSwapField11AndField12());

        var complexProcessor = new ComplexProcessor(processors, ex -> logger.info("exception caught"));
        var historyListener = new HistoryListener();
        var consoleListener = new ListenerPrinterConsole();
        complexProcessor.addListener(historyListener);
        complexProcessor.addListener(consoleListener);

        var message = new Message.Builder(1L)
                .field1("field1")
                .field2("field2")
                .field3("field3")
                .field6("field6")
                .field10("field10")
                .field11("field11")
                .field12("field12")
                .build();

        var result = complexProcessor.handle(message);
        logger.info("result:{}", result);

        complexProcessor.removeListener(historyListener);
    }
}
