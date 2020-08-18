package com.denispetrov.charting.example.drawable;

import java.util.Map;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.denispetrov.charting.drawable.DrawParameters;
import com.denispetrov.charting.example.model.ExampleModel;
import com.denispetrov.charting.model.FRectangle;
import com.denispetrov.charting.model.XAnchor;
import com.denispetrov.charting.model.YAnchor;
import com.denispetrov.charting.plugin.Clickable;
import com.denispetrov.charting.plugin.Trackable;
import com.denispetrov.charting.plugin.TrackableObject;
import com.denispetrov.charting.plugin.impl.PluginAdapter;
import com.denispetrov.charting.plugin.impl.SimpleTrackableObject;
import com.denispetrov.charting.plugin.impl.TrackerViewPlugin;
import com.denispetrov.charting.view.View;

public class ExampleModelRectDrawable extends PluginAdapter<ExampleModel> implements Trackable, Clickable {
    private static final Logger LOG = LoggerFactory.getLogger(ExampleModelRectDrawable.class);

    private TrackerViewPlugin<ExampleModel> trackerViewPlugin;
    private DrawParameters dp = new DrawParameters();
    private Cursor cursor;

    public ExampleModelRectDrawable(TrackerViewPlugin<ExampleModel> trackerViewPlugin) {
        this.trackerViewPlugin = trackerViewPlugin;
    }

    @Override
    public void draw(View<ExampleModel> view, ExampleModel model) {
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
    public void modelUpdated() {
        LOG.debug("model updated");
        ExampleModel model = view.getModel();
        trackerViewPlugin.clearTrackableObjects(this);
        for (FRectangle rect : model.getRectangles()) {
            SimpleTrackableObject trackingObject = new SimpleTrackableObject();
            trackingObject.setTarget(rect);
            trackingObject.setFRect(new FRectangle(rect));
            trackingObject.setXPadding(1);
            trackingObject.setYPadding(1);
            trackerViewPlugin.addTrackableObject(this,trackingObject);
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
    public void setView(View<ExampleModel> view) {
        super.setView(view);
        this.cursor = view.getCanvas().getDisplay().getSystemCursor(SWT.CURSOR_HAND);
    }

    @Override
    public Cursor getCursor() {
        return cursor;
    }

    @Override
    public void mouseDown(Map<Clickable,Set<TrackableObject>> objects, int button, int x, int y) {
    }

    @Override
    public void mouseUp(int button, int x, int y) {
    }

}
