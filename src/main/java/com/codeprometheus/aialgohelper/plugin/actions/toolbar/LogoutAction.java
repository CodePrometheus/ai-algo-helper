package com.codeprometheus.aialgohelper.plugin.actions.toolbar;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.DumbAware;
import com.codeprometheus.aialgohelper.plugin.actions.AbstractAction;
import com.codeprometheus.aialgohelper.plugin.listener.LoginNotifier;
import com.codeprometheus.aialgohelper.plugin.model.Config;
import com.codeprometheus.aialgohelper.plugin.model.HttpRequest;
import com.codeprometheus.aialgohelper.plugin.utils.*;
import com.codeprometheus.aialgohelper.plugin.window.NavigatorTabsPanel;

/**
 * @author shuzijun
 */
public class LogoutAction extends AbstractAction implements DumbAware {
    @Override
    public void actionPerformed(AnActionEvent anActionEvent, Config config) {

        HttpResponse httpResponse = HttpRequest.builderGet(URLUtils.getLeetcodeLogout()).request();
        HttpRequestUtils.resetHttpclient();
        MessageUtils.getInstance(anActionEvent.getProject()).showInfoMsg("info", PropertiesUtils.getInfo("login.out"));
        NavigatorTabsPanel.loadUser(false);
        ApplicationManager.getApplication().getMessageBus().syncPublisher(LoginNotifier.TOPIC).logout(anActionEvent.getProject(), config.getUrl());
    }
}
