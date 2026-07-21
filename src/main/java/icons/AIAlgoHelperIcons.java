package icons;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

public interface AIAlgoHelperIcons {

    Icon TOOL_WINDOW = load("/icons/AIAlgoHelper.svg");
    Icon EMPEROR_NEW_CLOTHES = load("/icons/emperor_new_clothes.svg");

    Icon CLEAN = load("/icons/clean.svg");
    Icon CLEAR = load("/icons/clear.svg");
    Icon COLLAPSE = load("/icons/collapse.svg");
    Icon CONFIG = load("/icons/config_lc.svg");
    Icon DESC = load("/icons/desc.svg");
    Icon EDIT_DOC = load("/icons/edit_doc.svg");
    Icon FAVORITE = load("/icons/favorite.svg");
    Icon FILTER = load("/icons/filter.svg");
    Icon FIND = load("/icons/find.svg");
    Icon HELP = load("/icons/help.svg");
    Icon HISTORY = load("/icons/history.svg");
    Icon LOGIN = load("/icons/login.svg");
    Icon LOGOUT = load("/icons/logout.svg");
    Icon POPUP = load("/icons/popup.svg");
    Icon POSITION = load("/icons/position.svg");
    Icon PROGRESS = load("/icons/progress.svg");
    Icon QUESTION = load("/icons/question.svg");
    Icon RANDOM = load("/icons/random.svg");
    Icon REFRESH = load("/icons/refresh.svg");
    Icon RUN = load("/icons/run.svg");
    Icon SOLUTION = load("/icons/solution.svg");
    Icon SUBMIT = load("/icons/submit.svg");
    Icon TIME = load("/icons/time.svg");
    Icon SORT_ASC = load("/icons/sortAsc.svg");
    Icon SORT_DESC = load("/icons/sortDesc.svg");
    Icon NOTE = load("/icons/note.svg");
    Icon LCV = load("/icons/lcv.svg");
    Icon SHARE = load("/icons/share.svg");
    Icon TOGGLE = load("/icons/toggle.svg");
    Icon SHOW = load("/icons/show.svg");
    Icon PULL = load("/icons/pull.svg");
    Icon PUSH = load("/icons/push.svg");

    private static Icon load(String path) {
        return IconLoader.getIcon(path, AIAlgoHelperIcons.class);
    }
}
