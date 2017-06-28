package com.denispetrov.charting.example.drawable;

import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.denispetrov.charting.drawable.DrawParameters;
import com.denispetrov.charting.drawable.impl.DrawableBase;
import com.denispetrov.charting.example.model.ExampleModel;
import com.denispetrov.charting.example.plugin.SimpleTrackableObject;
import com.denispetrov.charting.model.FRectangle;
import com.denispetrov.charting.model.XAnchor;
import com.denispetrov.charting.model.YAnchor;
import com.denispetrov.charting.plugin.Clickable;
import com.denispetrov.charting.plugin.Trackable;
import com.denispetrov.charting.plugin.TrackableObject;
import com.denispetrov.charting.plugin.impl.TrackerViewPlugin;
import com.denispetrov.charting.view.View;

public class ExampleModelRectDrawable extends DrawableBase implements Trackable, Clickable {
    private static final Logger LOG = LoggerFactory.getLogger(ExampleModelRectDrawable.class);

    private TrackerViewPlugin trackerViewPlugin;
    private DrawParameters dp = new DrawParameters();
    private Cursor cursor;

    @Override
    public void draw() {
        ExampleModel model = (ExampleModel) this.viewContext.getModel();
        for (FRectangle rect : model.getRectangles()) {
            viewContext.drawRectangle(rect.x, rect.y, rect.w, rect.h);
            viewContext.drawLine(rect.x, rect.y + rect.h / 2, rect.x + rect.w, rect.y + rect.h / 2);
            dp.xAnchor = XAnchor.LEFT;
            dp.yAnchor = YAnchor.BOTTOM;
            viewContext.drawText("LB", rect.x + rect.w, rect.y + rect.h, dp);
            dp.yAnchor = YAnchor.MIDDLE;
            viewContext.drawText("LM", rect.x + rect.w, rect.y + rect.h / 2, dp);
            dp.yAnchor = YAnchor.TOP;
            viewContext.drawText("LT", rect.x + rect.w, rect.y, dp);
            dp.xAnchor = XAnchor.RIGHT;
            dp.yAnchor = YAnchor.BOTTOM;
            viewContext.drawText("RB", rect.x, rect.y + rect.h, dp);
            dp.yAnchor = YAnchor.MIDDLE;
            viewContext.drawText("RM", rect.x, rect.y + rect.h / 2, dp);
            dp.yAnchor = YAnchor.TOP;
            viewContext.drawText("RT", rect.x, rect.y, dp);
        }
    }

    @Override
    public void modelUpdated() {
        LOG.debug("model updated");
        ExampleModel model = (ExampleModel) this.viewContext.getModel();
        trackerViewPlugin.clearTrackingObjects(this);
        for (FRectangle rect : model.getRectangles()) {
            SimpleTrackableObject trackingObject = new SimpleTrackableObject();
            trackingObject.setTarget(rect);
            trackingObject.setFRect(new FRectangle(rect));
            trackingObject.setXPadding(1);
            trackingObject.setYPadding(1);
            trackerViewPlugin.addTrackingObject(this,trackingObject);
        }
    }

    @Override
    public void objectClicked(Set<TrackableObject> objects, int button) {
        for (TrackableObject o : objects) {
            FRectangle rect = (FRectangle) o.getTarget();
            LOG.debug("Rectangle {} {} {} {} clicked", rect.x, rect.y, rect.w, rect.h);
        }
    }

    @Override
    public void setView(View view) {
        super.setView(view);
        trackerViewPlugin = view.findPlugin(TrackerViewPlugin.class);
        this.cursor = view.getCanvas().getDisplay().getSystemCursor(SWT.CURSOR_HAND);
    }

    @Override
    public Cursor getCursor() {
        return cursor;
    }

}
