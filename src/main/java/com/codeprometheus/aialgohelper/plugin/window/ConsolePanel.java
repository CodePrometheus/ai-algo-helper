package com.codeprometheus.aialgohelper.plugin.window;

import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DataSink;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.wm.ToolWindow;
import com.codeprometheus.aialgohelper.plugin.model.PluginConstant;
import com.codeprometheus.aialgohelper.plugin.utils.DataKeys;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author shuzijun
 */
public class ConsolePanel extends SimpleToolWindowPanel {

    private ConsoleView consoleView;

    public ConsolePanel(ToolWindow toolWindow, Project project) {
        super(Boolean.FALSE, Boolean.TRUE);
        this.consoleView = TextConsoleBuilderFactory.getInstance().createBuilder(project).getConsole();
        SimpleToolWindowPanel toolWindowPanel = new SimpleToolWindowPanel(Boolean.FALSE, Boolean.TRUE);
        toolWindowPanel.setContent(consoleView.getComponent());
        setContent(toolWindowPanel);
        final DefaultActionGroup consoleGroup = new DefaultActionGroup(consoleView.createConsoleActions());
        ActionToolbar consoleToolbar = ActionManager.getInstance().createActionToolbar(PluginConstant.ACTION_PREFIX + " ConsoleToolbar", consoleGroup, true);
        consoleToolbar.setTargetComponent(toolWindowPanel);
        setToolbar(consoleToolbar.getComponent());

    }

    @Override
    public void uiDataSnapshot(@NotNull DataSink sink) {
        super.uiDataSnapshot(sink);
        if (consoleView != null) {
            sink.set(DataKeys.LEETCODE_CONSOLE_VIEW, consoleView);
        }
    }

    public void dispose() {
        if (consoleView != null) {
            Disposer.dispose(consoleView);
        }
    }
}
