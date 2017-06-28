package com.denispetrov.charting.example.drawable;

import java.util.Collection;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.denispetrov.charting.drawable.DrawParameters;
import com.denispetrov.charting.drawable.impl.DrawableBase;
import com.denispetrov.charting.example.model.ExampleModel;
import com.denispetrov.charting.example.model.Label;
import com.denispetrov.charting.example.plugin.SimpleTrackableObject;
import com.denispetrov.charting.model.FPoint;
import com.denispetrov.charting.plugin.Clickable;
import com.denispetrov.charting.plugin.Draggable;
import com.denispetrov.charting.plugin.Trackable;
import com.denispetrov.charting.plugin.TrackableObject;
import com.denispetrov.charting.plugin.impl.TrackerViewPlugin;
import com.denispetrov.charting.view.View;

public class ExampleModelLabelDrawable extends DrawableBase implements Trackable, Draggable, Clickable {
    private static final Logger LOG = LoggerFactory.getLogger(ExampleModelLabelDrawable.class);

    private TrackerViewPlugin trackerViewPlugin;
    private Cursor cursor;
    private DrawParameters drawParameters = new DrawParameters();

    private TrackableObject lastUpdatedTO = null;
    private boolean needToUpdateIRects = false;

    @Override
    public void draw() {
        if (needToUpdateIRects) {
            if (lastUpdatedTO == null) {
                // loop through trackable objects instead of the actual labels in the model
                // to update clickable areas, for which we need a GC that's only available during drawing
                Collection<TrackableObject> trackables = trackerViewPlugin.getTrackables(this);
                for (TrackableObject to : trackables) {
                    Label label = (Label) to.getTarget();
                    to.setIRect(viewContext.textRectangle(label.getText(), label.getOrigin().x, label.getOrigin().y, drawParameters));
                    to.setFRect(viewContext.rectangle(to.getIRect()));
                    viewContext.drawText(label.getText(), label.getOrigin().x, label.getOrigin().y, drawParameters);
                }
            } else {
                // only need to update trackable object for one label
                Label label = (Label) lastUpdatedTO.getTarget();
                lastUpdatedTO.setIRect(viewContext.textRectangle(label.getText(), label.getOrigin().x, label.getOrigin().y, drawParameters));
                lastUpdatedTO.setFRect(viewContext.rectangle(lastUpdatedTO.getIRect()));
                drawLabels();
            }
            needToUpdateIRects = false;
        } else {
            drawLabels();
        }
    }

    private void drawLabels() {
        ExampleModel model = (ExampleModel) this.viewContext.getModel();
        for (Label label : model.getLabels()) {
            viewContext.drawText(label.getText(), label.getOrigin().x, label.getOrigin().y, drawParameters);
        }
    }

    @Override
    public void modelUpdated() {
        LOG.debug("model updated");
        ExampleModel model = (ExampleModel) this.viewContext.getModel();
        lastUpdatedTO = null;
        trackerViewPlugin.clearTrackingObjects(this);
        for (Label label : model.getLabels()) {
            TrackableObject to = new SimpleTrackableObject();
            to.setTarget(label);
            to.setPixelSized(true);
            to.setXPadding(1);
            to.setYPadding(1);
            trackerViewPlugin.addTrackingObject(this,to);
        }
        needToUpdateIRects = true;
    }

    @Override
    public void modelUpdated(Object component, Object item) {
        if (component == this && item != null) {
            lastUpdatedTO = (TrackableObject) item;
            Label lastUpdatedLabel = (Label) lastUpdatedTO.getTarget();
            LOG.debug("Label updated ({})", lastUpdatedLabel.getText());
            needToUpdateIRects = true;
        }
    }

    @Override
    public void setOrigin(Object object, FPoint origin) {
        Label label = (Label) object;
        label.getOrigin().x = origin.x;
        label.getOrigin().y = origin.y;
    }

    @Override
    public FPoint getOrigin(Object object) {
        Label label = (Label) object;
        return new FPoint(label.getOrigin().x, label.getOrigin().y);
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

    @Override
    public void objectClicked(Set<TrackableObject> objects, int button) {
        for (TrackableObject to : objects) {
            Label label = (Label) to.getTarget();
            LOG.debug("Label {} clicked", label.getText());
        }
    }

    @Override
    public void contextUpdated() {
        // force recalc of irects
        needToUpdateIRects = true;
        lastUpdatedTO = null;
    }
}
