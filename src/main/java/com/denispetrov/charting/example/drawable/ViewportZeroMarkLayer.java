package com.denispetrov.charting.example.drawable;

import com.denispetrov.charting.layer.DrawableLayer;
import com.denispetrov.charting.layer.service.LayerAdapter;



public class ViewportZeroMarkLayer extends LayerAdapter implements DrawableLayer {

    @Override
    public void draw() {
        view.drawLine(-10, -10, 10, 10);
        view.drawLine(10, -10, -10, 10);
    }
}
