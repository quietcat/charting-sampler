package com.denispetrov.charting.example.drawable;

import com.denispetrov.charting.plugin.DrawablePlugin;
import com.denispetrov.charting.plugin.impl.PluginAdapter;



public class ViewportZeroMarkDrawable extends PluginAdapter implements DrawablePlugin {

    @Override
    public void draw() {
        view.drawLine(-10, -10, 10, 10);
        view.drawLine(10, -10, -10, 10);
    }
}
