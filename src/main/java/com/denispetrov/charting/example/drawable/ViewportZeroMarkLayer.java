package com.denispetrov.charting.example.drawable;

import com.denispetrov.charting.layer.DrawableLayer;
import com.denispetrov.charting.layer.adapters.LayerAdapter;
import com.denispetrov.charting.view.ViewContext;



public class ViewportZeroMarkLayer extends LayerAdapter implements DrawableLayer {

    @Override
    public void draw() {
        ViewContext viewContext = view.getViewContext();
        double w = viewContext.w(10);
        double h = viewContext.h(10);
        view.drawLine(-w, -h, w, h);
        view.drawLine(w, -h, -w, h);
    }
}
