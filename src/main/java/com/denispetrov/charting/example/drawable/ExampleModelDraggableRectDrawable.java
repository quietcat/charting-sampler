package com.denispetrov.charting.example.drawable;

import java.util.Map;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.denispetrov.charting.example.model.ExampleModel;
import com.denispetrov.charting.model.FPoint;
import com.denispetrov.charting.model.FRectangle;
import com.denispetrov.charting.plugin.Clickable;
import com.denispetrov.charting.plugin.Draggable;
import com.denispetrov.charting.plugin.Trackable;
import com.denispetrov.charting.plugin.TrackableObject;
import com.denispetrov.charting.plugin.impl.DraggerViewPlugin;
import com.denispetrov.charting.plugin.impl.PluginAdapter;
import com.denispetrov.charting.plugin.impl.SimpleTrackableObject;
import com.denispetrov.charting.plugin.impl.TrackerViewPlugin;
import com.denispetrov.charting.view.View;

public class ExampleModelDraggableRectDrawable extends PluginAdapter<ExampleModel> implements Trackable, Draggable, Clickable {
    private static final Logger LOG = LoggerFactory.getLogger(ExampleModelDraggableRectDrawable.class);

    private TrackerViewPlugin<ExampleModel> trackerViewPlugin;
    private DraggerViewPlugin<ExampleModel> draggerViewPlugin;
    private TrackableObject objectDragged;
    private Cursor cursor;

    public ExampleModelDraggableRectDrawable(TrackerViewPlugin<ExampleModel> trackerViewPlugin, DraggerViewPlugin<ExampleModel> draggerViewPlugin) {
        this.trackerViewPlugin = trackerViewPlugin;
        this.draggerViewPlugin = draggerViewPlugin;
    }

    @Override
    public void draw(View<ExampleModel> view, ExampleModel model) {
        for (FRectangle rect : model.getDraggableRectangles()) {
            view.drawRectangle(rect.x, rect.y, rect.w, rect.h);
        }
    }

    @Override
    public void modelUpdated() {
        LOG.debug("model updated");
        ExampleModel model = view.getModel();
        trackerViewPlugin.clearTrackableObjects(this);
        for (FRectangle rect : model.getDraggableRectangles()) {
            TrackableObject to = new SimpleTrackableObject();
            to.setTarget(rect);
            to.setFRect(rect);
            to.setIRect(view.getViewContext().rectangle(rect));
            to.setXPadding(1);
            to.setYPadding(1);
            trackerViewPlugin.addTrackableObject(this,to);
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
    public void setView(View<ExampleModel> view) {
        super.setView(view);
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

    @Override
    public void objectClicked(Set<TrackableObject> objects, int button) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseDown(Map<Clickable,Set<TrackableObject>> objects, int button, int x, int y) {
        LOG.debug("{}", objects);
        Set<TrackableObject> myObjects = objects.get(this);
        if (myObjects != null && myObjects.size() > 0) {
            objectDragged = myObjects.iterator().next();
            draggerViewPlugin.beginDrag(this, objectDragged);
        }
    }

    @Override
    public void mouseUp(int button, int x, int y) {
        if (objectDragged != null) {
            draggerViewPlugin.endDrag();
            objectDragged = null;
        }
    }

}
