package com.codeprometheus.aialgohelper.plugin.model.study;

import java.util.List;

public class StudyListSection {

    private final int level;
    private final String title;
    private final List<StudyListSection> sections;
    private final List<StudyListProblem> problems;

    public StudyListSection(int level,
                            String title,
                            List<StudyListSection> sections,
                            List<StudyListProblem> problems) {
        this.level = level;
        this.title = title;
        this.sections = List.copyOf(sections);
        this.problems = List.copyOf(problems);
    }

    public int getLevel() {
        return level;
    }

    public String getTitle() {
        return title;
    }

    public List<StudyListSection> getSections() {
        return sections;
    }

    public List<StudyListProblem> getProblems() {
        return problems;
    }

    public int getProblemCount() {
        return problems.size() + sections.stream().mapToInt(StudyListSection::getProblemCount).sum();
    }
}
