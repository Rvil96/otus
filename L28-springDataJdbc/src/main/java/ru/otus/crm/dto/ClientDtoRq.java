package ru.otus.crm.dto;

import java.util.Set;

public record ClientDtoRq(String name, Set<PhoneDto> phones, AddressDto address) {}
