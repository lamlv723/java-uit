package ui;

import javax.swing.*;

// Logical icon names -> icons/*.svg
public enum IconName {
    APP("app"),
    USER("user"),
    LOCK("lock"),
    LOGIN("login"),
    SIGN_IN_ALT("sign-in-alt"),
    BUILDING("building"),
    PLUS("plus-dashboard"),
    BOXES("boxes-dashboard"),
    CLIPBOARD_LIST("clipboard-list-dashboard"),
    USERS("users-dashboard"),
    BOX("box-db"),
    CHECK_CIRCLE("check-circle-db"),
    USER_CLOCK("user-clock-db"),
    LAPTOP("laptop"),
    CHART_LINE("chart-line"),
    COGS("cogs"),
    EDIT("edit"),
    TRASH("trash"),
    RULER_COMBINED("ruler-combined");

    private final String fileName;

    IconName(String fileName) {
        this.fileName = fileName;
    }

    public String fileName() {
        return fileName;
    }

    public Icon icon(int size) {
        return IconFactory.get(fileName, size);
    }
}
