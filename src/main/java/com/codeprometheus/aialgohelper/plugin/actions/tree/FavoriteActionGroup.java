package com.codeprometheus.aialgohelper.plugin.actions.tree;

import com.google.common.collect.Lists;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAware;
import com.codeprometheus.aialgohelper.plugin.manager.NavigatorAction;
import com.codeprometheus.aialgohelper.plugin.model.Constant;
import com.codeprometheus.aialgohelper.plugin.model.Tag;
import com.codeprometheus.aialgohelper.plugin.utils.DataKeys;
import com.codeprometheus.aialgohelper.plugin.window.WindowFactory;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author shuzijun
 */
public class FavoriteActionGroup extends ActionGroup implements DumbAware {

    @Override
    public AnAction[] getChildren(AnActionEvent anActionEvent) {
        NavigatorAction navigatorAction = WindowFactory.getDataContext(anActionEvent.getProject()).getData(DataKeys.LEETCODE_PROJECTS_NAVIGATORACTION);
        if (navigatorAction == null){
            return new AnAction[0];
        }
        List<AnAction> anActionList = Lists.newArrayList();
        List<Tag> tags = navigatorAction.getFind().getFilter(Constant.FIND_TYPE_LISTS);
        if (tags != null && !tags.isEmpty()) {
            for (Tag tag : tags) {
                if (!"leetcode_favorites".equals(tag.getType())) {
                    anActionList.add(new FavoriteAction(tag.getName(), tag));
                }
            }
        }
        AnAction[] anActions = new AnAction[anActionList.size()];
        anActionList.toArray(anActions);
        return anActions;

    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return  ActionUpdateThread.BGT;
    }

}
