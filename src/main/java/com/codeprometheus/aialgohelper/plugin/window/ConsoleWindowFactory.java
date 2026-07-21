package com.codeprometheus.aialgohelper.plugin.window;

import com.intellij.ide.DataManager;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.impl.SimpleDataContext;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.codeprometheus.aialgohelper.plugin.model.PluginConstant;
import com.codeprometheus.aialgohelper.plugin.setting.PersistentConfig;
import com.codeprometheus.aialgohelper.plugin.utils.DataKeys;
import icons.AIAlgoHelperIcons;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author shuzijun
 */
public class ConsoleWindowFactory implements ToolWindowFactory, DumbAware {

    public static String ID = PluginConstant.CONSOLE_WINDOW_ID;


    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {

        ConsolePanel consolePanel = new ConsolePanel(toolWindow, project);
        Content content = toolWindow.getContentManager().getFactory().createContent(consolePanel, "", true);
        toolWindow.getContentManager().addContent(content);
        if (PersistentConfig.getInstance().getInitConfig() != null && !PersistentConfig.getInstance().getInitConfig().getShowToolIcon()) {
            toolWindow.setIcon(AIAlgoHelperIcons.EMPEROR_NEW_CLOTHES);
        }
    }

    public static DataContext getDataContext(@NotNull Project project) {
        if (ApplicationManager.getApplication().isDispatchThread()) {
            return createDataContext(project);
        }
        AtomicReference<DataContext> dataContext = new AtomicReference<>(DataContext.EMPTY_CONTEXT);
        ApplicationManager.getApplication().invokeAndWait(() -> dataContext.set(createDataContext(project)));
        return dataContext.get();
    }

    private static DataContext createDataContext(@NotNull Project project) {
        ToolWindow leetcodeToolWindows = ToolWindowManager.getInstance(project).getToolWindow(ID);
        if (leetcodeToolWindows == null || leetcodeToolWindows.getContentManager().getContentCount() == 0) {
            return DataContext.EMPTY_CONTEXT;
        }
        Content content = leetcodeToolWindows.getContentManager().getContent(0);
        if (content == null || !(content.getComponent() instanceof ConsolePanel)) {
            return DataContext.EMPTY_CONTEXT;
        }
        ConsolePanel consolePanel = (ConsolePanel) content.getComponent();
        ConsoleView consoleView = DataManager.getInstance().getDataContext(consolePanel).getData(DataKeys.LEETCODE_CONSOLE_VIEW);
        SimpleDataContext.Builder builder = SimpleDataContext.builder();
        if (consoleView != null) {
            builder.add(DataKeys.LEETCODE_CONSOLE_VIEW, consoleView);
        }
        return builder.build();
    }

    @Override
    public boolean shouldBeAvailable(@NotNull Project project) {
        return false;
    }
}
