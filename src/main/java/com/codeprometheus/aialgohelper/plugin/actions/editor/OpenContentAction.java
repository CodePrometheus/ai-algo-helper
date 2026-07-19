package com.codeprometheus.aialgohelper.plugin.actions.editor;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.codeprometheus.aialgohelper.plugin.editor.ConvergePreview;
import com.codeprometheus.aialgohelper.plugin.manager.CodeManager;
import com.codeprometheus.aialgohelper.plugin.model.Config;
import com.codeprometheus.aialgohelper.plugin.model.Question;

/**
 * @author shuzijun
 */
public class OpenContentAction extends AbstractEditAction {

    @Override
    public void actionPerformed(AnActionEvent anActionEvent, Config config, Question question) {
        if (config.getConvergeEditor() && openConvergeEditor(anActionEvent, new ConvergePreview.TabSelectFileEditorState("Content"))) {
            return;
        }
        Project project = anActionEvent.getProject();
        CodeManager.openContent(question.getTitleSlug(), project, true);

    }
}
