package ru.otus.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.annotation.Log;

public class LogProxyFactory implements InvocationHandler {
    private final Object target;
    private final Logger logger;

    public LogProxyFactory(Object target) {
        this.target = target;
        logger = LoggerFactory.getLogger(target.getClass());
    }

    public Object getProxy() {
        return Proxy.newProxyInstance(
                target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Method realMethod = target.getClass().getMethod(method.getName(), method.getParameterTypes());
        if (realMethod.isAnnotationPresent(Log.class)) {
            logger.info("Execute method: {} , params: {}", method.getName(), Arrays.toString(args));
        }
        return method.invoke(target, args);
    }
}
