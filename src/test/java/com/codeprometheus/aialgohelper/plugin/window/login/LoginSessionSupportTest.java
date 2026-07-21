package com.codeprometheus.aialgohelper.plugin.window.login;

import com.intellij.ui.jcef.JBCefCookie;
import org.cef.handler.CefLoadHandler;
import org.junit.Test;

import java.net.HttpCookie;
import java.util.List;

import static org.junit.Assert.*;

public class LoginSessionSupportTest {

    @Test
    public void ignoresAbortedLoadsAndSubframes() {
        assertFalse(LoginSessionSupport.shouldReportLoadError(
                true,
                CefLoadHandler.ErrorCode.ERR_ABORTED
        ));
        assertFalse(LoginSessionSupport.shouldReportLoadError(
                false,
                CefLoadHandler.ErrorCode.ERR_CONNECTION_FAILED
        ));
        assertTrue(LoginSessionSupport.shouldReportLoadError(
                true,
                CefLoadHandler.ErrorCode.ERR_CONNECTION_FAILED
        ));
    }

    @Test
    public void stripsSensitiveUrlDetailsFromLogs() {
        assertEquals(
                "https://leetcode.cn",
                LoginSessionSupport.safeOrigin("https://leetcode.cn/accounts/callback/?token=secret#fragment")
        );
        assertEquals("<invalid-url>", LoginSessionSupport.safeOrigin("not a url"));
    }

    @Test
    public void importsOnlyCookiesForTheSelectedHost() {
        List<JBCefCookie> cefCookies = List.of(
                new JBCefCookie("LEETCODE_SESSION", "cn-session", ".leetcode.cn", "/", true, true),
                new JBCefCookie("csrftoken", "cn-csrf", "leetcode.cn", "/", true, false),
                new JBCefCookie("LEETCODE_SESSION", "com-session", ".leetcode.com", "/", true, true)
        );

        List<HttpCookie> cookies = LoginSessionSupport.toHttpCookies(cefCookies, "leetcode.cn");

        assertEquals(2, cookies.size());
        assertEquals("cn-session", LoginSessionSupport.findSessionValue(cookies));
        assertTrue(cookies.stream().allMatch(HttpCookie::getSecure));
        assertTrue(cookies.stream().noneMatch(cookie -> ".leetcode.com".equals(cookie.getDomain())));
    }
}
