package ru.otus.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.crm.dto.ClientDtoRq;
import ru.otus.crm.dto.ClientDtoRs;
import ru.otus.service.ClientService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @PostMapping("/client")
    public ResponseEntity<Void> createClient(@RequestBody ClientDtoRq client) {
        try {
            clientService.createClient(client);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/clients")
    public ResponseEntity<List<ClientDtoRs>> findAll() {
        try {
            var clientList = clientService.findAll();
            return ResponseEntity.ok(clientList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
