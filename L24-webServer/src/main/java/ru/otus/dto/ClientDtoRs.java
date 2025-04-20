package ru.otus.dto;

public record ClientDtoRs(Long id, String name, String login, PhoneDto phone, AddressDto address) {}
