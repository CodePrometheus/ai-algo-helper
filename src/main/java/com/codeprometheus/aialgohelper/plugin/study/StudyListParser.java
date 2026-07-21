package com.codeprometheus.aialgohelper.plugin.study;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.codeprometheus.aialgohelper.plugin.model.study.StudyList;
import com.codeprometheus.aialgohelper.plugin.model.study.StudyListDescriptor;
import com.codeprometheus.aialgohelper.plugin.model.study.StudyListProblem;
import com.codeprometheus.aialgohelper.plugin.model.study.StudyListSection;
import com.vladsch.flexmark.ast.Heading;
import com.vladsch.flexmark.ast.Link;
import com.vladsch.flexmark.ast.ListItem;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.ast.TextCollectingVisitor;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.net.URI;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class StudyListParser {

    private static final Parser MARKDOWN_PARSER = Parser.builder().build();

    public StudyList parse(String html, StudyListDescriptor descriptor) {
        String markdown = extractMarkdown(html);
        Node document = MARKDOWN_PARSER.parse(markdown);
        SectionBuilder root = new SectionBuilder(0, descriptor.title());
        Deque<SectionBuilder> sectionStack = new ArrayDeque<>();
        sectionStack.push(root);

        for (Node node : document.getDescendants()) {
            if (node instanceof Heading heading) {
                String title = normalizeText(new TextCollectingVisitor().collectAndGetText(heading));
                if (StringUtils.isBlank(title) || isDocumentTitle(heading, title, descriptor)) {
                    continue;
                }
                while (sectionStack.peek().level >= heading.getLevel()) {
                    sectionStack.pop();
                }
                SectionBuilder section = new SectionBuilder(heading.getLevel(), title);
                sectionStack.peek().sections.add(section);
                sectionStack.push(section);
            } else if (node instanceof Link link) {
                String titleSlug = extractProblemSlug(link.getUrl().toString());
                if (titleSlug == null) {
                    continue;
                }
                String title = normalizeText(new TextCollectingVisitor().collectAndGetText(link));
                sectionStack.peek().problems.add(
                        new StudyListProblem(title, titleSlug, extractNote(link, title))
                );
            }
        }

        if (root.problemCount() == 0) {
            throw new IllegalArgumentException("No LeetCode problems were found in the study list");
        }
        return new StudyList(
                descriptor,
                root.sections.stream()
                        .filter(section -> section.problemCount() > 0)
                        .map(SectionBuilder::build)
                        .toList(),
                root.problems
        );
    }

    String extractMarkdown(String html) {
        Element nextData = Jsoup.parse(html).selectFirst("script#__NEXT_DATA__");
        if (nextData == null) {
            throw new IllegalArgumentException("The page does not contain __NEXT_DATA__");
        }

        JSONObject root = JSON.parseObject(nextData.data());
        JSONObject pageProps = objectAt(root, "props", "pageProps");
        JSONObject dehydratedState = pageProps == null ? null : pageProps.getJSONObject("dehydratedState");
        JSONArray queries = dehydratedState == null ? null : dehydratedState.getJSONArray("queries");
        if (queries == null) {
            throw new IllegalArgumentException("The page does not contain discussion data");
        }

        for (int i = 0; i < queries.size(); i++) {
            JSONObject query = queries.getJSONObject(i);
            JSONObject data = objectAt(query, "state", "data");
            JSONObject question = data == null ? null : data.getJSONObject("qaQuestion");
            String content = question == null ? null : question.getString("content");
            if (StringUtils.isNotBlank(content)) {
                return content;
            }
        }
        throw new IllegalArgumentException("The discussion does not contain Markdown content");
    }

    private static JSONObject objectAt(JSONObject source, String... keys) {
        JSONObject current = source;
        for (String key : keys) {
            if (current == null) {
                return null;
            }
            current = current.getJSONObject(key);
        }
        return current;
    }

    private static boolean isDocumentTitle(Heading heading, String title, StudyListDescriptor descriptor) {
        return heading.getLevel() == 1 &&
                (title.equals(descriptor.title()) || descriptor.title().startsWith(title));
    }

    private static String extractProblemSlug(String url) {
        try {
            URI uri = URI.create(url);
            String host = uri.getHost();
            if (host != null && !host.equals("leetcode.cn") && !host.endsWith(".leetcode.cn")) {
                return null;
            }
            String[] segments = uri.getPath().split("/");
            for (int i = 0; i + 1 < segments.length; i++) {
                if ("problems".equals(segments[i]) && StringUtils.isNotBlank(segments[i + 1])) {
                    return segments[i + 1];
                }
            }
        } catch (IllegalArgumentException ignore) {
        }
        return null;
    }

    private static String extractNote(Link link, String title) {
        Node listItem = link.getAncestorOfType(ListItem.class);
        if (listItem == null) {
            return "";
        }
        String itemText = normalizeText(new TextCollectingVisitor().collectAndGetText(listItem));
        int titleIndex = itemText.indexOf(title);
        if (titleIndex < 0) {
            return "";
        }
        return itemText.substring(titleIndex + title.length()).trim();
    }

    private static String normalizeText(String text) {
        return text == null ? "" : InlineMathTextConverter.toPlainText(text).replaceAll("\\s+", " ").trim();
    }

    private static class SectionBuilder {

        private final int level;
        private final String title;
        private final List<SectionBuilder> sections = new ArrayList<>();
        private final List<StudyListProblem> problems = new ArrayList<>();

        private SectionBuilder(int level, String title) {
            this.level = level;
            this.title = title;
        }

        private int problemCount() {
            return problems.size() + sections.stream().mapToInt(SectionBuilder::problemCount).sum();
        }

        private StudyListSection build() {
            return new StudyListSection(
                    level,
                    title,
                    sections.stream()
                            .filter(section -> section.problemCount() > 0)
                            .map(SectionBuilder::build)
                            .toList(),
                    problems
            );
        }
    }
}
