package ru.otus.model;

import java.time.LocalDateTime;

public class TimeHolder {
    private final LocalDateTime localDateTime;

    public TimeHolder(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }
}
