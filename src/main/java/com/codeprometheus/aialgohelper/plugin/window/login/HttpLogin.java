package com.codeprometheus.aialgohelper.plugin.window.login;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.codeprometheus.aialgohelper.plugin.listener.LoginNotifier;
import com.codeprometheus.aialgohelper.plugin.model.PluginConstant;
import com.codeprometheus.aialgohelper.plugin.model.User;
import com.codeprometheus.aialgohelper.plugin.setting.SessionCookieStore;
import com.codeprometheus.aialgohelper.plugin.utils.*;
import com.codeprometheus.aialgohelper.plugin.window.NavigatorTabsPanel;
import com.codeprometheus.aialgohelper.plugin.window.WindowFactory;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpCookie;
import java.util.List;

/**
 * @author shuzijun
 */
public class HttpLogin {
    public static void examineEmail(Project project) {
        ApplicationManager.getApplication().executeOnPooledThread(new Runnable() {
            @Override
            public void run() {
                try {
                    User user = WindowFactory.getDataContext(project).getData(DataKeys.LEETCODE_PROJECTS_TABS).getUser();
                    if (user.isVerified() || user.isPhoneVerified()) {
                        return;
                    }
                    MessageUtils.getInstance(project).showWarnMsg("info", PropertiesUtils.getInfo("user.email"));
                } catch (Exception i) {
                    LogUtils.LOG.error("验证邮箱错误");
                }
            }
        });
    }

    public static void loginSuccess(Project project, List<HttpCookie> cookieList) {
        loginSuccess(project, URLUtils.getLeetcodeHost(), cookieList);
    }

    public static void loginSuccess(Project project, String host, List<HttpCookie> cookieList) {
        ProgressManager.getInstance().run(new Task.Backgroundable(project, PluginConstant.ACTION_PREFIX + ".loginSuccess", false) {
            @Override
            public void run(@NotNull ProgressIndicator progressIndicator) {
                SessionCookieStore.save(host, CookieUtils.httpCookieToJSONString(cookieList));
                MessageUtils.getInstance(project).showInfoMsg("info", PropertiesUtils.getInfo("login.success"));
                NavigatorTabsPanel.loadUser(true);
                ApplicationManager.getApplication().getMessageBus().syncPublisher(LoginNotifier.TOPIC).login(project, host);
                examineEmail(project);
            }
        });
    }

    public static boolean isEnabledJcef() {
        return isSupportedJcef();
    }

    public static boolean isSupportedJcef() {
        try {
            Class<?> JBCefAppClass = Class.forName("com.intellij.ui.jcef.JBCefApp");
            Method method = JBCefAppClass.getMethod("isSupported");
            return (boolean) method.invoke(null);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                 InvocationTargetException e) {
            return Boolean.FALSE;
        }
    }

}
