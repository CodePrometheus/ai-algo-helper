package com.codeprometheus.aialgohelper.plugin.setting;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.*;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.codeprometheus.aialgohelper.plugin.model.Config;
import com.codeprometheus.aialgohelper.plugin.model.Constant;
import com.codeprometheus.aialgohelper.plugin.model.PluginConstant;
import com.codeprometheus.aialgohelper.plugin.utils.MessageUtils;
import com.codeprometheus.aialgohelper.plugin.utils.PropertiesUtils;
import com.codeprometheus.aialgohelper.plugin.utils.URLUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shuzijun
 */
@State(name = "PersistentConfig" + PluginConstant.ACTION_SUFFIX, storages = {@Storage(value = PluginConstant.ACTION_PREFIX + "-config.xml", roamingType = RoamingType.DISABLED)})
public class PersistentConfig implements PersistentStateComponent<PersistentConfig> {

    public static String PATH = "leetcode" + File.separator + "editor";
    public static String OLDPATH = "leetcode-plugin";
    private static String INITNAME = "initConfig";

    private Map<String, Config> initConfig = new HashMap<>();

    public static PersistentConfig getInstance() {
        return ApplicationManager.getApplication().getService(PersistentConfig.class);
    }

    @Nullable
    @Override
    public PersistentConfig getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull PersistentConfig persistentConfig) {
        XmlSerializerUtil.copyBean(persistentConfig, this);
    }


    @Nullable
    public Config getInitConfig() {
        Config config = initConfig.get(INITNAME);
        if (config != null && config.getVersion() != null && config.getVersion() < Constant.PLUGIN_CONFIG_VERSION_3) {
            if (URLUtils.leetcodecnOld.equals(config.getUrl())) {
                config.setUrl(URLUtils.leetcodecn);
            }
            config.setVersion(Constant.PLUGIN_CONFIG_VERSION_3);
            setInitConfig(config);
        }
        return config;
    }

    @NotNull
    public Config getConfig() {
        Config config = getInitConfig();
        if (config == null) {
            MessageUtils.showAllWarnMsg("warning", PropertiesUtils.getInfo("config.first"));
            throw new UnsupportedOperationException("not configured:File -> settings->tools->leetcode plugin");
        } else {
            return config;
        }

    }

    public void setInitConfig(Config config) {
        initConfig.put(INITNAME, config);
    }

    public String getTempFilePath() {
        return getConfig().getFilePath() + File.separator + PATH + File.separator + initConfig.get(INITNAME).getAlias() + File.separator;
    }

}
