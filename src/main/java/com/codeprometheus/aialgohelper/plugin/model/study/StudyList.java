package com.codeprometheus.aialgohelper.plugin.model.study;

import java.util.List;

public class StudyList {

    private final StudyListDescriptor descriptor;
    private final List<StudyListSection> sections;
    private final List<StudyListProblem> problems;

    public StudyList(StudyListDescriptor descriptor,
                     List<StudyListSection> sections,
                     List<StudyListProblem> problems) {
        this.descriptor = descriptor;
        this.sections = List.copyOf(sections);
        this.problems = List.copyOf(problems);
    }

    public StudyListDescriptor getDescriptor() {
        return descriptor;
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
