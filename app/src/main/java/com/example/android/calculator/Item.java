package com.example.android.calculator;

public class Item {
    String formula;
    String solution;

    public Item(String name, String description) {
        formula = name;
        this.solution = description;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }
}
