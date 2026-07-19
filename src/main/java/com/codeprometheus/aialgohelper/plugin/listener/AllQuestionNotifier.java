package com.codeprometheus.aialgohelper.plugin.listener;

import com.intellij.util.messages.Topic;
import com.codeprometheus.aialgohelper.plugin.model.PluginConstant;

/**
 * @author shuzijun
 */
public interface AllQuestionNotifier {

    @Topic.AppLevel
    Topic<AllQuestionNotifier> TOPIC = Topic.create(PluginConstant.LEETCODE_ALL_QUESTION_TOPIC, AllQuestionNotifier.class);

    void reset();
}
