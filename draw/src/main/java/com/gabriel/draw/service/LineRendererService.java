package com.gabriel.draw.service;

import com.gabriel.draw.model.Line;
import com.gabriel.drawfx.service.RendererService;
import com.gabriel.drawfx.model.Shape;

import java.awt.*;

public class LineRendererService implements RendererService {

    @Override
    public void render(Graphics g, Shape shape, boolean xor) {
        Line line = (Line) shape;
        Graphics2D g2d = (Graphics2D) g.create();

        if (xor) {
            g2d.setXORMode(shape.getColor());
        } else {
            g2d.setColor(shape.getColor());
        }

        
        g2d.drawLine(line.getLocation().x, line.getLocation().y, line.getEnd().x, line.getEnd().y);


        
        if (shape.isSelected()) {
            
            java.awt.Rectangle bounds = line.getBounds();

            
            g2d.setColor(Color.BLUE);
            g2d.setStroke(new BasicStroke(
                    1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{3, 3}, 0
            ));
            
            g2d.drawRect(bounds.x - 2, bounds.y - 2, bounds.width + 4, bounds.height + 4);

            
            int handleSize = 6;
            int halfH = handleSize / 2;
            int w = bounds.width;
            int h = bounds.height;

            
            Point[] handles = new Point[] {
                    new Point(bounds.x, bounds.y),                                       
                    new Point(bounds.x + w / 2, bounds.y),                    
                    new Point(bounds.x + w, bounds.y),                        
                    new Point(bounds.x + w, bounds.y + h / 2),    
                    new Point(bounds.x + w, bounds.y + h),        
                    new Point(bounds.x + w / 2, bounds.y + h),    
                    new Point(bounds.x, bounds.y + h),                       
                    new Point(bounds.x, bounds.y + h / 2)                    
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