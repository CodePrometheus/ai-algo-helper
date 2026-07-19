package com.codeprometheus.aialgohelper.plugin.actions.editor;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.codeprometheus.aialgohelper.plugin.manager.CodeManager;
import com.codeprometheus.aialgohelper.plugin.model.Config;
import com.codeprometheus.aialgohelper.plugin.model.Question;
import com.codeprometheus.aialgohelper.plugin.utils.MessageUtils;
import com.codeprometheus.aialgohelper.plugin.utils.PropertiesUtils;
import com.codeprometheus.aialgohelper.plugin.window.dialog.TestcasePanel;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author shuzijun
 */
public class TestcaseAction extends AbstractEditAction {
    @Override
    public void actionPerformed(AnActionEvent anActionEvent, Config config, Question question) {
        AtomicReference<String> text = new AtomicReference<>(TestcaseAction.class.getName());
        ApplicationManager.getApplication().invokeAndWait(() -> {
            TestcasePanel dialog = new TestcasePanel(anActionEvent.getProject(), question);
            dialog.setTitle(question.getFormTitle() + " Testcase");
            dialog.setText(question.getTestCase());
            if (dialog.showAndGet()) {
                text.set(dialog.testcaseText());
            }
        });
        if (!TestcaseAction.class.getName().equals(text.get())) {
            if (StringUtils.isBlank(text.get())) {
                MessageUtils.getInstance(anActionEvent.getProject()).showWarnMsg("info", PropertiesUtils.getInfo("test.case"));
                return;
            } else {
                question.setTestCase(text.get());
                CodeManager.RunCodeCode(question.getTitleSlug(), anActionEvent.getProject());
            }
        }
    }
}
