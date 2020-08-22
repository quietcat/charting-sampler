package com.denispetrov.charting.example.drawable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.denispetrov.charting.example.model.ExampleModel;
import com.denispetrov.charting.layer.ClickableLayer;
import com.denispetrov.charting.layer.DrawableLayer;
import com.denispetrov.charting.layer.ModelLayer;
import com.denispetrov.charting.layer.TrackableLayer;
import com.denispetrov.charting.layer.TrackableObject;
import com.denispetrov.charting.layer.adapters.LayerAdapter;
import com.denispetrov.charting.layer.drawable.DrawParameters;
import com.denispetrov.charting.layer.service.TrackerServiceLayer;
import com.denispetrov.charting.layer.service.trackable.SimpleTrackableObject;
import com.denispetrov.charting.model.FRectangle;
import com.denispetrov.charting.model.XAnchor;
import com.denispetrov.charting.model.YAnchor;
import com.denispetrov.charting.view.View;

public class ExampleModelRectLayer extends LayerAdapter implements ModelLayer<ExampleModel>, DrawableLayer, TrackableLayer, ClickableLayer {
    private static final Logger LOG = LoggerFactory.getLogger(ExampleModelRectLayer.class);

    private TrackerServiceLayer trackerLayer;
    private DrawParameters dp = new DrawParameters();
    private Cursor cursor;
    private ExampleModel model;

    public ExampleModelRectLayer(TrackerServiceLayer trackerLayer) {
        this.trackerLayer = trackerLayer;
    }

    @Override
    public void draw() {
        for (FRectangle rect : model.getRectangles()) {
            view.drawRectangle(rect.x, rect.y, rect.w, rect.h);
            view.drawLine(rect.x, rect.y + rect.h / 2, rect.x + rect.w, rect.y + rect.h / 2);
            dp.xAnchor = XAnchor.LEFT;
            dp.yAnchor = YAnchor.BOTTOM;
            view.drawText("LB", rect.x + rect.w, rect.y + rect.h, dp);
            dp.yAnchor = YAnchor.MIDDLE;
            view.drawText("LM", rect.x + rect.w, rect.y + rect.h / 2, dp);
            dp.yAnchor = YAnchor.TOP;
            view.drawText("LT", rect.x + rect.w, rect.y, dp);
            dp.xAnchor = XAnchor.RIGHT;
            dp.yAnchor = YAnchor.BOTTOM;
            view.drawText("RB", rect.x, rect.y + rect.h, dp);
            dp.yAnchor = YAnchor.MIDDLE;
            view.drawText("RM", rect.x, rect.y + rect.h / 2, dp);
            dp.yAnchor = YAnchor.TOP;
            view.drawText("RT", rect.x, rect.y, dp);
        }
    }

    @Override
    public void modelUpdated(ExampleModel model) {
        LOG.trace("model updated");
        this.model = model;
        trackerLayer.clearTrackableObjects(this);
        for (FRectangle rect : model.getRectangles()) {
            SimpleTrackableObject trackableObject = new SimpleTrackableObject();
            trackableObject.setTarget(rect);
            trackableObject.setFRect(new FRectangle(rect));
            trackerLayer.addTrackableObject(this,trackableObject);
        }
    }

    @Override
    public void objectClicked(TrackableObject object, int button) {
        FRectangle rect = (FRectangle) object.getTarget();
        LOG.debug("Rectangle {} {} {} {} clicked", rect.x, rect.y, rect.w, rect.h);
    }

    @Override
    public void setView(View view) {
        super.setView(view);
        this.cursor = view.getCanvas().getDisplay().getSystemCursor(SWT.CURSOR_HAND);
    }

    @Override
    public Cursor getCursor() {
        return cursor;
    }

    @Override
    public void mouseDown(TrackableObject object, int button, int x, int y) {
    }

    @Override
    public void mouseUp(int button, int x, int y) {
    }

}
