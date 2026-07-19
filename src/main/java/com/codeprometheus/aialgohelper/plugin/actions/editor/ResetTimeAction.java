package com.codeprometheus.aialgohelper.plugin.actions.editor;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.wm.WindowManager;
import com.codeprometheus.aialgohelper.plugin.model.Config;
import com.codeprometheus.aialgohelper.plugin.model.Question;
import com.codeprometheus.aialgohelper.plugin.timer.TimerBarWidget;

/**
 * @author shuzijun
 */
public class ResetTimeAction extends AbstractEditAction {

    @Override
    public void actionPerformed(AnActionEvent anActionEvent, Config config, Question question) {
        TimerBarWidget timerBarWidget = (TimerBarWidget) WindowManager.getInstance().getStatusBar(anActionEvent.getProject()).getWidget(TimerBarWidget.ID);
        if (timerBarWidget != null) {
            timerBarWidget.reset();
        } else {
            //For possible reasons, the IDE version is not supported
        }
    }
}
