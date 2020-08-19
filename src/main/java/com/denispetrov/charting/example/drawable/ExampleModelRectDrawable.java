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
import com.denispetrov.charting.plugin.DrawablePlugin;
import com.denispetrov.charting.plugin.ModelAwarePlugin;
import com.denispetrov.charting.plugin.Trackable;
import com.denispetrov.charting.plugin.TrackableObject;
import com.denispetrov.charting.plugin.impl.PluginAdapter;
import com.denispetrov.charting.plugin.impl.SimpleTrackableObject;
import com.denispetrov.charting.plugin.impl.TrackerViewPlugin;
import com.denispetrov.charting.view.View;

public class ExampleModelRectDrawable extends PluginAdapter implements ModelAwarePlugin<ExampleModel>, DrawablePlugin, Trackable, Clickable {
    private static final Logger LOG = LoggerFactory.getLogger(ExampleModelRectDrawable.class);

    private TrackerViewPlugin trackerViewPlugin;
    private DrawParameters dp = new DrawParameters();
    private Cursor cursor;
    private ExampleModel model;

    public ExampleModelRectDrawable(TrackerViewPlugin trackerViewPlugin) {
        this.trackerViewPlugin = trackerViewPlugin;
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
        trackerViewPlugin.clearTrackableObjects(this);
        for (FRectangle rect : model.getRectangles()) {
            SimpleTrackableObject trackableObject = new SimpleTrackableObject();
            trackableObject.setTarget(rect);
            trackableObject.setFRect(new FRectangle(rect));
            trackableObject.setXPadding(1);
            trackableObject.setYPadding(1);
            trackerViewPlugin.addTrackableObject(this,trackableObject);
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
