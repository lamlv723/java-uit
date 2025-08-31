package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

// Icon helpers (SVG files in resources/icons)
public final class AppIcons {
    // Common icons
    public static final Icon USER_32 = IconFactory.get("user", 32);
    public static final Icon USER_64 = IconFactory.get("user", 64);
    public static final Icon LOCK_16 = IconFactory.get("lock", 16);
    public static final Icon LOGIN_24 = IconFactory.get("login", 24);
    public static final Icon DATABASE_24 = IconFactory.get("database", 24);
    public static final Icon HIBERNATE_24 = IconFactory.get("hibernate", 24);
    public static final Icon MYSQL_24 = IconFactory.get("mysql", 24);
    public static final Icon MAVEN_24 = IconFactory.get("maven", 24);

    private AppIcons() {}

    // Default multi-resolution window icons
    public static java.util.List<Image> windowIconImages() {
        return windowIconImages(IconName.APP);
    }

    /** Generate multi-resolution window icons for a given base icon name. */
    public static java.util.List<Image> windowIconImages(IconName iconName) {
        int[] sizes = {16, 24, 32, 48, 64, 128};
        List<Image> list = new ArrayList<>();
        for (int s : sizes) {
            list.add(toImage(IconFactory.get(iconName.fileName(), s), s, s));
        }
        return list;
    }

    private static Image toImage(Icon icon, int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        try {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            icon.paintIcon(null, g, 0, 0);
        } finally {
            g.dispose();
        }
        return img;
    }
}
