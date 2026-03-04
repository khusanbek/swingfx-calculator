package com.calculator.controller;

import com.calculator.model.CalculatorModel;
import com.calculator.view.CalculatorView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * CalculatorController - MVC Controller Layer
 *
 * Receives input events from the View,
 * delegates logic to the Model,
 * and updates the View with results.
 */
public class CalculatorController {

    private final CalculatorModel model;
    private final CalculatorView view;

    public CalculatorController(CalculatorModel model, CalculatorView view) {
        this.model = model;
        this.view = view;
        wireHandlers();
    }

    private void wireHandlers() {
        view.setDigitHandler(this::onDigit);
        view.setDecimalHandler(this::onDecimal);
        view.setOperatorHandler(this::onOperator);
        view.setEqualsHandler(this::onEquals);
        view.setClearHandler(this::onClear);
        view.setToggleSignHandler(this::onToggleSign);
        view.setPercentageHandler(this::onPercentage);
        view.setBackspaceHandler(this::onBackspace);
        view.setHistoryToggleHandler(this::onHistoryToggle);
        view.setClearHistoryHandler(this::onClearHistory);
        view.setKeyHandler(this::onKeyPress);
    }

    private void onDigit(String digit) {
        model.inputDigit(digit);
        refreshDisplay();
    }

    private void onDecimal() {
        model.inputDecimal();
        refreshDisplay();
    }

    private void onOperator(String op) {
        model.setOperator(op);
        refreshDisplay();
    }

    private void onEquals() {
        model.calculate();
        refreshDisplay();
        refreshHistory();
    }

    private void onClear() {
        model.reset();
        refreshDisplay();
    }

    private void onToggleSign() {
        model.toggleSign();
        refreshDisplay();
    }

    private void onPercentage() {
        model.percentage();
        refreshDisplay();
    }

    private void onBackspace() {
        model.backspace();
        refreshDisplay();
    }

    private void onHistoryToggle() {
        view.toggleHistoryPanel();
    }

    private void onClearHistory() {
        model.getHistory().clear();
        refreshHistory();
    }

    private void onKeyPress(KeyEvent e) {
        String t = e.getText();
        KeyCode c = e.getCode();

        if (t.matches("[0-9]"))        onDigit(t);
        else if (t.equals("."))        onDecimal();
        else if (t.equals("+"))        onOperator("+");
        else if (t.equals("-"))        onOperator("-");
        else if (t.equals("*"))        onOperator("×");
        else if (t.equals("/"))        { e.consume(); onOperator("÷"); }
        else if (t.equals("%"))        onPercentage();
        else if (c == KeyCode.ENTER || t.equals("=")) onEquals();
        else if (c == KeyCode.BACK_SPACE) onBackspace();
        else if (c == KeyCode.ESCAPE)  onClear();
    }

    private void refreshDisplay() {
        view.updateDisplay(model.getDisplayValue());
        view.updateOperatorIndicator(model.getCurrentOperator());
    }

    private void refreshHistory() {
        view.updateHistory(model.getHistory().getEntries());
    }
}
