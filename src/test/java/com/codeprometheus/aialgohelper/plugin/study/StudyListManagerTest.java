package com.codeprometheus.aialgohelper.plugin.study;

import com.codeprometheus.aialgohelper.plugin.model.study.StudyListDescriptor;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StudyListManagerTest {

    @Test
    public void containsTheCompleteScientificPracticeCatalog() {
        List<StudyListDescriptor> studyLists = new StudyListManager().getStudyLists();

        assertEquals(12, studyLists.size());
        assertEquals(List.of(
                        "滑动窗口与双指针",
                        "二分算法",
                        "单调栈",
                        "网格图",
                        "位运算",
                        "图论算法",
                        "动态规划",
                        "常用数据结构",
                        "数学算法",
                        "贪心与思维",
                        "链表、树与回溯",
                        "字符串"
                ),
                studyLists.stream().map(StudyListDescriptor::title).toList());
        assertTrue(studyLists.stream().allMatch(descriptor -> "灵茶山艾府".equals(descriptor.author())));
        assertTrue(studyLists.stream().allMatch(descriptor ->
                descriptor.sourceUrl().startsWith("https://leetcode.cn/discuss/post/")
        ));
    }
}
