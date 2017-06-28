package com.denispetrov.charting.example.plugin;

import org.eclipse.swt.graphics.Rectangle;

import com.denispetrov.charting.model.FRectangle;
import com.denispetrov.charting.plugin.TrackableObject;
import com.denispetrov.charting.view.ViewContext;

public class SimpleTrackableObject implements TrackableObject {

    private final FRectangle fRect = new FRectangle(0,0,0,0);
    private final Rectangle iRect = new Rectangle(0,0,0,0);
    private final Rectangle paddedIRect = new Rectangle(0,0,0,0);
    private Object target = null;
    private int xPadding = 0, yPadding = 0;
    private boolean isPixelSized = false;

    /* (non-Javadoc)
     * @see com.denispetrov.graphics.plugin.TrackingObject#getfRect()
     */
    @Override
    public FRectangle getFRect() {
        return fRect;
    }

    /* (non-Javadoc)
     * @see com.denispetrov.graphics.plugin.TrackingObject#setfRect(com.denispetrov.graphics.model.FRectangle)
     */
    @Override
    public void setFRect(FRectangle fRect) {
        copyRect(fRect, this.fRect);
    }

    /* (non-Javadoc)
     * @see com.denispetrov.graphics.plugin.TrackingObject#getiRect()
     */
    @Override
    public Rectangle getIRect() {
        return iRect;
    }

    private static void copyRect(Rectangle src, Rectangle dst) {
        dst.x = src.x;
        dst.y = src.y;
        dst.width = src.width;
        dst.height = src.height;
    }

    private static void copyRect(FRectangle src, FRectangle dst) {
        dst.x = src.x;
        dst.y = src.y;
        dst.w = src.w;
        dst.h = src.h;
    }
    /* (non-Javadoc)
     * @see com.denispetrov.graphics.plugin.TrackingObject#setiRect(org.eclipse.swt.graphics.Rectangle)
     */
    @Override
    public void setIRect(Rectangle iRect) {
        copyRect(iRect, this.iRect);
        updatePaddedIRect();
    }

    /* (non-Javadoc)
     * @see com.denispetrov.graphics.plugin.TrackingObject#getTarget()
     */
    @Override
    public Object getTarget() {
        return target;
    }

    /* (non-Javadoc)
     * @see com.denispetrov.graphics.plugin.TrackingObject#setTarget(java.lang.Object)
     */
    @Override
    public void setTarget(Object target) {
        this.target = target;
    }

    /* (non-Javadoc)
     * @see com.denispetrov.graphics.plugin.TrackingObject#getxPadding()
     */
    @Override
    public int getxPadding() {
        return xPadding;
    }

    /* (non-Javadoc)
     * @see com.denispetrov.graphics.plugin.TrackingObject#setxPadding(int)
     */
    @Override
    public void setXPadding(int xPadding) {
        this.xPadding = xPadding;
        updatePaddedIRect();
    }

    /* (non-Javadoc)
     * @see com.denispetrov.graphics.plugin.TrackingObject#getyPadding()
     */
    @Override
    public int getyPadding() {
        return yPadding;
    }

    /* (non-Javadoc)
     * @see com.denispetrov.graphics.plugin.TrackingObject#setyPadding(int)
     */
    @Override
    public void setYPadding(int yPadding) {
        this.yPadding = yPadding;
        updatePaddedIRect();
    }

    @Override
    public void contextUpdated(ViewContext viewContext) {
        if (isPixelSized) {
            // only update pixel x and y but keep pixel width and height intact
            this.iRect.x = viewContext.x(this.fRect.x);
            this.iRect.y = viewContext.y(this.fRect.y);
            updatePaddedIRect();
        } else {
            copyRect(viewContext.rectangle(this.fRect),this.iRect);
            updatePaddedIRect();
        }
    }

    @Override
    public boolean isPixelSized() {
        return this.isPixelSized;
    }

    @Override
    public void setPixelSized(boolean isPixelSized) {
        this.isPixelSized = isPixelSized;
    }

    @Override
    public Rectangle getPaddedIRect() {
        return this.paddedIRect;
    }

    private void updatePaddedIRect() {
        this.paddedIRect.x = this.iRect.x - this.xPadding;
        this.paddedIRect.y = this.iRect.y - this.yPadding;
        this.paddedIRect.width = this.iRect.width + this.xPadding + this.xPadding;
        this.paddedIRect.height = this.iRect.height  + this.yPadding + this.yPadding;
    }
}
