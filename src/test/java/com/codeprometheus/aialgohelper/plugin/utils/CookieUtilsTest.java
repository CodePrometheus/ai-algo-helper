package com.codeprometheus.aialgohelper.plugin.utils;

import org.junit.Test;

import java.net.HttpCookie;
import java.util.List;

import static org.junit.Assert.*;

public class CookieUtilsTest {

    @Test
    public void preservesSecureSessionCookiesWithoutInventingAnExpiry() {
        HttpCookie original = new HttpCookie("LEETCODE_SESSION", "session");
        original.setDomain(".leetcode.cn");
        original.setPath("/");
        original.setSecure(true);

        List<HttpCookie> restored = CookieUtils.toHttpCookie(
                CookieUtils.httpCookieToJSONString(List.of(original))
        );

        assertEquals(1, restored.size());
        assertEquals("LEETCODE_SESSION", restored.get(0).getName());
        assertEquals(".leetcode.cn", restored.get(0).getDomain());
        assertEquals("/", restored.get(0).getPath());
        assertTrue(restored.get(0).getSecure());
        assertEquals(-1, restored.get(0).getMaxAge());
    }
}
