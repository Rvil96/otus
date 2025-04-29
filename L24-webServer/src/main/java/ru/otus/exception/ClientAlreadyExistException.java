package ru.otus.exception;

public class ClientAlreadyExistException extends RuntimeException {
    public ClientAlreadyExistException(String message) {
        super(message);
    }
}
