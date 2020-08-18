package com.denispetrov.charting.example.drawable;

import com.denispetrov.charting.plugin.impl.PluginAdapter;
import com.denispetrov.charting.view.View;
import com.denispetrov.charting.view.ViewContext;

public class ViewportYAxisDrawable<M> extends PluginAdapter<M> {

    @Override
    public void draw(View<M> view, M model) {
        ViewContext viewContext = view.getViewContext();
        int resizePosition = viewContext.y(viewContext.getBaseY() + view.getHeight() * viewContext.getResizeCenterY());
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
        ViewContext viewContext = view.getViewContext();
        view.drawLine(
                viewContext.getBaseX(),
                viewContext.getBaseY(),
                viewContext.getBaseX(),
                viewContext.getBaseY() + view.getHeight());
        view.getGC().drawOval(viewContext.x(viewContext.getBaseX()) - 10, resizePosition - 3, 7, 7);
    }

    private void drawRightAxis(int resizePosition) {
        ViewContext viewContext = view.getViewContext();
        view.drawLine(
                viewContext.getBaseX() + view.getWidth(),
                viewContext.getBaseY(),
                viewContext.getBaseX() + view.getWidth(),
                viewContext.getBaseY() + view.getHeight());
        view.getGC().drawOval(viewContext.x(viewContext.getBaseX() + view.getWidth()) + 3, resizePosition - 3, 7, 7);
    }
}
