package com.denispetrov.charting.example.drawable;

import com.denispetrov.charting.drawable.impl.DrawableBase;



public class ViewportZeroMarkDrawable extends DrawableBase {

    @Override
    public void draw() {
        viewContext.drawLine(-10, -10, 10, 10);
        viewContext.drawLine(10, -10, -10, 10);
    }
}
