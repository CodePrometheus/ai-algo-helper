package com.codeprometheus.aialgohelper.plugin.actions;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.codeprometheus.aialgohelper.plugin.model.Config;
import com.codeprometheus.aialgohelper.plugin.model.PluginConstant;
import com.codeprometheus.aialgohelper.plugin.setting.PersistentConfig;
import com.codeprometheus.aialgohelper.plugin.utils.MessageUtils;
import com.codeprometheus.aialgohelper.plugin.utils.PropertiesUtils;
import org.jetbrains.annotations.NotNull;

/**
 * @author shuzijun
 */
public abstract class AbstractAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        Config config = PersistentConfig.getInstance().getInitConfig();
        if (config == null) {
            MessageUtils.getInstance(anActionEvent.getProject()).showWarnMsg("warning", PropertiesUtils.getInfo("config.first"));
            ShowSettingsUtil.getInstance().showSettingsDialog(anActionEvent.getProject(), PluginConstant.APPLICATION_CONFIGURABLE_DISPLAY_NAME);
            return;
        }

        ProgressManager.getInstance().run(new Task.Backgroundable(anActionEvent.getProject(),anActionEvent.getActionManager().getId(this),false) {
            @Override
            public void run(@NotNull ProgressIndicator progressIndicator) {
                actionPerformed(anActionEvent, config);
            }
        });

    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return  ActionUpdateThread.BGT;
    }

    public abstract void actionPerformed(AnActionEvent anActionEvent, Config config);
}
