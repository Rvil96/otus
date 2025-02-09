package ru.otus.example;

import ru.otus.annotation.Log;

public interface Logged {

    @Log
    void someMethodOne(int x);

    @Log
    void someMethodTwo(int x, String y);

    @Log
    void someMethodThree(int x, String y, long z);
}
