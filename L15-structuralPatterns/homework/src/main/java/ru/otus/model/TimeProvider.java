package ru.otus.model;

import java.time.LocalDateTime;

public class TimeProvider {
    private final LocalDateTime localDateTime;

    public TimeProvider(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }
}
