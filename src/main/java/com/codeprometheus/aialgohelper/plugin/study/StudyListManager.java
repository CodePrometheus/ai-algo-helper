package com.codeprometheus.aialgohelper.plugin.study;

import com.codeprometheus.aialgohelper.plugin.model.HttpRequest;
import com.codeprometheus.aialgohelper.plugin.model.study.StudyList;
import com.codeprometheus.aialgohelper.plugin.model.study.StudyListDescriptor;
import com.codeprometheus.aialgohelper.plugin.utils.HttpResponse;

import java.util.List;

public class StudyListManager {

    private static final String AUTHOR = "灵茶山艾府";

    private static final List<StudyListDescriptor> STUDY_LISTS = List.of(
            new StudyListDescriptor(
                    "滑动窗口与双指针",
                    "定长 / 不定长 / 单序列 / 双序列 / 三指针 / 分组循环",
                    AUTHOR,
                    "https://leetcode.cn/discuss/post/0viNMK/"
            ),
            new StudyListDescriptor(
                    "二分算法",
                    "二分答案 / 最小化最大值 / 最大化最小值 / 第 K 小",
                    AUTHOR,
                    "https://leetcode.cn/discuss/post/SqopEo/"
            ),
            new StudyListDescriptor(
                    "单调栈",
                    "基础 / 矩形面积 / 贡献法 / 最小字典序",
                    AUTHOR,
                    "https://leetcode.cn/discuss/post/9oZFK9/"
            ),
            new StudyListDescriptor(
                    "网格图",
                    "DFS / BFS / 综合应用",
                    AUTHOR,
                    "https://leetcode.cn/discuss/post/YiXPXW/"
            ),
            new StudyListDescriptor(
                    "位运算",
                    "基础 / 性质 / 拆位 / 试填 / 恒等式 / 思维",
                    AUTHOR,
                    "https://leetcode.cn/discuss/post/dHn9Vk/"
            ),
            new StudyListDescriptor(
                    "图论算法",
                    "DFS / BFS / 拓扑排序 / 基环树 / 最短路 / 最小生成树 / 网络流",
                    AUTHOR,
                    "https://leetcode.cn/discuss/post/01LUak/"
            ),
            new StudyListDescriptor(
                    "动态规划",
                    "入门 / 背包 / 划分 / 状态机 / 区间 / 状压 / 数位 / 数据结构优化 / 树形 / 博弈 / 概率期望",
                    AUTHOR,
                    "https://leetcode.cn/discuss/post/tXLS3i/"
            ),
            new StudyListDescriptor(
                    "常用数据结构",
                    "枚举技巧 / 前缀和 / 差分 / 栈 / 队列 / 堆 / 字典树 / 并查集 / 树状数组 / 线段树",
                    AUTHOR,
                    "https://leetcode.cn/discuss/post/mOr1u6/"
            ),
            new StudyListDescriptor(
                    "数学算法",
                    "数论 / 组合 / 概率期望 / 博弈 / 计算几何 / 随机算法",
                    AUTHOR,
                    "https://leetcode.cn/discuss/post/IYT3ss/"
            ),
            new StudyListDescriptor(
                    "贪心与思维",
                    "基本贪心策略 / 反悔 / 区间 / 字典序 / 数学 / 思维 / 脑筋急转弯 / 构造",
                    AUTHOR,
                    "https://leetcode.cn/discuss/post/g6KTKL/"
            ),
            new StudyListDescriptor(
                    "链表、树与回溯",
                    "前后指针 / 快慢指针 / DFS / BFS / 直径 / LCA",
                    AUTHOR,
                    "https://leetcode.cn/discuss/post/K0n2gO/"
            ),
            new StudyListDescriptor(
                    "字符串",
                    "KMP / Z 函数 / Manacher / 字符串哈希 / AC 自动机 / 后缀数组 / 子序列自动机",
                    AUTHOR,
                    "https://leetcode.cn/discuss/post/SJFwQI/"
            )
    );

    private final StudyListParser parser = new StudyListParser();

    public List<StudyListDescriptor> getStudyLists() {
        return STUDY_LISTS;
    }

    public StudyList load(StudyListDescriptor descriptor) {
        HttpResponse response = HttpRequest.builderGet(descriptor.sourceUrl())
                .cache(true)
                .request();
        if (response.getStatusCode() != 200) {
            throw new IllegalStateException("Failed to load study list: HTTP " + response.getStatusCode());
        }
        return parser.parse(response.getBody(), descriptor);
    }
}
