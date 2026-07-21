package com.codeprometheus.aialgohelper.plugin.window;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.impl.SimpleDataContext;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.content.ContentManager;
import com.codeprometheus.aialgohelper.plugin.manager.NavigatorAction;
import com.codeprometheus.aialgohelper.plugin.model.PluginConstant;
import com.codeprometheus.aialgohelper.plugin.setting.PersistentConfig;
import com.codeprometheus.aialgohelper.plugin.utils.DataKeys;
import icons.LeetCodeEditorIcons;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author shuzijun
 */
public class WindowFactory implements ToolWindowFactory, DumbAware {

    public static String ID = PluginConstant.TOOL_WINDOW_ID;

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {

        ContentFactory contentFactory = ContentFactory.getInstance();
        JComponent navigatorPanel = new NavigatorTabsPanel(toolWindow, project);
        Content content = contentFactory.createContent(navigatorPanel, "", false);
        toolWindow.getContentManager().addContent(content);
        if (PersistentConfig.getInstance().getInitConfig() != null) {
            if (!PersistentConfig.getInstance().getInitConfig().getShowToolIcon()) {
                toolWindow.setIcon(LeetCodeEditorIcons.EMPEROR_NEW_CLOTHES);
            }
            if (!PersistentConfig.getInstance().getInitConfig().isLeftQuestionEditor()) {
                toolWindow.setAnchor(ToolWindowAnchor.RIGHT, null);
            }

        }
    }


    @NotNull
    public static DataContext getDataContext(@NotNull Project project) {
        if (ApplicationManager.getApplication().isDispatchThread()) {
            return createDataContext(project);
        }
        AtomicReference<DataContext> dataContext = new AtomicReference<>(DataContext.EMPTY_CONTEXT);
        ApplicationManager.getApplication().invokeAndWait(() -> dataContext.set(createDataContext(project)));
        return dataContext.get();
    }

    @NotNull
    private static DataContext createDataContext(@NotNull Project project) {
        ToolWindow leetcodeToolWindows = ToolWindowManager.getInstance(project).getToolWindow(ID);
        if (leetcodeToolWindows == null) {
            return DataContext.EMPTY_CONTEXT;
        }
        ContentManager navigatorContentManager = leetcodeToolWindows.getContentManagerIfCreated();
        if (navigatorContentManager == null) {
            return DataContext.EMPTY_CONTEXT;
        }
        Content navigatorContent= navigatorContentManager.getContent(0);
        if (navigatorContent == null) {
            return DataContext.EMPTY_CONTEXT;
        }
        JComponent navigatorPanel =  navigatorContent.getComponent();
        DataContext componentDataContext = DataManager.getInstance().getDataContext(navigatorPanel);
        NavigatorTabsPanel navigatorTabsPanel = componentDataContext.getData(DataKeys.LEETCODE_PROJECTS_TABS);
        NavigatorAction navigatorAction = componentDataContext.getData(DataKeys.LEETCODE_PROJECTS_NAVIGATORACTION);
        SimpleDataContext.Builder builder = SimpleDataContext.builder();
        if (navigatorTabsPanel != null) {
            builder.add(DataKeys.LEETCODE_PROJECTS_TABS, navigatorTabsPanel);
        }
        if (navigatorAction != null) {
            builder.add(DataKeys.LEETCODE_PROJECTS_NAVIGATORACTION, navigatorAction);
        }
        return builder.build();
    }

    public static void updateTitle(@NotNull Project project, String userName) {
        ToolWindow leetcodeToolWindows = ToolWindowManager.getInstance(project).getToolWindow(ID);
        ApplicationManager.getApplication().invokeLater(() -> {
            if (StringUtils.isNotBlank(userName)) {
                leetcodeToolWindows.setTitle("[" + userName + "]");
            } else {
                leetcodeToolWindows.setTitle("");
            }
        });

    }

    public static void activateToolWindow(@NotNull Project project) {
        ToolWindow leetcodeToolWindows = ToolWindowManager.getInstance(project).getToolWindow(ID);
        leetcodeToolWindows.activate(null);
    }

}
