package com.denispetrov.charting.example.drawable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.denispetrov.charting.drawable.impl.DrawableBase;
import com.denispetrov.charting.example.model.ExampleModel;
import com.denispetrov.charting.example.plugin.SimpleTrackableObject;
import com.denispetrov.charting.model.FPoint;
import com.denispetrov.charting.model.FRectangle;
import com.denispetrov.charting.plugin.Draggable;
import com.denispetrov.charting.plugin.Trackable;
import com.denispetrov.charting.plugin.TrackableObject;
import com.denispetrov.charting.plugin.impl.TrackerViewPlugin;
import com.denispetrov.charting.view.View;

public class ExampleModelDraggableRectDrawable extends DrawableBase implements Trackable, Draggable {
    private static final Logger LOG = LoggerFactory.getLogger(ExampleModelDraggableRectDrawable.class);

    private TrackerViewPlugin trackerViewPlugin;
    private Cursor cursor;

    @Override
    public void draw() {
        ExampleModel model = (ExampleModel) this.viewContext.getModel();
        for (FRectangle rect : model.getDraggableRectangles()) {
            viewContext.drawRectangle(rect.x, rect.y, rect.w, rect.h);
        }
    }

    @Override
    public void modelUpdated() {
        LOG.debug("model updated");
        ExampleModel model = (ExampleModel) this.viewContext.getModel();
        trackerViewPlugin.clearTrackingObjects(this);
        for (FRectangle rect : model.getDraggableRectangles()) {
            TrackableObject to = new SimpleTrackableObject();
            to.setTarget(rect);
            to.setFRect(rect);
            to.setIRect(view.getViewContext().rectangle(rect));
            to.setXPadding(1);
            to.setYPadding(1);
            trackerViewPlugin.addTrackingObject(this,to);
        }
    }

    @Override
    public void modelUpdated(Object component, Object item) {
        if (component == this && item != null) {
            TrackableObject to = (TrackableObject) item;
            FRectangle rect = (FRectangle) to.getTarget();
            to.setFRect(rect);
            to.setIRect(view.getViewContext().rectangle(rect));
            LOG.debug("Rectangle updated ({} {} {} {})", rect.x, rect.y, rect.w, rect.h);
        }
    }

    @Override
    public void setView(View view) {
        super.setView(view);
        trackerViewPlugin = view.findPlugin(TrackerViewPlugin.class);
        this.cursor = view.getCanvas().getDisplay().getSystemCursor(SWT.CURSOR_SIZEALL);
    }

    @Override
    public Cursor getCursor() {
        return cursor;
    }

    @Override
    public void setOrigin(Object object, FPoint origin) {
        FRectangle target = (FRectangle) object;
        target.x = origin.x;
        target.y = origin.y;
    }

    @Override
    public FPoint getOrigin(Object object) {
        FRectangle target = (FRectangle) object;
        return new FPoint(target.x, target.y);
    }

}
