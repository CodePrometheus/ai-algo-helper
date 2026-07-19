package com.codeprometheus.aialgohelper.plugin.actions.toolbar;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAware;
import com.codeprometheus.aialgohelper.plugin.actions.AbstractAction;
import com.codeprometheus.aialgohelper.plugin.manager.NavigatorAction;
import com.codeprometheus.aialgohelper.plugin.manager.ViewManager;
import com.codeprometheus.aialgohelper.plugin.model.Config;
import com.codeprometheus.aialgohelper.plugin.utils.DataKeys;
import com.codeprometheus.aialgohelper.plugin.window.WindowFactory;

/**
 * @author shuzijun
 */
public class PickAction extends AbstractAction implements DumbAware {
    private int i = 0;

    @Override
    public void actionPerformed(AnActionEvent anActionEvent, Config config) {
        NavigatorAction navigatorAction = WindowFactory.getDataContext(anActionEvent.getProject()).getData(DataKeys.LEETCODE_PROJECTS_NAVIGATORACTION);
        ViewManager.pick(anActionEvent.getProject(), navigatorAction.getPageInfo());
    }
}
