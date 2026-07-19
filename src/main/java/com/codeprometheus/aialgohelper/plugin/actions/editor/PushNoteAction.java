package com.codeprometheus.aialgohelper.plugin.actions.editor;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.codeprometheus.aialgohelper.plugin.manager.NoteManager;
import com.codeprometheus.aialgohelper.plugin.model.Config;
import com.codeprometheus.aialgohelper.plugin.model.Question;

/**
 * @author shuzijun
 */
public class PushNoteAction extends AbstractEditAction {

    @Override
    public void actionPerformed(AnActionEvent anActionEvent, Config config, Question question) {
        NoteManager.push(question.getTitleSlug(), anActionEvent.getProject());
    }
}
