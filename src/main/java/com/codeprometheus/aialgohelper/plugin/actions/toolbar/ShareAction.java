package com.codeprometheus.aialgohelper.plugin.actions.toolbar;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAware;
import com.codeprometheus.aialgohelper.plugin.actions.AbstractAction;
import com.codeprometheus.aialgohelper.plugin.model.Config;

/**
 * @author shuzijun
 */
public class ShareAction extends AbstractAction implements DumbAware {
    @Override
    public void actionPerformed(AnActionEvent anActionEvent, Config config) {
        BrowserUtil.browse("https://codetop.cc/?utm_source=leetcode_editor");
    }
}
