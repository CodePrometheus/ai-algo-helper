package com.codeprometheus.aialgohelper.plugin.actions.tree;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.codeprometheus.aialgohelper.plugin.model.CodeTypeEnum;
import com.codeprometheus.aialgohelper.plugin.model.Config;
import com.codeprometheus.aialgohelper.plugin.model.Question;
import com.codeprometheus.aialgohelper.plugin.setting.PersistentConfig;
import com.codeprometheus.aialgohelper.plugin.utils.MessageUtils;
import com.codeprometheus.aialgohelper.plugin.utils.PropertiesUtils;
import com.codeprometheus.aialgohelper.plugin.utils.VelocityUtils;

import java.io.File;

/**
 * @author shuzijun
 */
public class ClearOneAction extends AbstractTreeAction {
    @Override
    public void actionPerformed(AnActionEvent anActionEvent, Config config, Question question) {

        String codeType = config.getCodeType();
        CodeTypeEnum codeTypeEnum = CodeTypeEnum.getCodeTypeEnum(codeType);
        if (codeTypeEnum == null) {
            MessageUtils.getInstance(anActionEvent.getProject()).showWarnMsg("info", PropertiesUtils.getInfo("config.code"));
            return;
        }

        String filePath = PersistentConfig.getInstance().getTempFilePath() + VelocityUtils.convert(config.getCustomFileName(), question) + codeTypeEnum.getSuffix();

        File file = new File(filePath);
        if (file.exists()) {
            ApplicationManager.getApplication().invokeAndWait(() -> {
                VirtualFile vf = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(file);
                if (FileEditorManager.getInstance(anActionEvent.getProject()).isFileOpen(vf)) {
                    FileEditorManager.getInstance(anActionEvent.getProject()).closeFile(vf);
                }
                file.delete();
            });
        }
        MessageUtils.getInstance(anActionEvent.getProject()).showInfoMsg(question.getFormTitle(), PropertiesUtils.getInfo("clear.success"));

    }

}
