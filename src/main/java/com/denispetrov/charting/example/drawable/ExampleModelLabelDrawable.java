package com.denispetrov.charting.example.drawable;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.denispetrov.charting.drawable.DrawParameters;
import com.denispetrov.charting.example.model.ExampleModel;
import com.denispetrov.charting.example.model.Label;
import com.denispetrov.charting.model.FPoint;
import com.denispetrov.charting.plugin.Clickable;
import com.denispetrov.charting.plugin.Draggable;
import com.denispetrov.charting.plugin.DrawablePlugin;
import com.denispetrov.charting.plugin.ModelAwarePlugin;
import com.denispetrov.charting.plugin.Trackable;
import com.denispetrov.charting.plugin.TrackableObject;
import com.denispetrov.charting.plugin.impl.DraggerViewPlugin;
import com.denispetrov.charting.plugin.impl.PluginAdapter;
import com.denispetrov.charting.plugin.impl.SimpleTrackableObject;
import com.denispetrov.charting.plugin.impl.TrackerViewPlugin;
import com.denispetrov.charting.view.View;

public class ExampleModelLabelDrawable extends PluginAdapter implements ModelAwarePlugin<ExampleModel>, DrawablePlugin, Trackable, Draggable, Clickable {
    private static final Logger LOG = LoggerFactory.getLogger(ExampleModelLabelDrawable.class);

    private TrackerViewPlugin trackerViewPlugin;
    private DraggerViewPlugin draggerViewPlugin;
    private Cursor cursor;
    private DrawParameters drawParameters = new DrawParameters();

//    private TrackableObject lastUpdatedTO = null;
    private boolean needToUpdateIRects = false;
    private TrackableObject objectDragged = null;

    private ExampleModel model;

    public ExampleModelLabelDrawable(TrackerViewPlugin trackerViewPlugin, DraggerViewPlugin draggerViewPlugin) {
        this.trackerViewPlugin = trackerViewPlugin;
        this.draggerViewPlugin = draggerViewPlugin;
    }

    @Override
    public void draw() {
        if (needToUpdateIRects) {
//            if (lastUpdatedTO == null) {
                // loop through trackable objects instead of the actual labels in the model
                // to update clickable areas, for which we need a GC that's only available during drawing
                Collection<TrackableObject> trackables = trackerViewPlugin.getTrackables(this);
                for (TrackableObject to : trackables) {
                    Label label = (Label) to.getTarget();
                    to.setIRect(view.textRectangle(label.getText(), label.getOrigin().x, label.getOrigin().y, drawParameters));
                    to.setFRect(view.getViewContext().rectangle(to.getIRect()));
                    view.drawText(label.getText(), label.getOrigin().x, label.getOrigin().y, drawParameters);
                }
//            } else {
//                // only need to update trackable object for one label
//                Label label = (Label) lastUpdatedTO.getTarget();
//                lastUpdatedTO.setIRect(view.textRectangle(
//                        label.getText(),
//                        label.getOrigin().x,
//                        label.getOrigin().y,
//                        drawParameters));
//                lastUpdatedTO.setFRect(view.getViewContext().rectangle(lastUpdatedTO.getIRect()));
//            }
            needToUpdateIRects = false;
        }
        drawLabels();
    }

    private void drawLabels() {
        for (Label label : model.getLabels()) {
            view.drawText(
                    label.getText(),
                    label.getOrigin().x,
                    label.getOrigin().y,
                    drawParameters);
            view.drawRectangle(view.getViewContext().rectangle(view.textRectangle(
                    label.getText(),
                    label.getOrigin().x,
                    label.getOrigin().y,
                    drawParameters)));
        }
    }

    @Override
    public void modelUpdated(ExampleModel model) {
        LOG.debug("model updated");
        this.model = model;
//        lastUpdatedTO = null;
        trackerViewPlugin.clearTrackableObjects(this);
        for (Label label : model.getLabels()) {
            TrackableObject to = new SimpleTrackableObject();
            to.setTarget(label);
            to.setPixelSized(true);
            to.setXPadding(1);
            to.setYPadding(1);
            trackerViewPlugin.addTrackableObject(this,to);
        }
        needToUpdateIRects = true;
    }

//    @Override
//    public void modelUpdated(ExampleModel model, Plugin component, Object item) {
//        if (component == this && item != null) {
//            lastUpdatedTO = (TrackableObject) item;
//            Label lastUpdatedLabel = (Label) lastUpdatedTO.getTarget();
//            LOG.debug("Label updated ({})", lastUpdatedLabel.getText());
//            needToUpdateIRects = true;
//        }
//    }

    @Override
    public void setOrigin(Object object, FPoint origin) {
        Label label = (Label) object;
        label.getOrigin().x = origin.x;
        label.getOrigin().y = origin.y;
        view.getCanvas().redraw();
    }

    @Override
    public FPoint getOrigin(Object object) {
        Label label = (Label) object;
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
//        lastUpdatedTO = null;
    }

    @Override
    public void mouseDown(Map<Clickable,Set<TrackableObject>> objects, int button, int x, int y) {
        LOG.debug("Mouse Down {}", objects);
        Set<TrackableObject> myObjects = objects.get(this);
        if ( myObjects != null && myObjects.size() > 0) {
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
