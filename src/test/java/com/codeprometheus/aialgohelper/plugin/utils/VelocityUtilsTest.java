package com.codeprometheus.aialgohelper.plugin.utils;

import com.intellij.testFramework.fixtures.BasePlatformTestCase;

import java.util.Map;

public class VelocityUtilsTest extends BasePlatformTestCase {

    public void testRendersTemplatesWithTheIntellijFileTemplateApi() {
        Map<String, String> question = Map.of(
                "frontendQuestionId", "1",
                "title", "Two Sum",
                "titleSlug", "two-sum"
        );

        String rendered = VelocityUtils.convert(
                "[$!{question.frontendQuestionId}]${question.title} " +
                        "#if(${question.titleSlug})$velocityTool.camelCaseName(${question.titleSlug})#end",
                question
        );

        assertEquals("[1]Two Sum TwoSum", rendered);
    }

    public void testRendersPinyinTemplateHelper() {
        String rendered = VelocityUtils.convert(
                "$velocityTool.toPinyinAndTrims(${question.title})",
                Map.of("title", "两数之和")
        );

        assertEquals("LiangShuZhiHe", rendered);
    }
}
