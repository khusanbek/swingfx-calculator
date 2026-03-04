package com.calculator.model;

/**
 * CalculatorModel - MVC Model Layer
 *
 * Handles all calculator state and arithmetic.
 * Has no knowledge of the View or Controller.
 */
public class CalculatorModel {

    private double firstOperand;
    private double secondOperand;
    private String currentOperator;
    private String displayValue;
    private boolean isNewEntry;
    private boolean hasError;
    private final CalculationHistory history;

    public CalculatorModel() {
        this.history = new CalculationHistory();
        reset();
    }

    public void reset() {
        firstOperand = 0;
        secondOperand = 0;
        currentOperator = "";
        displayValue = "0";
        isNewEntry = true;
        hasError = false;
    }

    public void inputDigit(String digit) {
        if (hasError) return;
        if (isNewEntry || displayValue.equals("0")) {
            displayValue = digit;
            isNewEntry = false;
        } else {
            if (displayValue.length() < 15) {
                displayValue += digit;
            }
        }
    }

    public void inputDecimal() {
        if (hasError) return;
        if (isNewEntry) {
            displayValue = "0.";
            isNewEntry = false;
        } else if (!displayValue.contains(".")) {
            displayValue += ".";
        }
    }

    public void setOperator(String operator) {
        if (hasError) return;
        if (!currentOperator.isEmpty() && !isNewEntry) {
            calculate();
        }
        firstOperand = Double.parseDouble(displayValue);
        currentOperator = operator;
        isNewEntry = true;
    }

    public void calculate() {
        if (hasError || currentOperator.isEmpty()) return;

        secondOperand = Double.parseDouble(displayValue);
        double result;

        switch (currentOperator) {
            case "+": result = firstOperand + secondOperand; break;
            case "-": result = firstOperand - secondOperand; break;
            case "×": result = firstOperand * secondOperand; break;
            case "÷":
                if (secondOperand == 0) { setError("Cannot divide by zero"); return; }
                result = firstOperand / secondOperand;
                break;
            case "%": result = firstOperand % secondOperand; break;
            default: return;
        }

        String expression = formatNumber(firstOperand) + " " + currentOperator + " " + formatNumber(secondOperand);
        history.addEntry(expression, result);

        displayValue = formatResult(result);
        firstOperand = result;
        currentOperator = "";
        isNewEntry = true;
    }

    public void toggleSign() {
        if (hasError) return;
        double value = Double.parseDouble(displayValue);
        displayValue = formatResult(-value);
    }

    public void percentage() {
        if (hasError) return;
        double value = Double.parseDouble(displayValue);
        displayValue = formatResult(value / 100.0);
    }

    public void backspace() {
        if (hasError || isNewEntry) return;
        if (displayValue.length() > 1) {
            displayValue = displayValue.substring(0, displayValue.length() - 1);
            if (displayValue.equals("-")) displayValue = "0";
        } else {
            displayValue = "0";
        }
    }

    private void setError(String message) {
        displayValue = message;
        hasError = true;
        currentOperator = "";
        isNewEntry = true;
    }

    private String formatResult(double value) {
        if (Double.isInfinite(value) || Double.isNaN(value)) {
            setError("Math Error");
            return "Math Error";
        }
        if (value == Math.floor(value) && Math.abs(value) < 1e15) {
            return String.valueOf((long) value);
        }
        String result = String.format("%.10f", value);
        return result.replaceAll("0+$", "").replaceAll("\\.$", "");
    }

    private String formatNumber(double value) {
        if (value == Math.floor(value)) return String.valueOf((long) value);
        return String.valueOf(value);
    }

    // Getters
    public String getDisplayValue()    { return displayValue; }
    public String getCurrentOperator() { return currentOperator; }
    public boolean hasError()          { return hasError; }
    public CalculationHistory getHistory() { return history; }
}
