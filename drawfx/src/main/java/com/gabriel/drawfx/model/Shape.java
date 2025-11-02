package com.gabriel.drawfx.model;

import com.gabriel.drawfx.service.RendererService;
import lombok.Data;
import java.awt.*;

@Data
public abstract class Shape {
    int id;
    private Point location;
    private Point end;
    private Color color;
    private Color fill;
    private RendererService rendererService;
    private boolean selected = false;

    public Shape(Point location){
        this.setLocation(location);
        this.setEnd(location);
    }

    public abstract java.awt.Rectangle getBounds();
    public abstract boolean contains(Point p);

    public void move(int dx, int dy) {
        this.location.translate(dx, dy);
        this.end.translate(dx, dy);
    }
}