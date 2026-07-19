package com.codeprometheus.aialgohelper.plugin.utils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.util.net.HttpConnectionUtils;
import com.codeprometheus.aialgohelper.plugin.model.HttpRequest;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.CookiePolicy;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author shuzijun
 */
public class HttpRequestUtils {

    private static final Cache<String, HttpResponse> httpResponseCache = CacheBuilder.newBuilder()
            .expireAfterWrite(30, TimeUnit.SECONDS)
            .build();
    private static final CookieManager cookieManager = new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER);
    private static final int CONNECT_TIMEOUT_MS = 30_000;
    private static final int READ_TIMEOUT_MS = 60_000;
    private static final int MAX_REDIRECTS = 5;

    private static Map<String, String> getHeader(String url) {
        URI uri = URI.create(url);
        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", "Mozilla/5.0 (compatible; AI Algo Helper/8.17.0)");
        headers.put("Accept", "*/*");
        headers.put("Accept-Language", Locale.getDefault().toLanguageTag());
        headers.put("Origin", uri.getScheme() + "://" + uri.getAuthority());

        for (HttpCookie cookie : cookieManager.getCookieStore().get(uri)) {
            if ("csrftoken".equals(cookie.getName())) {
                headers.put("x-csrftoken", cookie.getValue());
                break;
            }
        }
        return headers;
    }

    @NotNull
    public static HttpResponse executeGet(HttpRequest httpRequest) {
        return CacheProcessor.processor(httpRequest, request -> execute(request, "GET"));
    }

    @NotNull
    public static HttpResponse executePost(HttpRequest httpRequest) {
        return CacheProcessor.processor(httpRequest, request -> execute(request, "POST"));
    }

    public static HttpResponse executePut(HttpRequest httpRequest) {
        return CacheProcessor.processor(httpRequest, request -> execute(request, "PUT"));
    }

    public static String getToken() {
        Map<String, String> headerMap = getHeader(URLUtils.getLeetcodeHost());
        return headerMap.get("x-csrftoken");
    }

    public static boolean isLogin(Project project) {
        HttpResponse response = HttpRequest.builderGet(URLUtils.getLeetcodePoints()).request();
        if (response.getStatusCode() == 200) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public static void setCookie(List<HttpCookie> cookieList) {
        cookieManager.getCookieStore().removeAll();
        URI uri = URI.create(URLUtils.getLeetcodeUrl());
        for (HttpCookie cookie : cookieList) {
            if (cookie.getDomain() == null || cookie.getDomain().isBlank()) {
                cookie.setDomain(uri.getHost());
            }
            if (cookie.getPath() == null || cookie.getPath().isBlank()) {
                cookie.setPath("/");
            }
            cookieManager.getCookieStore().add(uri, cookie);
        }
    }

    public static void resetHttpclient() {
        cookieManager.getCookieStore().removeAll();
    }

    private static HttpResponse execute(HttpRequest request, String initialMethod) {
        HttpResponse response = new HttpResponse();
        response.setUrl(request.getUrl());

        URI currentUri = URI.create(request.getUrl());
        String method = initialMethod;
        String body = request.getBody();

        for (int redirectCount = 0; redirectCount <= MAX_REDIRECTS; redirectCount++) {
            HttpURLConnection connection = null;
            try {
                connection = HttpConnectionUtils.openHttpConnection(currentUri.toString());
                connection.setConnectTimeout(CONNECT_TIMEOUT_MS);
                connection.setReadTimeout(READ_TIMEOUT_MS);
                connection.setInstanceFollowRedirects(false);
                connection.setRequestMethod(method);

                Map<String, String> headers = getHeader(currentUri.toString());
                if (request.getHeader() != null) {
                    headers.putAll(request.getHeader());
                }
                headers.putIfAbsent("Referer", currentUri.toString());
                if (request.getContentType() != null && !request.getContentType().isBlank()) {
                    headers.putIfAbsent("Content-Type", request.getContentType());
                }
                headers.forEach(connection::setRequestProperty);

                Map<String, List<String>> cookieHeaders = cookieManager.get(currentUri, Map.of());
                for (Map.Entry<String, List<String>> cookieHeader : cookieHeaders.entrySet()) {
                    connection.setRequestProperty(
                            cookieHeader.getKey(),
                            String.join("; ", cookieHeader.getValue())
                    );
                }

                if (!"GET".equals(method) && body != null) {
                    connection.setDoOutput(true);
                    try (OutputStream outputStream = connection.getOutputStream()) {
                        outputStream.write(body.getBytes(StandardCharsets.UTF_8));
                    }
                }

                int statusCode = connection.getResponseCode();
                cookieManager.put(currentUri, connection.getHeaderFields());

                if (isRedirect(statusCode)) {
                    String location = connection.getHeaderField("Location");
                    if (location != null && redirectCount < MAX_REDIRECTS) {
                        currentUri = currentUri.resolve(location);
                        if (statusCode == HttpURLConnection.HTTP_SEE_OTHER ||
                                ((statusCode == HttpURLConnection.HTTP_MOVED_PERM ||
                                        statusCode == HttpURLConnection.HTTP_MOVED_TEMP) &&
                                        "POST".equals(method))) {
                            method = "GET";
                            body = null;
                        }
                        continue;
                    }
                }

                response.setStatusCode(statusCode);
                response.setBody(readBody(connection, statusCode));
                return response;
            } catch (IOException | IllegalArgumentException e) {
                LogUtils.LOG.error("HTTP request failed: " + currentUri, e);
                response.setStatusCode(-1);
                response.setBody("");
                return response;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }

        response.setStatusCode(-1);
        response.setBody("");
        return response;
    }

    private static boolean isRedirect(int statusCode) {
        return statusCode == HttpURLConnection.HTTP_MOVED_PERM ||
                statusCode == HttpURLConnection.HTTP_MOVED_TEMP ||
                statusCode == HttpURLConnection.HTTP_SEE_OTHER ||
                statusCode == 307 ||
                statusCode == 308;
    }

    private static String readBody(HttpURLConnection connection, int statusCode) throws IOException {
        InputStream inputStream = statusCode >= HttpURLConnection.HTTP_BAD_REQUEST
                ? connection.getErrorStream()
                : connection.getInputStream();
        if (inputStream == null) {
            return "";
        }
        try (inputStream) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }


    private static class CacheProcessor {
        public static HttpResponse processor(HttpRequest httpRequest, HttpRequestUtils.Callable<HttpResponse> callable) {

            String key = httpRequest.hashCode() + "";
            if (httpRequest.isCache() && httpResponseCache.getIfPresent(key) != null) {
                return httpResponseCache.getIfPresent(key);
            }
            if (httpRequest.isCache()) {
                synchronized (key.intern()) {
                    if (httpResponseCache.getIfPresent(key) != null) {
                        return httpResponseCache.getIfPresent(key);
                    } else {
                        HttpResponse httpResponse = callable.call(httpRequest);
                        if (httpResponse.getStatusCode() == 200) {
                            httpResponseCache.put(key, httpResponse);
                        }
                        return httpResponse;
                    }
                }
            } else {
                return callable.call(httpRequest);

            }
        }
    }

    @FunctionalInterface
    private interface Callable<V> {
        V call(HttpRequest request);
    }


}
