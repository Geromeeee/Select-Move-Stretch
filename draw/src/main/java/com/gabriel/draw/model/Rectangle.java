package com.gabriel.draw.model;

import com.gabriel.draw.service.RectangleRendererService;
import com.gabriel.drawfx.model.Shape;

import java.awt.*;

public class Rectangle extends Shape {

    public Rectangle(Point start, Point end){
        super(start);
        this.setEnd(end);
        this.setColor(Color.RED);
        this.setRendererService(new RectangleRendererService());
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
        return getBounds().contains(p);
    }
}