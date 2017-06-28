package com.denispetrov.charting.example.drawable;

import com.denispetrov.charting.drawable.impl.DrawableBase;

public class ViewportYAxisDrawable extends DrawableBase {

    @Override
    public void draw() {
        int resizePosition = viewContext.y(viewContext.getBaseY() + viewContext.getHeight() * viewContext.getResizeCenterY());
        switch (viewContext.getXAxisRange()) {
        case FULL:
            drawLeftAxis(resizePosition);
            drawRightAxis(resizePosition);
            break;
        case POSITIVE_ONLY:
            drawLeftAxis(resizePosition);
            break;
        case NEGATIVE_ONLY:
            drawRightAxis(resizePosition);
            break;
        }
    }

    private void drawLeftAxis(int resizePosition) {
        viewContext.drawLine(
                viewContext.getBaseX(),
                viewContext.getBaseY(),
                viewContext.getBaseX(),
                viewContext.getBaseY() + viewContext.getHeight());
        viewContext.getGC().drawOval(viewContext.x(viewContext.getBaseX()) - 10, resizePosition - 3, 7, 7);
    }

    private void drawRightAxis(int resizePosition) {
        viewContext.drawLine(
                viewContext.getBaseX() + viewContext.getWidth(),
                viewContext.getBaseY(),
                viewContext.getBaseX() + viewContext.getWidth(),
                viewContext.getBaseY() + viewContext.getHeight());
        viewContext.getGC().drawOval(viewContext.x(viewContext.getBaseX() + viewContext.getWidth()) + 3, resizePosition - 3, 7, 7);
    }
}
