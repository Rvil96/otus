package ru.otus.exception;

public class ClientsNotFoundException extends RuntimeException {
    public ClientsNotFoundException(String message) {
        super(message);
    }
}
