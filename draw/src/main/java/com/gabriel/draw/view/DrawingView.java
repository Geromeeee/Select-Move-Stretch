package com.gabriel.draw.view;

import com.gabriel.draw.controller.DrawingController;
import com.gabriel.draw.controller.DrawingWindowController;
import com.gabriel.drawfx.model.Drawing;
import com.gabriel.drawfx.model.Shape;
import com.gabriel.drawfx.service.AppService;

import javax.swing.*;
import java.awt.*;

public class DrawingView extends JPanel {
    AppService appService;
    private Shape shapePreview;

    public DrawingView(AppService appService){
        this.appService = appService;
        appService.setView(this);
        setBackground(Color.WHITE);

    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Drawing drawing = (Drawing) appService.getModel();
        for(Shape shape : drawing.getShapes()){
            shape.getRendererService().render(g, shape, false);
        }
        if (shapePreview != null) {
            shapePreview.getRendererService().render(g, shapePreview, false);
        }

    }

    public void createShapePreview(Shape shape) {
        this.shapePreview = shape;
        repaint();
    }
}