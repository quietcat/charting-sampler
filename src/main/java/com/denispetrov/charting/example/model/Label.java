package com.denispetrov.charting.example.model;

import com.denispetrov.charting.model.FPoint;

public class Label {
    private String text = "";
    private FPoint origin = new FPoint(0,0);

    public Label(String string, double x, double y) {
        this.text = string;
        this.origin.x = x;
        this.origin.y = y;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public FPoint getOrigin() {
        return origin;
    }

    public void setOrigin(FPoint origin) {
        this.origin = origin;
    }
}
