package com.denispetrov.charting.example.model;

import java.util.LinkedList;
import java.util.List;

import com.denispetrov.charting.model.FRectangle;

public class ExampleModel {

    private List<FRectangle> rectangles = new LinkedList<>();
    private List<FRectangle> draggableRectangles = new LinkedList<>();
    private List<Label> labels = new LinkedList<>();

    public List<FRectangle> getRectangles() {
        return rectangles;
    }

    public List<FRectangle> getDraggableRectangles() {
        return draggableRectangles;
    }

    public List<Label> getLabels() {
        return labels;
    }
}
