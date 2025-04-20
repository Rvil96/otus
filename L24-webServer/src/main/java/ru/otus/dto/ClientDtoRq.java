package ru.otus.dto;

public record ClientDtoRq(String name, String login, String password, PhoneDto phone, AddressDto address) {}
