package com.gabriel.draw.service;

import com.gabriel.draw.model.Rectangle;
import com.gabriel.drawfx.model.Shape;
import com.gabriel.drawfx.service.RendererService;

import java.awt.*;

public class RectangleRendererService implements RendererService {
    @Override
    public void render(Graphics g, Shape shape, boolean xor) {
        Rectangle rect = (Rectangle) shape;
        Graphics2D g2d = (Graphics2D) g.create();

        if(xor) {
            g2d.setXORMode(shape.getColor());
        }
        else {
            g2d.setColor(shape.getColor());
        }

        
        java.awt.Rectangle bounds = rect.getBounds();

        
        g2d.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);

        
        if (shape.isSelected()) {
            
            g2d.setColor(Color.BLUE);
            g2d.setStroke(new BasicStroke(
                    1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{3, 3}, 0
            ));
            
            g2d.drawRect(bounds.x - 2, bounds.y - 2, bounds.width + 4, bounds.height + 4);

            
            int handleSize = 6;
            int halfH = handleSize / 2;

            
            Point[] handles = new Point[] {
                    new Point(bounds.x, bounds.y),                                       
                    new Point(bounds.x + bounds.width / 2, bounds.y),                    
                    new Point(bounds.x + bounds.width, bounds.y),                        
                    new Point(bounds.x + bounds.width, bounds.y + bounds.height / 2),    
                    new Point(bounds.x + bounds.width, bounds.y + bounds.height),        
                    new Point(bounds.x + bounds.width / 2, bounds.y + bounds.height),    
                    new Point(bounds.x, bounds.y + bounds.height),                       
                    new Point(bounds.x, bounds.y + bounds.height / 2)                    
            };

            for (Point p : handles) {
                
                g2d.setColor(Color.WHITE);
                g2d.fillRect(p.x - halfH, p.y - halfH, handleSize, handleSize);
                g2d.setColor(Color.BLACK);
                g2d.drawRect(p.x - halfH, p.y - halfH, handleSize, handleSize);
            }
        }
        g2d.dispose();
    }
}