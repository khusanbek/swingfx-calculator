package com.calculator.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;

/**
 * ButtonFactory - View helper
 *
 * Creates consistently styled buttons for the calculator UI.
 */
public class ButtonFactory {

    public Button createDigitButton(String label) {
        Button btn = createBase(label);
        applyStyle(btn, "#333333", "white", "22px");
        addHover(btn, "#333333", "#4a4a4a", "white");
        return btn;
    }

    public Button createOperatorButton(String label) {
        Button btn = createBase(label);
        applyStyle(btn, "#ff9f0a", "white", "24px");
        addHover(btn, "#ff9f0a", "#ffb340", "white");
        return btn;
    }

    public Button createUtilButton(String label) {
        Button btn = createBase(label);
        applyStyle(btn, "#a5a5a5", "black", "20px");
        addHover(btn, "#a5a5a5", "#bdbdbd", "black");
        return btn;
    }

    public Button createEqualsButton() {
        Button btn = createBase("=");
        applyStyle(btn, "#ff9f0a", "white", "26px");
        addHover(btn, "#ff9f0a", "#ffb340", "white");
        return btn;
    }

    private Button createBase(String label) {
        Button btn = new Button(label);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setMaxHeight(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER);
        btn.setFocusTraversable(false);
        return btn;
    }

    private void applyStyle(Button btn, String bg, String fg, String fontSize) {
        btn.setStyle(
            "-fx-background-color: " + bg + ";" +
            "-fx-text-fill: " + fg + ";" +
            "-fx-font-size: " + fontSize + ";" +
            "-fx-background-radius: 0;" +
            "-fx-cursor: hand;"
        );
    }

    private void addHover(Button btn, String normalBg, String hoverBg, String fg) {
        String base = btn.getStyle();
        btn.setOnMouseEntered(e -> btn.setStyle(base.replace(normalBg, hoverBg)));
        btn.setOnMouseExited(e  -> btn.setStyle(base));
        btn.setOnMousePressed(e -> btn.setStyle(base.replace(normalBg, hoverBg)));
        btn.setOnMouseReleased(e -> btn.setStyle(base));
    }
}
