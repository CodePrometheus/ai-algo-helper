package com.codeprometheus.aialgohelper.plugin.actions.toolbar;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.DumbAware;
import com.codeprometheus.aialgohelper.plugin.actions.AbstractAction;
import com.codeprometheus.aialgohelper.plugin.listener.LoginNotifier;
import com.codeprometheus.aialgohelper.plugin.model.Config;
import com.codeprometheus.aialgohelper.plugin.setting.SessionCookieStore;
import com.codeprometheus.aialgohelper.plugin.utils.*;
import com.codeprometheus.aialgohelper.plugin.window.NavigatorTabsPanel;
import com.codeprometheus.aialgohelper.plugin.window.login.LoginPanel;
import org.apache.commons.lang3.StringUtils;

import java.net.HttpCookie;
import java.util.List;

/**
 * @author shuzijun
 */
public class LoginAction extends AbstractAction implements DumbAware {

    @Override
    protected boolean requiresConfiguration() {
        return false;
    }

    @Override
    public synchronized void actionPerformed(AnActionEvent anActionEvent, Config config) {
        String host = URLUtils.getLeetcodeHost();
        if (StringUtils.isNotBlank(HttpRequestUtils.getToken()) && HttpRequestUtils.isLogin(anActionEvent.getProject())) {
            MessageUtils.getInstance(anActionEvent.getProject()).showWarnMsg("info", PropertiesUtils.getInfo("login.exist"));
            NavigatorTabsPanel.loadUser(true);
            ApplicationManager.getApplication().getMessageBus().syncPublisher(LoginNotifier.TOPIC).login(anActionEvent.getProject(), host);
            return;
        }

        String sessionCookies = SessionCookieStore.load(host);
        if (StringUtils.isNotBlank(sessionCookies)) {
            try {
                List<HttpCookie> cookieList = CookieUtils.toHttpCookie(sessionCookies);
                HttpRequestUtils.setCookie(cookieList);
                if (HttpRequestUtils.isLogin(anActionEvent.getProject())) {
                    MessageUtils.getInstance(anActionEvent.getProject()).showInfoMsg("login", PropertiesUtils.getInfo("login.success"));
                    NavigatorTabsPanel.loadUser(true);
                    ApplicationManager.getApplication().getMessageBus().syncPublisher(LoginNotifier.TOPIC).login(anActionEvent.getProject(), host);
                    return;
                }
            } catch (RuntimeException exception) {
                LogUtils.LOG.warn("Failed to restore the saved web session", exception);
            }
            HttpRequestUtils.resetHttpclient();
            SessionCookieStore.clear(host);
        }

        ApplicationManager.getApplication().invokeLater(() -> new LoginPanel(anActionEvent.getProject()).show());

    }


}
