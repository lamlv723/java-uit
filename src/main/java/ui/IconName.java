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
    CHEVRON_DOWN("chevron-down"),
    PLUS("plus"),
    BOXES("boxes"),
    CLIPBOARD_LIST("clipboard-list"),
    USERS("users"),
    BOX("box"),
    CHECK_CIRCLE("check-circle"),
    USER_CLOCK("user-clock"),
    ARROW_DOWN("arrow-down"),
    CLOCK("clock"),
    ARROW_UP("arrow-up"),
    TIMES("times"),
    EYE("eye"),
    EDIT("edit"),
    TRASH("trash"),
    CHECK("check"),
    LAPTOP("laptop"),
    CHART_LINE("chart-line"),
    COGS("cogs"),
    KEY("key"),
    SAVE("save"),
    WINDOW_MAXIMIZE("window-maximize"),
    MOUSE_POINTER("mouse-pointer"),
    PALETTE("palette"),
    FONT("font"),
    RULER_COMBINED("ruler-combined"),
    EXPAND_ARROWS_ALT("expand-arrows-alt"),
    HAND_POINTER("hand-pointer"),
    DATABASE("database"),
    HIBERNATE("hibernate"),
    MYSQL("mysql"),
    MAVEN("maven");

    private final String fileName;
    IconName(String fileName) { this.fileName = fileName; }
    public String fileName() { return fileName; }
    public Icon icon(int size) { return IconFactory.get(fileName, size); }
}
