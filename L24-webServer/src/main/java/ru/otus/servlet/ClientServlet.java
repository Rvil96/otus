package ru.otus.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import ru.otus.dto.ClientDtoRq;
import ru.otus.dto.ClientDtoRs;
import ru.otus.exception.ClientsNotFoundException;
import ru.otus.mapper.Mapper;
import ru.otus.model.Client;
import ru.otus.repository.crm.service.DBServiceClient;
import ru.otus.services.TemplateProcessor;

@SuppressWarnings({"java:S1989"})
@Slf4j
public class ClientServlet extends HttpServlet {

    private static final String CLIENTS_PAGE_TEMPLATE = "clients.html";
    private static final String TEMPLATE_ATTR_CLIENT = "clients";

    private final transient DBServiceClient dbServiceClient;
    private final transient TemplateProcessor templateProcessor;
    private final transient Mapper<Client, ClientDtoRq, ClientDtoRs> mapper;

    public ClientServlet(
            TemplateProcessor templateProcessor,
            DBServiceClient dbServiceClient,
            Mapper<Client, ClientDtoRq, ClientDtoRs> mapper) {
        this.templateProcessor = templateProcessor;
        this.dbServiceClient = dbServiceClient;
        this.mapper = mapper;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();

        try {
            var clientsRs = Optional.ofNullable(dbServiceClient.findAll())
                    .map(clients -> clients.stream()
                            .filter(Objects::nonNull)
                            .map(mapper::toResponse)
                            .toList())
                    .orElseThrow(() -> new ClientsNotFoundException("no clients found"));

            paramsMap.put(TEMPLATE_ATTR_CLIENT, clientsRs);

            response.setContentType("text/html");
            response.getWriter().println(templateProcessor.getPage(CLIENTS_PAGE_TEMPLATE, paramsMap));
        } catch (ClientsNotFoundException e) {
            log.info(e.getMessage());
        }
    }
}
