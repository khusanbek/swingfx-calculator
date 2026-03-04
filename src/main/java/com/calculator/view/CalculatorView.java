package com.calculator.view;

import com.calculator.model.CalculationHistory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;
import java.util.function.Consumer;

/**
 * CalculatorView - MVC View Layer
 *
 * Builds the JavaFX UI and exposes handler setters
 * for the Controller to attach behaviour to.
 * Contains no business logic.
 */
public class CalculatorView {

    private Label displayLabel;
    private Label operatorIndicator;
    private VBox historyBox;
    private VBox historyPanel;
    private boolean historyVisible = false;

    private final ButtonFactory factory = new ButtonFactory();
    private final Stage stage;

    // Callbacks set by Controller
    private Consumer<String> digitHandler;
    private Runnable decimalHandler;
    private Consumer<String> operatorHandler;
    private Runnable equalsHandler;
    private Runnable clearHandler;
    private Runnable toggleSignHandler;
    private Runnable percentageHandler;
    private Runnable backspaceHandler;
    private Runnable historyToggleHandler;
    private Runnable clearHistoryHandler;
    private Consumer<KeyEvent> keyHandler;

    public CalculatorView(Stage stage) {
        this.stage = stage;
    }

    public void initialize() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1c1c1e;");

        root.setCenter(buildCalculatorArea());

        historyPanel = buildHistoryPanel();
        historyPanel.setVisible(false);
        historyPanel.setManaged(false);
        root.setRight(historyPanel);

        Scene scene = new Scene(root, 320, 650);
        scene.setOnKeyPressed(e -> { if (keyHandler != null) keyHandler.accept(e); });

