package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Counter {
    private static final Logger logger = LoggerFactory.getLogger(Counter.class);
    private final int[] data = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    private int counter = 0;
    private boolean isReversed = false;
    private boolean isModifierTurn = false;

    private synchronized void actionAndModifyCount(boolean modifierTurn) {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                while (isModifierTurn != modifierTurn) {
                    wait();
                }

                sleep();
                logger.info("Value: {}", data[counter]);

                if (isModifierTurn) {
                    if (isReversed) {
                        counter--;
                    } else {
                        counter++;
                    }

                    if (counter == 0) {
                        isReversed = false;
                    }
                    if (counter == data.length - 1) {
                        isReversed = true;
                    }
                }

                isModifierTurn = !isModifierTurn;
                notifyAll();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        Counter counter = new Counter();
        new Thread(() -> counter.actionAndModifyCount(false)).start();
        new Thread(() -> counter.actionAndModifyCount(true)).start();
    }

    private static void sleep() {
        try {
            Thread.sleep(1_00);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
