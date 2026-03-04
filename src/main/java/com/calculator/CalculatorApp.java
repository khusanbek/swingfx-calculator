package com.calculator;

import com.calculator.controller.CalculatorController;
import com.calculator.model.CalculatorModel;
import com.calculator.view.CalculatorView;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * CalculatorApp - JavaFX Application
 *
 * Wires the three MVC layers together:
 *   Model      → CalculatorModel
 *   View       → CalculatorView
 *   Controller → CalculatorController
 */
public class CalculatorApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        CalculatorModel model = new CalculatorModel();
        CalculatorView view = new CalculatorView(primaryStage);
        CalculatorController controller = new CalculatorController(model, view);
        view.initialize();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
