package com.codeprometheus.aialgohelper.plugin.actions.editor;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.codeprometheus.aialgohelper.plugin.model.Config;
import com.codeprometheus.aialgohelper.plugin.model.Question;
import com.codeprometheus.aialgohelper.plugin.utils.URLUtils;

/**
 * @author zzdcon
 */
public class OpenInWebAction extends AbstractEditAction {

    @Override
    public void actionPerformed(AnActionEvent anActionEvent, Config config, Question question) {
        BrowserUtil.browse(URLUtils.getLeetcodeProblems() + question.getTitleSlug());
    }
}
