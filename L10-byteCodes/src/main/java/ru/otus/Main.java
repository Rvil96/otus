package ru.otus;

import ru.otus.example.LoggedExample;
import ru.otus.example.Logged;
import ru.otus.proxy.LogProxyFactory;

public class Main {
    public static void main(String[] args) {
        Logged logExample = new LoggedExample();
        Logged proxyLog = (Logged) new LogProxyFactory(logExample).getProxy();

        proxyLog.someMethodOne(10);
        proxyLog.someMethodTwo(10, "StringOnMethodTwo");
        proxyLog.someMethodThree(10, "StringOnMethodThree", 999);
    }
}
