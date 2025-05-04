package ru.otus.mapper;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.otus.crm.dto.AddressDto;
import ru.otus.crm.dto.ClientDtoRq;
import ru.otus.crm.dto.ClientDtoRs;
import ru.otus.crm.dto.PhoneDto;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;

@Component
@Slf4j
public class ClientMapperImpl implements ClientMapper {

    @Override
    public Client toEntity(ClientDtoRq clientDto) {
        return Optional.ofNullable(clientDto)
                .map(clientDtoRq -> new Client(
                        null,
                        clientDtoRq.name(),
                        mapAddressToEntity(clientDtoRq.address()),
                        mapPhonesToEntity(clientDtoRq.phones())))
                .orElseThrow(() -> new RuntimeException("Client is null"));
    }

    @Override
    public ClientDtoRs toResponse(Client clientEntity) {
        return Optional.ofNullable(clientEntity)
                .map(client -> new ClientDtoRs(
                        client.getId(),
                        client.getName(),
                        mapPhoneToDto(client.getPhones()),
                        mapAddressToDto(client.getAddress())))
                .orElseThrow(() -> new RuntimeException("Client is null"));
    }

    private Address mapAddressToEntity(AddressDto addressDto) {
        return Optional.ofNullable(addressDto)
                .map(addressDtoRq -> new Address(null, addressDtoRq.street(), null))
                .orElse(null);
    }

    private Set<Phone> mapPhonesToEntity(Set<PhoneDto> phones) {
        return phones.stream()
                .filter(Objects::nonNull)
                .map(phone -> new Phone(null, phone.number(), null))
                .collect(Collectors.toSet());
    }

    private Set<PhoneDto> mapPhoneToDto(Set<Phone> phones) {
        return phones.stream()
                .filter(Objects::nonNull)
                .map(phone -> new PhoneDto(phone.getNumber()))
                .collect(Collectors.toSet());
    }

    private AddressDto mapAddressToDto(Address address) {
        return Optional.ofNullable(address)
                .map(addressRq -> new AddressDto(addressRq.getStreet()))
                .orElse(null);
    }
}