        stage.setScene(scene);
        stage.setTitle("Calculator");
        stage.setResizable(false);
        stage.show();
    }

    // ---- Build UI ----

    private VBox buildCalculatorArea() {
        VBox vbox = new VBox(0);
        vbox.setPrefWidth(320);
        VBox.setVgrow(vbox, Priority.ALWAYS);

        VBox displayArea = buildDisplayArea();
        GridPane buttonGrid = buildButtonGrid();
        VBox.setVgrow(buttonGrid, Priority.ALWAYS);

        vbox.getChildren().addAll(displayArea, buttonGrid);
        return vbox;
    }

    private VBox buildDisplayArea() {
        VBox area = new VBox(4);
        area.setPadding(new Insets(20, 20, 10, 20));
        area.setAlignment(Pos.BOTTOM_RIGHT);
        area.setStyle("-fx-background-color: #1c1c1e;");
        area.setPrefHeight(150);
        area.setMinHeight(150);
        area.setMaxHeight(150);

        // History toggle button
        Button histBtn = new Button("⏱");
        histBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #636366; -fx-font-size: 16px; -fx-cursor: hand;");
        histBtn.setOnAction(e -> { if (historyToggleHandler != null) historyToggleHandler.run(); });
        HBox topRow = new HBox();
        topRow.setAlignment(Pos.TOP_RIGHT);
        topRow.getChildren().add(histBtn);

        // Operator indicator
        operatorIndicator = new Label("");
        operatorIndicator.setStyle("-fx-text-fill: #ff9f0a; -fx-font-size: 18px;");
        operatorIndicator.setMaxWidth(Double.MAX_VALUE);
        operatorIndicator.setAlignment(Pos.CENTER_RIGHT);

        // Main display
        displayLabel = new Label("0");
        displayLabel.setStyle("-fx-text-fill: white; -fx-font-size: 52px; -fx-font-weight: 300;");
        displayLabel.setMaxWidth(Double.MAX_VALUE);
        displayLabel.setAlignment(Pos.CENTER_RIGHT);

        area.getChildren().addAll(topRow, operatorIndicator, displayLabel);
        return area;
    }

    private GridPane buildButtonGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(1);
        grid.setVgap(1);
        grid.setStyle("-fx-background-color: #3a3a3c;");
        grid.setPadding(new Insets(1));

        // Row 0: AC  +/-  %  ÷
        grid.add(utilBtn("AC",  () -> { if (clearHandler != null) clearHandler.run(); }),           0, 0);
        grid.add(utilBtn("+/-", () -> { if (toggleSignHandler != null) toggleSignHandler.run(); }), 1, 0);
        grid.add(utilBtn("%",   () -> { if (percentageHandler != null) percentageHandler.run(); }), 2, 0);
        grid.add(opBtn("÷"), 3, 0);

        // Row 1: 7  8  9  ×
        grid.add(digitBtn("7"), 0, 1);
        grid.add(digitBtn("8"), 1, 1);
        grid.add(digitBtn("9"), 2, 1);
        grid.add(opBtn("×"), 3, 1);

        // Row 2: 4  5  6  -
        grid.add(digitBtn("4"), 0, 2);
        grid.add(digitBtn("5"), 1, 2);
        grid.add(digitBtn("6"), 2, 2);
        grid.add(opBtn("-"), 3, 2);

        // Row 3: 1  2  3  +
        grid.add(digitBtn("1"), 0, 3);
        grid.add(digitBtn("2"), 1, 3);
        grid.add(digitBtn("3"), 2, 3);
        grid.add(opBtn("+"), 3, 3);

        // Row 4: ⌫  0  .  =
        grid.add(utilBtn("⌫", () -> { if (backspaceHandler != null) backspaceHandler.run(); }), 0, 4);
        grid.add(digitBtn("0"), 1, 4);
        grid.add(dotBtn(), 2, 4);
        grid.add(equalsBtn(), 3, 4);

        // Equal column widths
        for (int i = 0; i < 4; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(25);
            grid.getColumnConstraints().add(cc);
        }
        // Rows grow to fill remaining space equally
        for (int i = 0; i < 5; i++) {
            RowConstraints rc = new RowConstraints();
            rc.setPercentHeight(20);
            rc.setFillHeight(true);
            grid.getRowConstraints().add(rc);
        }

        return grid;
    }

    private VBox buildHistoryPanel() {
        VBox panel = new VBox(8);
        panel.setPrefWidth(220);
        panel.setPadding(new Insets(16));
        panel.setStyle("-fx-background-color: #2c2c2e;");

        Label title = new Label("History");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");

        Button clearBtn = new Button("Clear");
        clearBtn.setStyle("-fx-background-color: #3a3a3c; -fx-text-fill: #ff453a; -fx-font-size: 13px; -fx-cursor: hand; -fx-background-radius: 6;");
        clearBtn.setOnAction(e -> { if (clearHistoryHandler != null) clearHistoryHandler.run(); });

        HBox header = new HBox();
        HBox spacer = new HBox(); HBox.setHgrow(spacer, Priority.ALWAYS);
        header.setAlignment(Pos.CENTER_LEFT);
        header.getChildren().addAll(title, spacer, clearBtn);

        historyBox = new VBox(6);
        ScrollPane scroll = new ScrollPane(historyBox);
        scroll.setStyle("-fx-background: #2c2c2e; -fx-background-color: transparent;");
        scroll.setFitToWidth(true);
        VBox.setVgrow(scroll, Priority.ALWAYS);

        panel.getChildren().addAll(header, scroll);
        return panel;
    }

    // ---- Button helpers ----

    private Button digitBtn(String d) {
        Button b = factory.createDigitButton(d);
        b.setOnAction(e -> { if (digitHandler != null) digitHandler.accept(d); });
        return b;
    }

    private Button opBtn(String op) {
        Button b = factory.createOperatorButton(op);
        b.setOnAction(e -> { if (operatorHandler != null) operatorHandler.accept(op); });
        return b;
    }

    private Button dotBtn() {
        Button b = factory.createDigitButton(".");
        b.setOnAction(e -> { if (decimalHandler != null) decimalHandler.run(); });
        return b;
    }

    private Button equalsBtn() {
        Button b = factory.createEqualsButton();
        b.setOnAction(e -> { if (equalsHandler != null) equalsHandler.run(); });
        return b;
    }

    private Button utilBtn(String label, Runnable action) {
        Button b = factory.createUtilButton(label);
        b.setOnAction(e -> action.run());
        return b;
    }

    // ---- Public update methods (called by Controller) ----

    public void updateDisplay(String value) {
        displayLabel.setText(value);
        if (value.length() > 9)
            displayLabel.setStyle("-fx-text-fill: white; -fx-font-size: 30px; -fx-font-weight: 300;");
        else if (value.length() > 6)
            displayLabel.setStyle("-fx-text-fill: white; -fx-font-size: 40px; -fx-font-weight: 300;");
        else
            displayLabel.setStyle("-fx-text-fill: white; -fx-font-size: 52px; -fx-font-weight: 300;");
    }

    public void updateOperatorIndicator(String op) {
        operatorIndicator.setText(op);
    }

    public void updateHistory(List<CalculationHistory.HistoryEntry> entries) {
        historyBox.getChildren().clear();
        if (entries.isEmpty()) {
            Label empty = new Label("No history yet");
            empty.setStyle("-fx-text-fill: #636366; -fx-font-size: 13px;");
            historyBox.getChildren().add(empty);
            return;
        }
        for (int i = entries.size() - 1; i >= 0; i--) {
            CalculationHistory.HistoryEntry e = entries.get(i);
            VBox box = new VBox(2);
            box.setStyle("-fx-background-color: #3a3a3c; -fx-background-radius: 8; -fx-padding: 8 10 8 10;");
            Label expr   = new Label(e.getExpression());
            expr.setStyle("-fx-text-fill: #8e8e93; -fx-font-size: 12px;");
            Label result = new Label("= " + e.getResultFormatted());
            result.setStyle("-fx-text-fill: white; -fx-font-size: 15px; -fx-font-weight: bold;");
            Label time   = new Label(e.getTimestamp());
            time.setStyle("-fx-text-fill: #48484a; -fx-font-size: 10px;");
            box.getChildren().addAll(expr, result, time);
            historyBox.getChildren().add(box);
        }
    }

    public void toggleHistoryPanel() {
        historyVisible = !historyVisible;
        historyPanel.setVisible(historyVisible);
        historyPanel.setManaged(historyVisible);
        // Only change width — preserve height exactly as-is
        if (historyVisible) {
            stage.setWidth(stage.getWidth() + 220);
        } else {
            stage.setWidth(stage.getWidth() - 220);
        }
    }

    // ---- Handler setters (called by Controller) ----

    public void setDigitHandler(Consumer<String> h)    { digitHandler = h; }
    public void setDecimalHandler(Runnable h)           { decimalHandler = h; }
    public void setOperatorHandler(Consumer<String> h)  { operatorHandler = h; }
    public void setEqualsHandler(Runnable h)            { equalsHandler = h; }
    public void setClearHandler(Runnable h)             { clearHandler = h; }
    public void setToggleSignHandler(Runnable h)        { toggleSignHandler = h; }
    public void setPercentageHandler(Runnable h)        { percentageHandler = h; }
    public void setBackspaceHandler(Runnable h)         { backspaceHandler = h; }
    public void setHistoryToggleHandler(Runnable h)     { historyToggleHandler = h; }
    public void setClearHistoryHandler(Runnable h)      { clearHistoryHandler = h; }
    public void setKeyHandler(Consumer<KeyEvent> h)     { keyHandler = h; }
}
