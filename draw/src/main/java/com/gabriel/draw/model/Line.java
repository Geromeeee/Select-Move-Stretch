package com.gabriel.draw.model;

import com.gabriel.draw.service.LineRendererService;
import lombok.Data;
import com.gabriel.drawfx.model.Shape;
import java.awt.*;

@Data
public class Line extends Shape {

    public Line(Point start, Point end){
        super(start);
        this.setEnd(end);
        this.setColor(Color.RED);
        this.setRendererService(new LineRendererService());
    }

    
    @Override
    public java.awt.Rectangle getBounds() {
        
        int x1 = getLocation().x;
        int y1 = getLocation().y;
        int x2 = getEnd().x;
        int y2 = getEnd().y;

        int minX = Math.min(x1, x2);
        int minY = Math.min(y1, y2);
        int maxX = Math.max(x1, x2);
        int maxY = Math.max(y1, y2);

        
        int padding = 2;

        return new java.awt.Rectangle(minX - padding, minY - padding, (maxX - minX) + 2 * padding, (maxY - minY) + 2 * padding);
    }

    @Override
    public boolean contains(Point p) {
        
        
        return getBounds().contains(p);
    }
}