package ru.otus.servlet;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import ru.otus.dto.ClientDtoRq;
import ru.otus.dto.ClientDtoRs;
import ru.otus.exception.ClientAlreadyExistException;
import ru.otus.mapper.Mapper;
import ru.otus.model.Client;
import ru.otus.repository.crm.service.DBServiceClient;

@SuppressWarnings({"java:S1989"})
@Slf4j
public class ClientApiServlet extends HttpServlet {

    private static final int ID_PATH_PARAM_POSITION = 1;

    private final transient DBServiceClient dbServiceClient;
    private final transient Gson gson;
    private final transient Mapper<Client, ClientDtoRq, ClientDtoRs> mapper;

    public ClientApiServlet(
            DBServiceClient dbServiceClient, Gson gson, Mapper<Client, ClientDtoRq, ClientDtoRs> mapper) {
        this.dbServiceClient = dbServiceClient;
        this.gson = gson;
        this.mapper = mapper;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var bodyReader = req.getReader();
        ClientDtoRq clientDto = gson.fromJson(bodyReader, ClientDtoRq.class);
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");

        try {
            if (dbServiceClient.getClientByLogin(clientDto.login()).isPresent()) {
                throw new ClientAlreadyExistException("Client with " + clientDto.login() + " login exist.");
            }

            var client = mapper.toEntity(clientDto);

            dbServiceClient.saveClient(client);

            log.info("Client: {}", client);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("Client created");
        } catch (ClientAlreadyExistException e) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getWriter().write(e.getMessage());
            log.info(e.getMessage());
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(e.getMessage());
        }
    }
}
