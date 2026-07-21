package com.codeprometheus.aialgohelper.plugin.actions.toolbar;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ex.ActionUtil;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.DumbAware;
import com.codeprometheus.aialgohelper.plugin.actions.AbstractAction;
import com.codeprometheus.aialgohelper.plugin.manager.NavigatorAction;
import com.codeprometheus.aialgohelper.plugin.model.Config;
import com.codeprometheus.aialgohelper.plugin.model.Constant;
import com.codeprometheus.aialgohelper.plugin.model.PluginConstant;
import com.codeprometheus.aialgohelper.plugin.model.Sort;
import com.codeprometheus.aialgohelper.plugin.utils.DataKeys;
import com.codeprometheus.aialgohelper.plugin.window.WindowFactory;
import icons.AIAlgoHelperIcons;
import org.jetbrains.annotations.NotNull;

/**
 * @author shuzijun
 */
public class SortAction extends AbstractAction implements DumbAware {

    public SortAction() {
        getTemplatePresentation().putClientProperty(ActionUtil.SHOW_TEXT_IN_TOOLBAR, true);
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        NavigatorAction navigatorAction = WindowFactory.getDataContext(e.getProject()).getData(DataKeys.LEETCODE_PROJECTS_NAVIGATORACTION);
        Sort sort = getSort(e, navigatorAction);
        if (sort == null) {
            return;
        }
        if (sort.getType() == Constant.SORT_ASC) {
            e.getPresentation().setIcon(AIAlgoHelperIcons.SORT_ASC);
        } else if (sort.getType() == Constant.SORT_DESC) {
            e.getPresentation().setIcon(AIAlgoHelperIcons.SORT_DESC);
        } else {
            e.getPresentation().setIcon(null);
        }
        super.update(e);

    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent, Config config) {
        NavigatorAction navigatorAction = WindowFactory.getDataContext(anActionEvent.getProject()).getData(DataKeys.LEETCODE_PROJECTS_NAVIGATORACTION);
        if (navigatorAction == null) {
            return;
        }
        Sort sort = getSort(anActionEvent, navigatorAction);
        if (sort == null) {
            return;
        }
        navigatorAction.getFind().operationType(getKey(anActionEvent));
        navigatorAction.sort(sort);
    }

    private Sort getSort(AnActionEvent anActionEvent, NavigatorAction navigatorAction) {
        return navigatorAction.getFind().getSort(getKey(anActionEvent));
    }

    private String getKey(AnActionEvent anActionEvent) {
        return anActionEvent.getActionManager().getId(this).replace(PluginConstant.LEETCODE_SORT_PREFIX, "")
                .replace(PluginConstant.LEETCODE_CODETOP_SORT_PREFIX, "")
                .replace(PluginConstant.LEETCODE_ALL_SORT_PREFIX, "");
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return  ActionUpdateThread.EDT;
    }
}
