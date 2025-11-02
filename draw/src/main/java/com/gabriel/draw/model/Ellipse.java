package com.gabriel.draw.model;

import com.gabriel.draw.service.EllipseRendererService;
import com.gabriel.drawfx.model.Shape;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Ellipse extends Shape {
    public Ellipse(Point start, Point end){
        super(start);
        this.setEnd(end);
        this.setColor(Color.RED);
        this.setRendererService(new EllipseRendererService());
    }

    @Override
    public java.awt.Rectangle getBounds() {
        int x = getLocation().x;
        int y = getLocation().y;
        int width = getEnd().x - getLocation().x;
        int height = getEnd().y - getLocation().y;

        if (width < 0) {
            x = getEnd().x;
            width = -width;
        }
        if (height < 0) {
            y = getEnd().y;
            height = -height;
        }
        return new java.awt.Rectangle(x, y, width, height);
    }

    @Override
    public boolean contains(Point p) {
        java.awt.Rectangle bounds = getBounds();
        Ellipse2D ellipse = new Ellipse2D.Double(bounds.x, bounds.y, bounds.width, bounds.height);
        return ellipse.contains(p);
    }
}