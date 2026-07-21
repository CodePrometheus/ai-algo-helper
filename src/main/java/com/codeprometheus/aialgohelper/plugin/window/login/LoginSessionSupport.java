package com.codeprometheus.aialgohelper.plugin.window.login;

import com.intellij.ui.jcef.JBCefCookie;
import org.cef.handler.CefLoadHandler;

import java.net.HttpCookie;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

final class LoginSessionSupport {

    static final String SESSION_COOKIE_NAME = "LEETCODE_SESSION";

    private LoginSessionSupport() {
    }

    static boolean shouldReportLoadError(boolean mainFrame, CefLoadHandler.ErrorCode errorCode) {
        return mainFrame && errorCode != CefLoadHandler.ErrorCode.ERR_ABORTED;
    }

    static String safeOrigin(String url) {
        try {
            URI uri = URI.create(url);
            if (uri.getScheme() == null || uri.getHost() == null) {
                return "<redacted>";
            }
            int port = uri.getPort();
            return uri.getScheme() + "://" + uri.getHost() + (port < 0 ? "" : ":" + port);
        } catch (IllegalArgumentException exception) {
            return "<invalid-url>";
        }
    }

    static List<HttpCookie> toHttpCookies(List<JBCefCookie> cefCookies, String host) {
        List<HttpCookie> cookies = new ArrayList<>();
        for (JBCefCookie cefCookie : cefCookies) {
            if (!domainMatches(cefCookie.getDomain(), host)) {
                continue;
            }
            try {
                HttpCookie cookie = new HttpCookie(cefCookie.getName(), cefCookie.getValue());
                cookie.setDomain(isBlank(cefCookie.getDomain()) ? host : cefCookie.getDomain());
                cookie.setPath(isBlank(cefCookie.getPath()) ? "/" : cefCookie.getPath());
                cookie.setSecure(cefCookie.isSecure());
                cookies.add(cookie);
            } catch (IllegalArgumentException ignored) {
                // Ignore malformed browser cookies rather than aborting the login session import.
            }
        }
        return cookies;
    }

    static String findSessionValue(List<HttpCookie> cookies) {
        return cookies.stream()
                .filter(cookie -> SESSION_COOKIE_NAME.equals(cookie.getName()))
                .map(HttpCookie::getValue)
                .findFirst()
                .orElse(null);
    }

    private static boolean domainMatches(String cookieDomain, String host) {
        if (isBlank(cookieDomain) || isBlank(host)) {
            return false;
        }
        String normalizedDomain = cookieDomain.toLowerCase(Locale.ROOT);
        while (normalizedDomain.startsWith(".")) {
            normalizedDomain = normalizedDomain.substring(1);
        }
        String normalizedHost = host.toLowerCase(Locale.ROOT);
        return normalizedHost.equals(normalizedDomain) || normalizedHost.endsWith("." + normalizedDomain);
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
