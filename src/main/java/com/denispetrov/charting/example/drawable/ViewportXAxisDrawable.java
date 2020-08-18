package com.denispetrov.charting.example.drawable;

import com.denispetrov.charting.plugin.impl.PluginAdapter;
import com.denispetrov.charting.view.View;
import com.denispetrov.charting.view.ViewContext;

public class ViewportXAxisDrawable<M> extends PluginAdapter<M> {

    @Override
    public void draw(View<M> view, M model) {
        ViewContext viewContext = view.getViewContext();
        int resizePosition = viewContext.x(viewContext.getBaseX() + view.getWidth() * viewContext.getResizeCenterX());
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
        ViewContext viewContext = view.getViewContext();
        view.drawLine(
                viewContext.getBaseX(),
                viewContext.getBaseY() + view.getHeight(),
                viewContext.getBaseX() + view.getWidth(),
                viewContext.getBaseY() + view.getHeight());
        view.getGC().drawOval(resizePosition - 3, viewContext.y(viewContext.getBaseY() + view.getHeight()) - 10, 7, 7);
    }

    private void drawBottomAxis(int resizePosition) {
        ViewContext viewContext = view.getViewContext();
        view.drawLine(
                viewContext.getBaseX(),
                viewContext.getBaseY(),
                viewContext.getBaseX() + view.getWidth(),
                viewContext.getBaseY());
        view.getGC().drawOval(resizePosition - 3, viewContext.y(viewContext.getBaseY()) + 3, 7, 7);
    }
}
