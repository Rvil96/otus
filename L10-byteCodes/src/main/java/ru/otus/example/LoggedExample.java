package ru.otus.example;

import ru.otus.annotation.Log;

public class LoggedExample implements Logged {
    @Log
    @Override
    public void someMethodOne(int x) {
        System.out.println("someMethod one");
    }

    @Log
    @Override
    public void someMethodTwo(int x, String y) {
        System.out.println("someMethod two");
    }

    @Log
    @Override
    public void someMethodThree(int x, String y, long z) {
        System.out.println("someMethod three");
    }
}
