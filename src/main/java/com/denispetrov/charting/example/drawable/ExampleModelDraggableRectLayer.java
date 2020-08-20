package com.denispetrov.charting.example.drawable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.denispetrov.charting.example.model.ExampleModel;
import com.denispetrov.charting.layer.ClickableLayer;
import com.denispetrov.charting.layer.DraggableLayer;
import com.denispetrov.charting.layer.DrawableLayer;
import com.denispetrov.charting.layer.ModelLayer;
import com.denispetrov.charting.layer.TrackableLayer;
import com.denispetrov.charting.layer.TrackableObject;
import com.denispetrov.charting.layer.adapters.LayerAdapter;
import com.denispetrov.charting.layer.service.DraggerServiceLayer;
import com.denispetrov.charting.layer.service.TrackerServiceLayer;
import com.denispetrov.charting.layer.service.trackable.SimpleTrackableObject;
import com.denispetrov.charting.model.FPoint;
import com.denispetrov.charting.model.FRectangle;
import com.denispetrov.charting.view.View;

public class ExampleModelDraggableRectLayer extends LayerAdapter implements ModelLayer<ExampleModel>, DrawableLayer, TrackableLayer, DraggableLayer, ClickableLayer {
    private static final Logger LOG = LoggerFactory.getLogger(ExampleModelDraggableRectLayer.class);

    private TrackerServiceLayer trackerLayer;
    private DraggerServiceLayer draggerLayer;
//    private TrackableObject objectDragged;
    private Cursor cursor;

    private ExampleModel model;

    public ExampleModelDraggableRectLayer(TrackerServiceLayer trackerLayer, DraggerServiceLayer draggerLayer) {
        this.trackerLayer = trackerLayer;
        this.draggerLayer = draggerLayer;
    }

    @Override
    public void draw() {
        for (FRectangle rect : model.getDraggableRectangles()) {
            view.drawRectangle(rect.x, rect.y, rect.w, rect.h);
        }
    }

    @Override
    public void modelUpdated(ExampleModel model) {
        LOG.debug("model updated");
        this.model = model;
        trackerLayer.clearTrackableObjects(this);
        for (FRectangle rect : model.getDraggableRectangles()) {
            SimpleTrackableObject to = new SimpleTrackableObject();
            to.setTarget(rect);
            to.setFRect(rect);
            trackerLayer.addTrackableObject(this,to);
        }
    }

    @Override
    public void setView(View view) {
        super.setView(view);
        this.cursor = view.getCanvas().getDisplay().getSystemCursor(SWT.CURSOR_SIZEALL);
    }

    @Override
    public Cursor getCursor() {
        return cursor;
    }

    @Override
    public void setOrigin(TrackableObject object, FPoint origin) {
        FRectangle target = (FRectangle) object.getTarget();
        target.x = origin.x;
        target.y = origin.y;
        view.getCanvas().redraw();
    }

    @Override
    public FPoint getOrigin(TrackableObject object) {
        FRectangle target = (FRectangle) object.getTarget();
        return new FPoint(target.x, target.y);
    }

    @Override
    public void objectClicked(TrackableObject object, int button) {
        LOG.debug("Object clicked {} {}", object, button);
    }

    @Override
    public void mouseDown(TrackableObject object, int button, int x, int y) {
        LOG.debug("Mouse down {}", object);
        if (button == 1) {
            draggerLayer.beginDrag(this, object);
        }
    }

    @Override
    public void mouseUp(int button, int x, int y) {
        if (button == 1) {
            draggerLayer.endDrag();
        }
    }

}
