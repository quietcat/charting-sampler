package com.denispetrov.charting.example.drawable;

import com.denispetrov.charting.drawable.impl.DrawableBase;

public class ViewportXAxisDrawable extends DrawableBase {

    @Override
    public void draw() {
        int resizePosition = viewContext.x(viewContext.getBaseX() + viewContext.getWidth() * viewContext.getResizeCenterX());
        switch (viewContext.getYAxisRange()) {
        case FULL:
            drawTopAxis(resizePosition);
            drawBottomAxis(resizePosition);
            break;
        case POSITIVE_ONLY:
            drawBottomAxis(resizePosition);
            break;
        case NEGATIVE_ONLY:
            drawTopAxis(resizePosition);
            break;
        }
    }

    private void drawTopAxis(int resizePosition) {
        viewContext.drawLine(
                viewContext.getBaseX(),
                viewContext.getBaseY() + viewContext.getHeight(),
                viewContext.getBaseX() + viewContext.getWidth(),
                viewContext.getBaseY() + viewContext.getHeight());
        viewContext.getGC().drawOval(resizePosition - 3, viewContext.y(viewContext.getBaseY() + viewContext.getHeight()) - 10, 7, 7);
    }

    private void drawBottomAxis(int resizePosition) {
        viewContext.drawLine(
                viewContext.getBaseX(),
                viewContext.getBaseY(),
                viewContext.getBaseX() + viewContext.getWidth(),
                viewContext.getBaseY());
        viewContext.getGC().drawOval(resizePosition - 3, viewContext.y(viewContext.getBaseY()) + 3, 7, 7);
    }
}
