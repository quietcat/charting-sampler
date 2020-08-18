package com.denispetrov.charting.example.drawable;

import com.denispetrov.charting.plugin.impl.PluginAdapter;
import com.denispetrov.charting.view.View;



public class ViewportZeroMarkDrawable<M> extends PluginAdapter<M> {

    @Override
    public void draw(View<M> view, M model) {
        view.drawLine(-10, -10, 10, 10);
        view.drawLine(10, -10, -10, 10);
    }
}
