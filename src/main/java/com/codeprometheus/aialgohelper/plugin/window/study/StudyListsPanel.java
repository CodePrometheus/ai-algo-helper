package com.codeprometheus.aialgohelper.plugin.window.study;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.ui.ColoredTreeCellRenderer;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.ui.components.ActionLink;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.treeStructure.Tree;
import com.intellij.util.ui.JBFont;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import com.codeprometheus.aialgohelper.plugin.manager.CodeManager;
import com.codeprometheus.aialgohelper.plugin.manager.QuestionManager;
import com.codeprometheus.aialgohelper.plugin.model.QuestionIndex;
import com.codeprometheus.aialgohelper.plugin.model.QuestionView;
import com.codeprometheus.aialgohelper.plugin.model.study.StudyList;
import com.codeprometheus.aialgohelper.plugin.model.study.StudyListDescriptor;
import com.codeprometheus.aialgohelper.plugin.model.study.StudyListProblem;
import com.codeprometheus.aialgohelper.plugin.model.study.StudyListSection;
import com.codeprometheus.aialgohelper.plugin.study.StudyListManager;
import com.codeprometheus.aialgohelper.plugin.utils.LogUtils;
import com.codeprometheus.aialgohelper.plugin.utils.PropertiesUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class StudyListsPanel extends JPanel {

    private static final String INDEX_CARD = "index";
    private static final String DETAIL_CARD = "detail";
    private static final String LOADING_CARD = "loading";
    private static final String TREE_CARD = "tree";
    private static final String ERROR_CARD = "error";

    private final Project project;
    private final StudyListManager studyListManager = new StudyListManager();
    private final Map<String, StudyList> cache = new ConcurrentHashMap<>();
    private final CardLayout cards = new CardLayout();
    private final CardLayout detailCards = new CardLayout();
    private final JPanel detailContent = new JPanel(detailCards);
    private final JBLabel detailTitle = new JBLabel();
    private final JBLabel detailSubtitle = new JBLabel();
    private final JBLabel errorMessage = new JBLabel();
    private final Tree studyTree = new Tree();

    private StudyListDescriptor currentDescriptor;

    public StudyListsPanel(Project project) {
        super();
        this.project = project;
        setLayout(cards);
        add(createIndexPanel(), INDEX_CARD);
        add(createDetailPanel(), DETAIL_CARD);
        cards.show(this, INDEX_CARD);
    }

    private JComponent createIndexPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(JBUI.Borders.empty(12));

        JBLabel title = new JBLabel(PropertiesUtils.getInfo("study.title"));
        title.setFont(JBFont.h2());
        JBLabel hint = new JBLabel(PropertiesUtils.getInfo("study.choose"));
        hint.setForeground(UIUtil.getContextHelpForeground());

        JPanel heading = new JPanel();
        heading.setOpaque(false);
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));
        heading.add(title);
        heading.add(Box.createVerticalStrut(JBUI.scale(4)));
        heading.add(hint);
        heading.setBorder(JBUI.Borders.emptyBottom(10));
        panel.add(heading, BorderLayout.NORTH);

        JBList<StudyListDescriptor> list = new JBList<>(studyListManager.getStudyLists());
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setCellRenderer(new StudyListCellRenderer());
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                if (SwingUtilities.isLeftMouseButton(event) && event.getClickCount() == 1) {
                    int index = list.locationToIndex(event.getPoint());
                    if (index >= 0 && list.getCellBounds(index, index).contains(event.getPoint())) {
                        openStudyList(list.getModel().getElementAt(index), false);
                    }
                }
            }
        });
        list.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.VK_ENTER && list.getSelectedValue() != null) {
                    openStudyList(list.getSelectedValue(), false);
                }
            }
        });
        panel.add(new JBScrollPane(list), BorderLayout.CENTER);
        return panel;
    }

    private JComponent createDetailPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(JBUI.Borders.empty(8, 12, 12, 12));

        JPanel actions = new JPanel(new BorderLayout());
        actions.setOpaque(false);
        actions.add(actionLink(PropertiesUtils.getInfo("study.back"), this::showIndex), BorderLayout.WEST);
        actions.add(actionLink(PropertiesUtils.getInfo("study.source"), this::openSource), BorderLayout.EAST);

        detailTitle.setFont(JBFont.h3());
        detailSubtitle.setForeground(UIUtil.getContextHelpForeground());
        JPanel heading = new JPanel();
        heading.setOpaque(false);
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));
        heading.add(actions);
        heading.add(Box.createVerticalStrut(JBUI.scale(10)));
        heading.add(detailTitle);
        heading.add(Box.createVerticalStrut(JBUI.scale(3)));
        heading.add(detailSubtitle);
        heading.setBorder(JBUI.Borders.emptyBottom(8));
        panel.add(heading, BorderLayout.NORTH);

        detailContent.add(centeredLabel(PropertiesUtils.getInfo("study.loading")), LOADING_CARD);
        detailContent.add(createTreePanel(), TREE_CARD);
        detailContent.add(createErrorPanel(), ERROR_CARD);
        panel.add(detailContent, BorderLayout.CENTER);
        return panel;
    }

    private JComponent createTreePanel() {
        studyTree.setRootVisible(false);
        studyTree.setShowsRootHandles(true);
        studyTree.setCellRenderer(new StudyTreeCellRenderer());
        studyTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                if (SwingUtilities.isLeftMouseButton(event) && event.getClickCount() == 2) {
                    openSelectedProblem(studyTree.getPathForLocation(event.getX(), event.getY()));
                }
            }
        });
        studyTree.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.VK_ENTER) {
                    openSelectedProblem(studyTree.getSelectionPath());
                }
            }
        });

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JBScrollPane(studyTree), BorderLayout.CENTER);
        JBLabel hint = new JBLabel(PropertiesUtils.getInfo("study.open.hint"));
        hint.setForeground(UIUtil.getContextHelpForeground());
        hint.setBorder(JBUI.Borders.emptyTop(6));
        panel.add(hint, BorderLayout.SOUTH);
        return panel;
    }

    private JComponent createErrorPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        errorMessage.setAlignmentX(Component.CENTER_ALIGNMENT);
        ActionLink retry = actionLink(PropertiesUtils.getInfo("study.retry"), () -> {
            if (currentDescriptor != null) {
                openStudyList(currentDescriptor, true);
            }
        });
        retry.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(Box.createVerticalGlue());
        panel.add(errorMessage);
        panel.add(Box.createVerticalStrut(JBUI.scale(8)));
        panel.add(retry);
        panel.add(Box.createVerticalGlue());
        return panel;
    }

    private static JComponent centeredLabel(String text) {
        JPanel panel = new JPanel(new GridBagLayout());
        JBLabel label = new JBLabel(text);
        label.setForeground(UIUtil.getContextHelpForeground());
        panel.add(label);
        return panel;
    }

    private static ActionLink actionLink(String text, Runnable action) {
        return new ActionLink(text, (ActionListener) event -> action.run());
    }

    private void openStudyList(StudyListDescriptor descriptor, boolean reload) {
        currentDescriptor = descriptor;
        detailTitle.setText(descriptor.title());
        detailSubtitle.setText(descriptor.subtitle() + "  ·  " + descriptor.author());
        cards.show(this, DETAIL_CARD);

        StudyList cached = reload ? null : cache.get(descriptor.sourceUrl());
        if (cached != null) {
            showStudyList(cached);
            return;
        }

        detailCards.show(detailContent, LOADING_CARD);
        ApplicationManager.getApplication().executeOnPooledThread(() -> {
            try {
                StudyList studyList = studyListManager.load(descriptor);
                cache.put(descriptor.sourceUrl(), studyList);
                ApplicationManager.getApplication().invokeLater(() -> {
                    if (!project.isDisposed() && isCurrent(descriptor)) {
                        showStudyList(studyList);
                    }
                });
            } catch (RuntimeException exception) {
                LogUtils.LOG.warn("Failed to load study list: " + descriptor.sourceUrl(), exception);
                ApplicationManager.getApplication().invokeLater(() -> {
                    if (!project.isDisposed() && isCurrent(descriptor)) {
                        showLoadError(exception);
                    }
                });
            }
        });
    }

    private void showStudyList(StudyList studyList) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(studyList);
        for (StudyListProblem problem : studyList.getProblems()) {
            root.add(new DefaultMutableTreeNode(problem));
        }
        for (StudyListSection section : studyList.getSections()) {
            root.add(toTreeNode(section));
        }
        studyTree.setModel(new DefaultTreeModel(root));
        for (int i = 0; i < root.getChildCount(); i++) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) root.getChildAt(i);
            if (child.getUserObject() instanceof StudyListSection) {
                studyTree.expandPath(new TreePath(child.getPath()));
            }
        }
        detailSubtitle.setText(
                studyList.getDescriptor().subtitle() +
                        "  ·  " +
                        studyList.getDescriptor().author() +
                        "  ·  " +
                        PropertiesUtils.getInfo("study.problem.count", String.valueOf(studyList.getProblemCount()))
        );
        detailCards.show(detailContent, TREE_CARD);
    }

    private static DefaultMutableTreeNode toTreeNode(StudyListSection section) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(section);
        for (StudyListProblem problem : section.getProblems()) {
            node.add(new DefaultMutableTreeNode(problem));
        }
        for (StudyListSection child : section.getSections()) {
            node.add(toTreeNode(child));
        }
        return node;
    }

    private void showLoadError(RuntimeException exception) {
        String detail = StringUtils.defaultIfBlank(exception.getMessage(), exception.getClass().getSimpleName());
        errorMessage.setText(PropertiesUtils.getInfo("study.load.failed") + ": " + detail);
        detailCards.show(detailContent, ERROR_CARD);
    }

    private void showIndex() {
        currentDescriptor = null;
        cards.show(this, INDEX_CARD);
    }

    private void openSource() {
        if (currentDescriptor != null) {
            BrowserUtil.browse(currentDescriptor.sourceUrl());
        }
    }

    private boolean isCurrent(StudyListDescriptor descriptor) {
        return currentDescriptor != null &&
                Objects.equals(currentDescriptor.sourceUrl(), descriptor.sourceUrl());
    }

    private void openSelectedProblem(TreePath path) {
        if (path == null) {
            return;
        }
        Object value = ((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject();
        if (value instanceof StudyListProblem problem) {
            ProgressManager.getInstance().run(new Task.Backgroundable(project, problem.title(), false) {
                @Override
                public void run(@NotNull ProgressIndicator indicator) {
                    CodeManager.openCode(problem.titleSlug(), project);
                }
            });
        }
    }

    private static class StudyListCellRenderer extends JPanel implements ListCellRenderer<StudyListDescriptor> {

        private final JBLabel title = new JBLabel();
        private final JBLabel subtitle = new JBLabel();

        private StudyListCellRenderer() {
            super();
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBorder(JBUI.Borders.empty(9, 10));
            setOpaque(true);
            title.setFont(JBFont.label().asBold());
            add(title);
            add(Box.createVerticalStrut(JBUI.scale(3)));
            add(subtitle);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends StudyListDescriptor> list,
                                                      StudyListDescriptor value,
                                                      int index,
                                                      boolean selected,
                                                      boolean cellHasFocus) {
            title.setText(value.title());
            subtitle.setText(value.subtitle() + "  ·  " + value.author());
            setBackground(selected ? list.getSelectionBackground() : list.getBackground());
            title.setForeground(selected ? list.getSelectionForeground() : list.getForeground());
            subtitle.setForeground(selected ? list.getSelectionForeground() : UIUtil.getContextHelpForeground());
            return this;
        }
    }

    private static class StudyTreeCellRenderer extends ColoredTreeCellRenderer {

        @Override
        public void customizeCellRenderer(@NotNull JTree tree,
                                          Object value,
                                          boolean selected,
                                          boolean expanded,
                                          boolean leaf,
                                          int row,
                                          boolean hasFocus) {
            Object userObject = ((DefaultMutableTreeNode) value).getUserObject();
            if (userObject instanceof StudyListSection section) {
                append(section.getTitle(), SimpleTextAttributes.REGULAR_BOLD_ATTRIBUTES);
                append(
                        "  " + PropertiesUtils.getInfo("study.problem.count", String.valueOf(section.getProblemCount())),
                        SimpleTextAttributes.GRAYED_ATTRIBUTES
                );
            } else if (userObject instanceof StudyListProblem problem) {
                String statusSign = getStatusSign(problem);
                if (StringUtils.isNotBlank(statusSign)) {
                    append(statusSign + " ", SimpleTextAttributes.REGULAR_ATTRIBUTES);
                }
                append(problem.title(), SimpleTextAttributes.REGULAR_ATTRIBUTES);
                if (StringUtils.isNotBlank(problem.note())) {
                    append("  " + problem.note(), SimpleTextAttributes.GRAYED_ATTRIBUTES);
                }
            }
        }

        private static String getStatusSign(StudyListProblem problem) {
            QuestionIndex questionIndex = QuestionManager.getQuestionIndex(problem.titleSlug());
            if (questionIndex == null) {
                return "";
            }
            QuestionView question = questionIndex.getQuestionView();
            return question == null ? "" : question.getStatusSign();
        }
    }
}
