package com.codeprometheus.aialgohelper.plugin.setting;

import com.intellij.credentialStore.CredentialAttributes;
import com.intellij.credentialStore.Credentials;
import com.intellij.ide.passwordSafe.PasswordSafe;
import com.codeprometheus.aialgohelper.plugin.model.PluginConstant;
import org.apache.commons.lang3.StringUtils;

public final class SessionCookieStore {

    private static final String SERVICE_NAME = PluginConstant.PLUGIN_ID + ".session";

    private SessionCookieStore() {
    }

    public static String load(String host) {
        Credentials credentials = PasswordSafe.getInstance().get(attributes(host));
        return credentials == null ? null : credentials.getPasswordAsString();
    }

    public static void save(String host, String cookies) {
        CredentialAttributes attributes = attributes(host);
        if (StringUtils.isBlank(cookies)) {
            PasswordSafe.getInstance().set(attributes, null);
        } else {
            PasswordSafe.getInstance().set(attributes, new Credentials(host, cookies));
        }
    }

    public static void clear(String host) {
        PasswordSafe.getInstance().set(attributes(host), null);
    }

    private static CredentialAttributes attributes(String host) {
        return new CredentialAttributes(SERVICE_NAME, host);
    }
}
