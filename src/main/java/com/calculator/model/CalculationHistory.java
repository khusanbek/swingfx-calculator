package com.calculator.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * CalculationHistory - Model helper
 *
 * Stores a log of past calculations (max 50 entries).
 */
public class CalculationHistory {

    private static final int MAX_HISTORY = 50;
    private final List<HistoryEntry> entries = new ArrayList<>();

    public void addEntry(String expression, double result) {
        if (entries.size() >= MAX_HISTORY) entries.remove(0);
        entries.add(new HistoryEntry(expression, result));
    }

    public List<HistoryEntry> getEntries() {
        return Collections.unmodifiableList(entries);
    }

    public void clear() { entries.clear(); }

    public boolean isEmpty() { return entries.isEmpty(); }

    // ---- Inner class ----

    public static class HistoryEntry {
        private final String expression;
        private final double result;
        private final String timestamp;

        public HistoryEntry(String expression, double result) {
            this.expression = expression;
            this.result = result;
            this.timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        }

        public String getExpression()  { return expression; }
        public double getResult()      { return result; }
        public String getTimestamp()   { return timestamp; }

        public String getResultFormatted() {
            if (result == Math.floor(result) && Math.abs(result) < 1e15)
                return String.valueOf((long) result);
            return String.valueOf(result);
        }
    }
}
