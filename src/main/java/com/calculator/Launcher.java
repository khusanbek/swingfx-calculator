package com.calculator;

/**
 * Launcher - Entry Point
 *
 * This plain class is used as the JAR Main-Class instead of
 * CalculatorApp directly. This is required because JavaFX fat JARs
 * fail with "JavaFX runtime components are missing" if the Main-Class
 * extends Application. This wrapper class avoids that problem.
 */
public class Launcher {
    public static void main(String[] args) {
        CalculatorApp.main(args);
    }
}
