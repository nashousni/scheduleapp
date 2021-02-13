package com.schedule.utils;


import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.BlendMode;
import javafx.scene.text.Text;

import java.util.Objects;


public class Glyph extends Text {

    private final String fontFamily;

    private String size = null;

    private String color = null;

    private String color2 = null;

    public Glyph(String fontFamily, char c) {
        super(Character.toString(c));
        this.fontFamily = Objects.requireNonNull(fontFamily);
        updateStyle();
    }

    private void updateStyle() {
        StringBuilder style = new StringBuilder();
        style.append("-fx-font-family: ")
                .append(fontFamily);
        if (size != null) {
            style.append("; -fx-font-size: ")
                    .append(size);
        }
        if (color != null) {
            if (color2 == null) {
                style.append("; -fx-fill: ")
                        .append(color);
            } else {
                style.append("; -fx-fill: linear-gradient(").append(color).append(" 0%, ").append(color2).append(" 100%)");
            }
        }
        setStyle(style.toString());
    }

    public Glyph size(String size) {
        this.size = Objects.requireNonNull(size);
        updateStyle();
        return this;
    }

    public Glyph color(String color) {
        this.color = Objects.requireNonNull(color);
        updateStyle();
        return this;
    }

    public Glyph color2(String color2) {
        this.color2 = Objects.requireNonNull(color2);
        updateStyle();
        return this;
    }

    public Node stack(Node other) {
        Objects.requireNonNull(other);
        other.setBlendMode(BlendMode.SRC_ATOP);
        return new Group(this, other);
    }

    public static Glyph createAwesomeFont(char c) {
        return new Glyph("FontAwesome", c);
    }

    public static Glyph createZocial(char c) {
        return new Glyph("zocial", c);
    }
}
