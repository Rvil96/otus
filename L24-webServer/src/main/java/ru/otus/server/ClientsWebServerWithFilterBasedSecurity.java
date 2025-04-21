package ru.otus.server;

import com.google.gson.Gson;
import java.util.Arrays;
import org.eclipse.jetty.ee10.servlet.FilterHolder;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Handler;
import ru.otus.dto.ClientDtoRq;
import ru.otus.dto.ClientDtoRs;
import ru.otus.mapper.Mapper;
import ru.otus.model.Client;
import ru.otus.repository.crm.service.DBServiceClient;
import ru.otus.services.ClientAuthService;
import ru.otus.services.TemplateProcessor;
import ru.otus.servlet.AuthorizationFilter;
import ru.otus.servlet.LoginServlet;

public class ClientsWebServerWithFilterBasedSecurity extends ClientsWebServerSimple {
    private final ClientAuthService authService;

    public ClientsWebServerWithFilterBasedSecurity(
            int port,
            ClientAuthService authService,
            DBServiceClient dbServiceClient,
            Gson gson,
            TemplateProcessor templateProcessor,
            Mapper<Client, ClientDtoRq, ClientDtoRs> mapper) {
        super(port, dbServiceClient, gson, templateProcessor, mapper);
        this.authService = authService;
    }

    @Override
    protected Handler applySecurity(ServletContextHandler servletContextHandler, String... paths) {
        servletContextHandler.addServlet(new ServletHolder(new LoginServlet(templateProcessor, authService)), "/login");
        AuthorizationFilter authorizationFilter = new AuthorizationFilter();
        Arrays.stream(paths)
                .forEachOrdered(
                        path -> servletContextHandler.addFilter(new FilterHolder(authorizationFilter), path, null));
        return servletContextHandler;
    }
}
