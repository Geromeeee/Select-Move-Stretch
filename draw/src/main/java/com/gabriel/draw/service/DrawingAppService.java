package com.gabriel.draw.service;

import com.gabriel.draw.view.DrawingView;
import com.gabriel.drawfx.DrawMode;
import com.gabriel.drawfx.ShapeMode;
import com.gabriel.drawfx.command.CommandService;
import com.gabriel.drawfx.model.Drawing;
import com.gabriel.drawfx.model.Shape;
import com.gabriel.drawfx.service.AppService;
import com.gabriel.drawfx.service.MoverService;
import com.gabriel.drawfx.service.ScalerService;

import javax.swing.*;
import java.awt.*;

public class DrawingAppService implements AppService {

    final private Drawing drawing;
    MoverService moverService;
    ScalerService scalerService;
    JPanel drawingView;
    public DrawingAppService(){
        drawing = new Drawing();
        moverService = new MoverService();
        scalerService = new ScalerService();
        drawing.setDrawMode(DrawMode.Idle);
        drawing.setShapeMode(ShapeMode.Ellipse);
        drawing.setColor(Color.BLACK);
    }

    @Override
    public void undo() {

    }

    @Override
    public void redo() {

    }

    @Override
    public boolean canUndo() {
        return CommandService.canUndo();
    }

    @Override
    public boolean canRedo() {
        return CommandService.canRedo();
    }

    @Override
    public ShapeMode getShapeMode() {
        return drawing.getShapeMode();
    }

    @Override
    public void setShapeMode(ShapeMode shapeMode) {
        drawing.setShapeMode(shapeMode);
    }

    @Override
    public DrawMode getDrawMode() {
        return drawing.getDrawMode();
    }

    @Override
    public void setDrawMode(DrawMode drawMode) {
        this.drawing.setDrawMode(drawMode);
    }

    @Override
    public Color getColor() {
        return drawing.getColor();
    }

    @Override
    public void setColor(Color color) {
        drawing.setColor(color);
    }

    @Override
    public Color getFill(){
        return drawing.getFill();
    }

    @Override
    public void setFill(Color color) {
        drawing.setFill(color);
    }

    @Override
    public void move(Shape shape, Point newLoc) {
        moverService.move(shape, newLoc);}

    @Override
    public void scale(Shape shape, Point newEnd) {
        shape.setEnd(newEnd);
    }

    @Override
    public void create(Shape shape) {
        shape.setId(this.drawing.getShapes().size());
        this.drawing.getShapes().add(shape);
    }

    @Override
    public void delete(Shape shape) {
        drawing.getShapes().remove(shape);
    }

    @Override
    public void close() {
        System.exit(0);
    }

    @Override
    public Object getModel() {
        return drawing;
    }

    @Override
    public JPanel getView() {
        return drawingView;
    }

    @Override
    public void setView(JPanel panel) {
        drawingView = panel;
    }

    @Override
    public void repaint() {
        drawingView.repaint();
    }

    
    @Override
    public Shape findShapeAt(int x, int y) {
        
        for (int i = drawing.getShapes().size() - 1; i >= 0; i--) {
            Shape shape = drawing.getShapes().get(i);
            if (shape.contains(new Point(x, y))) {
                return shape;
            }
        }
        return null;
    }

    @Override
    public void setSelectedShape(Shape shape) {
        
        drawing.getSelectedShapes().forEach(s -> s.setSelected(false));
        drawing.getSelectedShapes().clear();

        
        if (shape != null) {
            shape.setSelected(true);
            drawing.getSelectedShapes().add(shape);
        }
    }

    @Override
    public Shape getSelectedShape() {
        
        if (drawing.getSelectedShapes().isEmpty()) return null;
        return drawing.getSelectedShapes().get(0);
    }

    @Override
    public void deselectAll() {
        drawing.getSelectedShapes().forEach(s -> s.setSelected(false));
        drawing.getSelectedShapes().clear();
    }
}