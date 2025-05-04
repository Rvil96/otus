package ru.otus.crm.dto;

import java.util.Set;

public record ClientDtoRs(Long id, String name, Set<PhoneDto> phones, AddressDto address) {}
