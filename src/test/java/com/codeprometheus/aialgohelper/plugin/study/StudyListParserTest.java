package com.codeprometheus.aialgohelper.plugin.study;

import com.alibaba.fastjson.JSONObject;
import com.codeprometheus.aialgohelper.plugin.model.study.StudyList;
import com.codeprometheus.aialgohelper.plugin.model.study.StudyListDescriptor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StudyListParserTest {

    private static final StudyListDescriptor DESCRIPTOR = new StudyListDescriptor(
            "滑动窗口与双指针",
            "定长 / 不定长",
            "灵茶山艾府",
            "https://leetcode.cn/discuss/post/3578981/example/"
    );

    @Test
    public void parsesNestedSectionsProblemsAndNotes() {
        String markdown = """
                # 滑动窗口与双指针
                ## 定长滑动窗口
                - [1456. 定长子串中元音的最大数目](https://leetcode.cn/problems/maximum-number-of-vowels-in-a-substring-of-given-length/) 1263
                ### 基础
                - [643. 子数组最大平均数 I](https://leetcode.cn/problems/maximum-average-subarray-i/)
                ## 不定长滑动窗口
                - [3. 无重复字符的最长子串](https://leetcode.cn/problems/longest-substring-without-repeating-characters/) 做到 O(n)
                """;

        StudyList studyList = new StudyListParser().parse(pageWithMarkdown(markdown), DESCRIPTOR);

        assertEquals(3, studyList.getProblemCount());
        assertEquals(2, studyList.getSections().size());
        assertEquals("定长滑动窗口", studyList.getSections().get(0).getTitle());
        assertEquals("基础", studyList.getSections().get(0).getSections().get(0).getTitle());
        assertEquals("maximum-number-of-vowels-in-a-substring-of-given-length",
                studyList.getSections().get(0).getProblems().get(0).titleSlug());
        assertEquals("1263", studyList.getSections().get(0).getProblems().get(0).note());
        assertEquals("做到 O(n)", studyList.getSections().get(1).getProblems().get(0).note());
    }

    @Test
    public void preservesProblemsOutsideHeadingsAndIgnoresExternalLinks() {
        String markdown = """
                - [1. 两数之和](https://leetcode.cn/problems/two-sum/)
                ## 说明
                - [说明](https://example.com/problems/not-a-problem/)
                """;

        StudyList studyList = new StudyListParser().parse(pageWithMarkdown(markdown), DESCRIPTOR);

        assertEquals(1, studyList.getProblemCount());
        assertEquals("two-sum", studyList.getProblems().get(0).titleSlug());
        assertEquals(0, studyList.getSections().size());
    }

    @Test
    public void rendersInlineMathAsReadablePlainText() {
        String markdown = """
                ## 进阶
                - [2593. 标记所有元素后数组的分数](https://leetcode.cn/problems/find-score-of-an-array-after-marking-all-elements/) 做到 $\\mathcal{O}(n)$，且 $a_i \\le n$
                """;

        StudyList studyList = new StudyListParser().parse(pageWithMarkdown(markdown), DESCRIPTOR);

        assertEquals("做到 O(n)，且 a_i ≤ n", studyList.getSections().get(0).getProblems().get(0).note());
    }

    private static String pageWithMarkdown(String markdown) {
        JSONObject question = new JSONObject();
        question.put("content", markdown);
        JSONObject data = new JSONObject();
        data.put("qaQuestion", question);
        JSONObject state = new JSONObject();
        state.put("data", data);
        JSONObject query = new JSONObject();
        query.put("state", state);
        JSONObject dehydratedState = new JSONObject();
        dehydratedState.put("queries", new Object[]{query});
        JSONObject pageProps = new JSONObject();
        pageProps.put("dehydratedState", dehydratedState);
        JSONObject props = new JSONObject();
        props.put("pageProps", pageProps);
        JSONObject root = new JSONObject();
        root.put("props", props);
        return "<html><body><script id=\"__NEXT_DATA__\" type=\"application/json\">" +
                root.toJSONString() +
                "</script></body></html>";
    }
}
