package com.codeprometheus.aialgohelper.plugin.actions.toolbar;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.DumbAware;
import com.codeprometheus.aialgohelper.plugin.actions.AbstractAction;
import com.codeprometheus.aialgohelper.plugin.model.Config;
import com.codeprometheus.aialgohelper.plugin.utils.DataKeys;
import com.codeprometheus.aialgohelper.plugin.window.NavigatorTabsPanel;
import com.codeprometheus.aialgohelper.plugin.window.WindowFactory;

/**
 * @author shuzijun
 */
public class ToggleListAction extends AbstractAction implements DumbAware {
    @Override
    public void actionPerformed(AnActionEvent anActionEvent, Config config) {
        NavigatorTabsPanel navigatorTabs = WindowFactory.getDataContext(anActionEvent.getProject()).getData(DataKeys.LEETCODE_PROJECTS_TABS);
        if (navigatorTabs == null) {
            return;
        }
        ApplicationManager.getApplication().invokeLater(() -> navigatorTabs.toggle());

    }
}
