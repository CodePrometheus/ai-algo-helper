package com.codeprometheus.aialgohelper.plugin.window.login;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.util.Disposer;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextArea;
import com.intellij.ui.jcef.JBCefCookie;
import com.intellij.ui.jcef.JCEFHtmlPanel;
import com.intellij.util.Alarm;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.components.BorderLayoutPanel;
import com.codeprometheus.aialgohelper.plugin.model.PluginConstant;
import com.codeprometheus.aialgohelper.plugin.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.handler.CefLoadHandler;
import org.cef.handler.CefLoadHandlerAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author shuzijun
 */
public class LoginPanel extends DialogWrapper {

    private BorderLayoutPanel panel = JBUI.Panels.simplePanel();

    private JTextArea cookieText = new JBTextArea();

    private JcefPanel jcefPanel;

    private Project project;

    private Action okAction;

    public LoginPanel(@Nullable Project project) {
        super(project, null, false, IdeModalityType.IDE, !HttpLogin.isEnabledJcef());
        this.project = project;
        if (HttpLogin.isEnabledJcef()) {
            okAction = new OkAction() {
            };
            try {
                jcefPanel = new JcefPanel(project, okAction);
            } catch (IllegalArgumentException e) {
                jcefPanel = new JcefPanel(project, okAction,true);
            }
            Disposer.register(getDisposable(),jcefPanel);
            jcefPanel.getComponent().setMinimumSize(new Dimension(1000, 500));
            jcefPanel.getComponent().setPreferredSize(new Dimension(1000, 500));
            panel.addToCenter(new JBScrollPane(jcefPanel.getComponent(), JBScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JBScrollPane.HORIZONTAL_SCROLLBAR_NEVER));

        } else {
            cookieText.setLineWrap(true);
            cookieText.setMinimumSize(new Dimension(400, 200));
            cookieText.setPreferredSize(new Dimension(400, 200));
            panel.addToCenter(new JBScrollPane(cookieText, JBScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JBScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
            okAction = new OkAction() {
                @Override
                protected void doAction(ActionEvent e) {
                    String cookiesString = cookieText.getText();
                    if (StringUtils.isBlank(cookiesString)) {
                        JOptionPane.showMessageDialog(null, "cookie is null");
                        return;
                    }
                    final List<HttpCookie> cookieList = new ArrayList<>();
                    String[] cookies = cookiesString.split(";");
                    for (String cookieString : cookies) {
                        String[] cookie = cookieString.trim().split("=");
                        if (cookie.length >= 2) {
                            try {
                                HttpCookie basicClientCookie = new HttpCookie(cookie[0], cookie[1]);
                                basicClientCookie.setDomain("." + URLUtils.getLeetcodeHost());
                                basicClientCookie.setPath("/");
                                cookieList.add(basicClientCookie);
                            } catch (IllegalArgumentException ignore) {

                            }
                        }
                    }
                    HttpRequestUtils.setCookie(cookieList);

                    ProgressManager.getInstance().run(new Task.Backgroundable(project, PluginConstant.ACTION_PREFIX + ".loginSuccess", false) {
                        @Override
                        public void run(@NotNull ProgressIndicator progressIndicator) {
                            if (HttpRequestUtils.isLogin(project)) {
                                HttpLogin.loginSuccess(project, cookieList);
                            } else {
                                JOptionPane.showMessageDialog(null, PropertiesUtils.getInfo("login.failed"));
                            }

                        }
                    });
                    super.doAction(e);
                }
            };
            okAction.putValue(Action.NAME, "login");
        }

        setModal(false);
        init();
        setTitle("login");
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return panel;
    }

    @NotNull
    @Override
    protected Action getOKAction() {
        return okAction;
    }

    @NotNull
    @Override
    protected Action[] createActions() {
        Action helpAction = new AbstractAction("help") {
            @Override
            public void actionPerformed(ActionEvent e) {
                BrowserUtil.browse("https://github.com/CodePrometheus/ai-algo-helper/blob/main/doc/LoginHelp.md");
            }

        };
        Action[] actions = new Action[]{helpAction, this.getOKAction(), this.getCancelAction()};
        return actions;
    }




    private static class JcefPanel extends JCEFHtmlPanel {

        private static final int COOKIE_POLL_INTERVAL_MS = 500;
        private static final int COOKIE_READ_TIMEOUT_SECONDS = 5;
        private static final int FAILED_VALIDATION_RETRY_MS = 5_000;

        private CefLoadHandlerAdapter cefLoadHandler;
        private Project project;
        private Action okAction;
        private String host;
        private String loginUrl;
        private Alarm cookiePollAlarm;
        private final AtomicBoolean disposed = new AtomicBoolean();
        private final AtomicBoolean loginSucceeded = new AtomicBoolean();
        private String lastValidatedSession;
        private long lastValidationAt;

        public JcefPanel(Project project, Action okAction, boolean old) {
            super( null);
            initialize(project, okAction);
        }

        public JcefPanel(Project project, Action okAction) {
            super(null, null);
            initialize(project, okAction);
        }

        private void initialize(Project project, Action okAction) {
            this.project = project;
            this.okAction = okAction;
            this.host = URLUtils.getLeetcodeHost();
            this.loginUrl = URLUtils.getLeetcodeLogin();
            this.cookiePollAlarm = new Alarm(Alarm.ThreadToUse.POOLED_THREAD, this);
            init();
        }

        private void init(){
            getJBCefClient().addLoadHandler(cefLoadHandler = new CefLoadHandlerAdapter() {
                @Override
                public void onLoadError(CefBrowser browser, CefFrame frame, CefLoadHandler.ErrorCode errorCode, String errorText, String failedUrl) {
                    boolean mainFrame = frame != null && frame.isMain();
                    if (loginSucceeded.get() ||
                            !LoginSessionSupport.shouldReportLoadError(mainFrame, errorCode)) {
                        return;
                    }
                    LogUtils.LOG.warn("JCEF login page failed to load: code=" + errorCode +
                            ", error=" + errorText +
                            ", origin=" + LoginSessionSupport.safeOrigin(failedUrl));
                    ApplicationManager.getApplication().invokeLater(() -> {
                        if (!disposed.get() && !loginSucceeded.get()) {
                            MessageUtils.getInstance(project).showWarnMsg("", "The page failed to load, please check the network and open it again");
                        }
                    });
                }
            }, getCefBrowser());
            loadURL(loginUrl);
            scheduleCookiePoll(0);
        }

        private void scheduleCookiePoll(int delayMs) {
            if (disposed.get() || loginSucceeded.get() || cookiePollAlarm.isDisposed()) {
                return;
            }
            cookiePollAlarm.addRequest(this::pollForSession, delayMs);
        }

        private void pollForSession() {
            if (disposed.get() || loginSucceeded.get()) {
                return;
            }
            try {
                List<JBCefCookie> cefCookies = getJBCefCookieManager()
                        .getCookies(loginUrl, true)
                        .get(COOKIE_READ_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                List<HttpCookie> cookies = LoginSessionSupport.toHttpCookies(cefCookies, host);
                String session = LoginSessionSupport.findSessionValue(cookies);
                if (session == null) {
                    scheduleCookiePoll(COOKIE_POLL_INTERVAL_MS);
                    return;
                }

                long now = System.currentTimeMillis();
                if (session.equals(lastValidatedSession) &&
                        now - lastValidationAt < FAILED_VALIDATION_RETRY_MS) {
                    scheduleCookiePoll(COOKIE_POLL_INTERVAL_MS);
                    return;
                }
                lastValidatedSession = session;
                lastValidationAt = now;

                if (!host.equals(URLUtils.getLeetcodeHost())) {
                    LogUtils.LOG.info("Selected LeetCode host changed while the login dialog was open");
                    scheduleCookiePoll(COOKIE_POLL_INTERVAL_MS);
                    return;
                }

                HttpRequestUtils.setCookie(cookies);
                if (!HttpRequestUtils.isLogin(project)) {
                    HttpRequestUtils.resetHttpclient();
                    LogUtils.LOG.info("Web session validation failed for host " + host);
                    scheduleCookiePoll(COOKIE_POLL_INTERVAL_MS);
                    return;
                }

                if (loginSucceeded.compareAndSet(false, true)) {
                    HttpLogin.loginSuccess(project, host, cookies);
                    ApplicationManager.getApplication().invokeLater(() -> {
                        if (!disposed.get()) {
                            okAction.actionPerformed(null);
                        }
                    });
                }
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
            } catch (ExecutionException | TimeoutException exception) {
                if (!disposed.get()) {
                    LogUtils.LOG.warn("Failed to read JCEF login cookies for host " + host, exception);
                    scheduleCookiePoll(COOKIE_POLL_INTERVAL_MS);
                }
            } catch (RuntimeException exception) {
                if (!disposed.get()) {
                    LogUtils.LOG.warn("Unexpected error while importing the JCEF login session for host " + host, exception);
                    scheduleCookiePoll(COOKIE_POLL_INTERVAL_MS);
                }
            }
        }

        @Override
        public void dispose() {
            disposed.set(true);
            cookiePollAlarm.cancelAllRequests();
            getJBCefClient().removeLoadHandler(cefLoadHandler, getCefBrowser());
            getJBCefBrowser(getCefBrowser()).getJBCefCookieManager().deleteCookies(loginUrl, null);
            super.dispose();
        }
    }
}
