package com.denispetrov.charting.example.drawable;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Rectangle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.denispetrov.charting.example.model.ExampleModel;
import com.denispetrov.charting.example.model.Label;
import com.denispetrov.charting.layer.ClickableLayer;
import com.denispetrov.charting.layer.DraggableLayer;
import com.denispetrov.charting.layer.DrawableLayer;
import com.denispetrov.charting.layer.ModelLayer;
import com.denispetrov.charting.layer.TrackableLayer;
import com.denispetrov.charting.layer.TrackableObject;
import com.denispetrov.charting.layer.drawable.DrawParameters;
import com.denispetrov.charting.layer.service.DraggerServiceLayer;
import com.denispetrov.charting.layer.service.LayerAdapter;
import com.denispetrov.charting.layer.service.TrackerServiceLayer;
import com.denispetrov.charting.layer.service.trackable.SimpleTrackableObject;
import com.denispetrov.charting.model.FPoint;
import com.denispetrov.charting.model.FRectangle;
import com.denispetrov.charting.view.View;

public class ExampleModelLabelLayer extends LayerAdapter implements ModelLayer<ExampleModel>, DrawableLayer, TrackableLayer, DraggableLayer, ClickableLayer {
    private static final Logger LOG = LoggerFactory.getLogger(ExampleModelLabelLayer.class);

    private TrackerServiceLayer trackerLayer;
    private DraggerServiceLayer draggerLayer;
    private Cursor cursor;
    private DrawParameters drawParameters = new DrawParameters();

    private TrackableObject objectDragged = null;

    private List<SimpleTrackableObject> trackableObjects = new ArrayList<>();

    public ExampleModelLabelLayer(TrackerServiceLayer trackerLayer, DraggerServiceLayer draggerLayer) {
        this.trackerLayer = trackerLayer;
        this.draggerLayer = draggerLayer;
    }

    @Override
    public void draw() {
        for (SimpleTrackableObject to : trackableObjects) {
            Label label = (Label) to.getTarget();
            Rectangle labelRect = view.drawText(
                    label.getText(),
                    label.getOrigin().x,
                    label.getOrigin().y,
                    drawParameters);
            FRectangle labelFRect = view.getViewContext().rectangle(labelRect);
            view.drawRectangle(labelFRect);
            to.setFRect(labelFRect);
        }
    }

    @Override
    public void modelUpdated(ExampleModel model) {
        LOG.debug("model updated");
        trackableObjects.clear();
        trackerLayer.clearTrackableObjects(this);
        for (Label label : model.getLabels()) {
            SimpleTrackableObject to = new SimpleTrackableObject();
            to.setTarget(label);
            trackerLayer.addTrackableObject(this,to);
            trackableObjects.add(to);
        }
    }

    @Override
    public void setOrigin(TrackableObject object, FPoint origin) {
        Label label = (Label) object.getTarget();
        label.getOrigin().x = origin.x;
        label.getOrigin().y = origin.y;
        view.getCanvas().redraw();
    }

    @Override
    public FPoint getOrigin(TrackableObject object) {
        Label label = (Label) object.getTarget();
        return new FPoint(label.getOrigin().x, label.getOrigin().y);
    }

    @Override
    public void setView(View view) {
        super.setView(view);
        this.cursor = view.getCanvas().getDisplay().getSystemCursor(SWT.CURSOR_HAND);
        drawParameters.xMargin = 5;
        drawParameters.yMargin = 5;
    }

    @Override
    public Cursor getCursor() {
        return cursor;
    }

    @Override
    public void objectClicked(TrackableObject object, int button) {
        Label label = (Label) object.getTarget();
        if (draggerLayer.isDragging()) {
            LOG.debug("Label {} click event received but dragging was active", label.getText());
        } else {
            LOG.debug("Label {} clicked", label.getText());
        }
    }

    @Override
    public void mouseDown(TrackableObject object, int button, int x, int y) {
        LOG.debug("Label Mouse Down {}", object);
        objectDragged = object;
        draggerLayer.beginDrag(this, objectDragged);
    }

    @Override
    public void mouseUp(int button, int x, int y) {
        LOG.debug("Label Mouse Up {}", button);
        if (objectDragged != null) {
            draggerLayer.endDrag();
            objectDragged = null;
        }
    }
}
