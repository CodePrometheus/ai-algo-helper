package com.codeprometheus.aialgohelper.plugin.utils;

import com.intellij.ide.fileTemplates.FileTemplateUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shuzijun
 */
public class VelocityUtils {

    private static final String VM_CONTEXT = "question";

    public static String convert(String template, Object data) {
        VelocityTool velocityTool = new VelocityTool();
        Map<String, Object> context = new HashMap<>();
        context.put(VM_CONTEXT, data);
        context.put("velocityTool", velocityTool);
        context.put("vt", velocityTool);
        return FileTemplateUtil.mergeTemplate(context, template, false);
    }
}
