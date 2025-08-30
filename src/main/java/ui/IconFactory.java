package ui;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// SVG icon loader + cache
public final class IconFactory {
    private static final Logger log = LoggerFactory.getLogger(IconFactory.class);
    private static final Map<String, Icon> CACHE = new ConcurrentHashMap<>();

    private IconFactory() {}

    public static Icon get(String name, int size) {
        String key = name + "#" + size;
        return CACHE.computeIfAbsent(key, k -> {
            try {
                return new FlatSVGIcon("icons/" + name + ".svg", size, size);
            } catch (Exception ex) {
                log.warn("Icon '{}' ({}px) not found. Returning empty placeholder.", name, size, ex);
                return empty(size, size);
            }
        });
    }

    private static Icon empty(int w, int h) {
        return new Icon() {
            @Override public void paintIcon(java.awt.Component c, java.awt.Graphics g, int x, int y) {}
            @Override public int getIconWidth() { return w; }
            @Override public int getIconHeight() { return h; }
        };
    }
}
