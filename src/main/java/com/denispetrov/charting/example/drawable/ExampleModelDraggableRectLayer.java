package com.denispetrov.charting.example.drawable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.wb.swt.SWTResourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.denispetrov.charting.example.model.ExampleModel;
import com.denispetrov.charting.layer.ClickableLayer;
import com.denispetrov.charting.layer.DraggableLayer;
import com.denispetrov.charting.layer.MouseAwareLayer;
import com.denispetrov.charting.layer.MouseEventType;
import com.denispetrov.charting.layer.TrackableLayer;
import com.denispetrov.charting.layer.TrackableObject;
import com.denispetrov.charting.layer.adapters.DrawableModelLayerAdapter;
import com.denispetrov.charting.layer.service.DraggerServiceLayer;
import com.denispetrov.charting.layer.service.TrackerServiceLayer;
import com.denispetrov.charting.layer.service.trackable.SimpleTrackableObject;
import com.denispetrov.charting.model.FPoint;
import com.denispetrov.charting.model.FRectangle;
import com.denispetrov.charting.view.View;
import com.denispetrov.charting.view.ViewContext;

public class ExampleModelDraggableRectLayer extends DrawableModelLayerAdapter<ExampleModel> implements MouseAwareLayer, TrackableLayer, DraggableLayer, ClickableLayer {
    private static final Logger LOG = LoggerFactory.getLogger(ExampleModelDraggableRectLayer.class);

    private TrackerServiceLayer trackerLayer;
    private DraggerServiceLayer draggerLayer;
//    private TrackableObject objectDragged;
    private Cursor cursor;

    private Color newRectColor = SWTResourceManager.getColor(SWT.COLOR_DARK_YELLOW);
    private Rectangle newRect;
    private Point initialSize = new Point(20,15);
    private boolean creatingNewRect = false;

    public ExampleModelDraggableRectLayer(TrackerServiceLayer trackerLayer, DraggerServiceLayer draggerLayer) {
        this.trackerLayer = trackerLayer;
        this.draggerLayer = draggerLayer;
    }

    @Override
    public void draw() {
        for (FRectangle rect : model.getDraggableRectangles()) {
            view.drawRectangle(rect);
        }
        if (newRect != null) {
            Color saveColor = view.getGC().getForeground();
            view.getGC().setForeground(newRectColor);
            view.getGC().drawRectangle(newRect);
            view.getGC().setForeground(saveColor);
        }
    }

    @Override
    public void modelUpdated(ExampleModel model) {
        LOG.debug("model updated");
        super.modelUpdated(model);
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

    @Override
    public boolean mouseEvent(MouseEventType eventType, MouseEvent e) {
        if (trackerLayer.getTopTrackableObjectUnderMouse() == null) {
            if (creatingNewRect) {
                ViewContext viewContext = view.getViewContext();
                switch (eventType) {
                case BUTTON_UP:
                    normalizeRectangle(newRect);
                    if (newRect.width > 0 && newRect.height > 0) {
                        model.getDraggableRectangles().add(viewContext.rectangle(newRect));
                        view.modelUpdated(model);
                    }
                    creatingNewRect = false;
                    newRect = null;
                    trackerLayer.showCursor();
                    view.getCanvas().redraw();
                    break;
                case MOVE:
                    newRect.width = e.x - newRect.x;
                    newRect.height = e.y - newRect.y;
                    view.getCanvas().redraw();
                    break;
                default:
                    break;
                }
            } else {
                if (eventType == MouseEventType.BUTTON_DOWN && e.button == 1) {
                    creatingNewRect = true;
                    newRect = new Rectangle(e.x, e.y, initialSize.x, initialSize.y);
                    view.getCanvas().toDisplay(e.x, e.y);
                    Display.getCurrent().setCursorLocation(
                            view.getCanvas().toDisplay(e.x + initialSize.x, e.y + initialSize.y));
                    trackerLayer.hideCursor();
                    view.getCanvas().redraw();
                }
            }
        }
        return false;
    }

    private static void normalizeRectangle(Rectangle rect) {
        if (rect.width < 0) {
            rect.width = -rect.width;
            rect.x -= rect.width;
        }
        if (rect.height < 0) {
            rect.height = -rect.height;
            rect.y -= rect.height;
        }
    }
}
