package com.codeprometheus.aialgohelper;

import com.codeprometheus.aialgohelper.plugin.model.HttpRequest;
import com.codeprometheus.aialgohelper.plugin.utils.HttpRequestUtils;
import com.codeprometheus.aialgohelper.plugin.utils.HttpResponse;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

public class HttpRequestUtilsTest {

    private HttpServer server;
    private String baseUrl;

    @Before
    public void setUp() throws IOException {
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/get", exchange -> respond(exchange, 200, "ok"));
        server.createContext("/redirect", exchange -> {
            exchange.getResponseHeaders().add("Set-Cookie", "csrftoken=test-token; Path=/");
            exchange.getResponseHeaders().add("Location", "/cookie");
            exchange.sendResponseHeaders(302, -1);
            exchange.close();
        });
        server.createContext("/cookie", exchange -> {
            String cookie = exchange.getRequestHeaders().getFirst("Cookie");
            String csrfToken = exchange.getRequestHeaders().getFirst("x-csrftoken");
            respond(exchange, 200, cookie + "|" + csrfToken);
        });
        server.createContext("/post", exchange -> {
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            String contentType = exchange.getRequestHeaders().getFirst("Content-Type");
            respond(exchange, 200, exchange.getRequestMethod() + "|" + contentType + "|" + body);
        });
        server.start();
        baseUrl = "http://127.0.0.1:" + server.getAddress().getPort();
        HttpRequestUtils.resetHttpclient();
    }

    @After
    public void tearDown() {
        HttpRequestUtils.resetHttpclient();
        server.stop(0);
    }

    @Test
    public void executesGet() {
        HttpResponse response = HttpRequest.builderGet(baseUrl + "/get").request();

        assertEquals(200, response.getStatusCode());
        assertEquals("ok", response.getBody());
    }

    @Test
    public void carriesCookiesAcrossRedirects() {
        HttpResponse response = HttpRequest.builderGet(baseUrl + "/redirect").request();

        assertEquals(200, response.getStatusCode());
        assertEquals("csrftoken=test-token|test-token", response.getBody());
    }

    @Test
    public void sendsPostBodyAndContentType() {
        HttpResponse response = HttpRequest.builderPost(baseUrl + "/post", "application/json")
                .body("{\"answer\":42}")
                .request();

        assertEquals(200, response.getStatusCode());
        assertEquals("POST|application/json|{\"answer\":42}", response.getBody());
    }

    private static void respond(HttpExchange exchange, int statusCode, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, bytes.length);
        exchange.getResponseBody().write(bytes);
        exchange.close();
    }
}
