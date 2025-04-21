package ru.otus.mapper;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import ru.otus.dto.AddressDto;
import ru.otus.dto.ClientDtoRq;
import ru.otus.dto.ClientDtoRs;
import ru.otus.dto.PhoneDto;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;

public class ClientMapper implements Mapper<Client, ClientDtoRq, ClientDtoRs> {
    @Override
    public Client toEntity(ClientDtoRq clientDto) {
        if (clientDto == null) {
            throw new IllegalArgumentException("Clint dto is null");
        }

        var address =
                mapIfPresentAndNotEmpty(clientDto.address(), AddressDto::street, street -> new Address(null, street));
        var phones = mapIfPresentAndNotEmpty(
                clientDto.phone(), PhoneDto::number, number -> List.of(new Phone(null, number)));
        return new Client(null, clientDto.name(), clientDto.login(), clientDto.password(), address, phones);
    }

    @Override
    public ClientDtoRs toResponse(Client client) {
        if (client == null) {
            throw new IllegalArgumentException();
        }
        return new ClientDtoRs(
                client.getId(),
                client.getName(),
                client.getLogin(),
                mapPhones(client.getPhones()),
                mapAddress(client.getAddress()));
    }

    private AddressDto mapAddress(Address address) {
        return Optional.ofNullable(address)
                .map(addressEntity -> new AddressDto(addressEntity.getStreet()))
                .orElse(new AddressDto(""));
    }

    private PhoneDto mapPhones(List<Phone> phones) {
        return phones.stream()
                .filter(Objects::nonNull)
                .map(phone -> new PhoneDto(phone.getNumber()))
                .findFirst()
                .orElse(new PhoneDto(""));
    }

    private <T, R> R mapIfPresentAndNotEmpty(T input, Function<T, String> valueExtractor, Function<String, R> mapper) {
        return Optional.ofNullable(input)
                .map(valueExtractor)
                .filter(s -> !s.isEmpty())
                .map(mapper)
                .orElse(null);
    }
}
