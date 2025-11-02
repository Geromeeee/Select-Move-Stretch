package com.gabriel.draw.controller;

import com.gabriel.draw.command.MoveShapeCommand;
import com.gabriel.draw.command.ScaleShapeCommand;
import com.gabriel.draw.model.Ellipse;
import com.gabriel.draw.model.Line;
import com.gabriel.draw.model.Rectangle;
import com.gabriel.draw.view.DrawingMenuBar;
import com.gabriel.drawfx.DrawMode;
import com.gabriel.draw.view.DrawingView;
import com.gabriel.draw.view.DrawingToolBar;
import com.gabriel.drawfx.service.AppService;
import com.gabriel.drawfx.command.Command;
import com.gabriel.drawfx.command.CommandService;
import com.gabriel.drawfx.model.Drawing;
import com.gabriel.drawfx.model.Shape;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;

public class DrawingController implements MouseListener, MouseMotionListener {
    private Point end;
    final private DrawingView drawingView;
    DrawingToolBar toolBar = DrawingToolBar.getInstance();
    DrawingMenuBar menuBar = DrawingMenuBar.getInstance();

    Shape currentShape;
    private final AppService appService;

    private Point pressPoint;
    private Shape selectedShapeEnd;
    private Point oldStart;
    private Point oldEnd;
    private int scaleHandleIndex = -1; 

    public DrawingController(AppService appService, DrawingView drawingView) {
        this.appService = appService;
        this.drawingView = drawingView;
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    
    private java.awt.Rectangle getNormalizedBounds(Shape shape) {
        Point p1 = shape.getLocation();
        Point p2 = shape.getEnd();

        int x = Math.min(p1.x, p2.x);
        int y = Math.min(p1.y, p2.y);
        int width = Math.abs(p1.x - p2.x);
        int height = Math.abs(p1.y - p2.y);

        return new java.awt.Rectangle(x, y, width, height);
    }

    
    private int isOverHandle(Shape shape, Point p) {
        if (shape == null) return -1;

        final int HANDLE_SIZE = 30;
        final int HANDLE_HALF_SIZE = HANDLE_SIZE / 2;

        
        if (shape instanceof Line) {
            Point p1 = shape.getLocation();
            Point p2 = shape.getEnd();

            
            java.awt.Rectangle handle1 = new java.awt.Rectangle(p1.x - HANDLE_HALF_SIZE, p1.y - HANDLE_HALF_SIZE, HANDLE_SIZE, HANDLE_SIZE);
            if (handle1.contains(p)) {
                return 0;
            }

            
            java.awt.Rectangle handle2 = new java.awt.Rectangle(p2.x - HANDLE_HALF_SIZE, p2.y - HANDLE_HALF_SIZE, HANDLE_SIZE, HANDLE_SIZE);
            if (handle2.contains(p)) {
                return 4;
            }

            return -1;
        }

        
        
        java.awt.Rectangle b = getNormalizedBounds(shape);

        int w = b.width;
        int h = b.height;

        
        java.awt.Rectangle[] hitBoxes = new java.awt.Rectangle[] {
                
                new java.awt.Rectangle(b.x - HANDLE_HALF_SIZE, b.y - HANDLE_HALF_SIZE, HANDLE_SIZE, HANDLE_SIZE),
                
                new java.awt.Rectangle(b.x + w / 2 - HANDLE_HALF_SIZE, b.y - HANDLE_HALF_SIZE, HANDLE_SIZE, HANDLE_SIZE),
                
                new java.awt.Rectangle(b.x + w - HANDLE_HALF_SIZE, b.y - HANDLE_HALF_SIZE, HANDLE_SIZE, HANDLE_SIZE),
                
                new java.awt.Rectangle(b.x + w - HANDLE_HALF_SIZE, b.y + h / 2 - HANDLE_HALF_SIZE, HANDLE_SIZE, HANDLE_SIZE),
                
                new java.awt.Rectangle(b.x + w - HANDLE_HALF_SIZE, b.y + h - HANDLE_HALF_SIZE, HANDLE_SIZE, HANDLE_SIZE),
                
                new java.awt.Rectangle(b.x + w / 2 - HANDLE_HALF_SIZE, b.y + h - HANDLE_HALF_SIZE, HANDLE_SIZE, HANDLE_SIZE),
                
                new java.awt.Rectangle(b.x - HANDLE_HALF_SIZE, b.y + h - HANDLE_HALF_SIZE, HANDLE_SIZE, HANDLE_SIZE),
                
                new java.awt.Rectangle(b.x - HANDLE_HALF_SIZE, b.y + h / 2 - HANDLE_HALF_SIZE, HANDLE_SIZE, HANDLE_SIZE)
        };

        for (int i = 0; i < hitBoxes.length; i++) {
            
            if (hitBoxes[i].contains(p)) {
                return i;
            }
        }
        return -1;
    }


    @Override
    public void mousePressed(MouseEvent e) {
        pressPoint = e.getPoint();
        selectedShapeEnd = null;
        scaleHandleIndex = -1;

        if (appService.getDrawMode() == DrawMode.Select) {

            Drawing drawing = (Drawing) appService.getModel();
            List<Shape> shapes = drawing.getShapes();

            
            
            for (int i = shapes.size() - 1; i >= 0; i--) {
                Shape s = shapes.get(i);

                int handleIndex = isOverHandle(s, pressPoint);

                if (handleIndex != -1) {
                    
                    appService.setSelectedShape(s);
                    selectedShapeEnd = s;

                    oldStart = (Point) s.getLocation().clone();
                    oldEnd = (Point) s.getEnd().clone();
                    scaleHandleIndex = handleIndex;

                    appService.setDrawMode(DrawMode.Scaling);
                    appService.repaint();
                    return; 
                }
            }


            Shape hitShape = appService.findShapeAt(e.getX(), e.getY());

            if (hitShape != null) {
                appService.setSelectedShape(hitShape);
                selectedShapeEnd = hitShape;
                appService.repaint();

                oldStart = (Point) hitShape.getLocation().clone();
                oldEnd = (Point) hitShape.getEnd().clone();

                appService.setDrawMode(DrawMode.Moving);
            } else {
                appService.deselectAll();
                appService.repaint();
                appService.setDrawMode(DrawMode.Select);
            }
            return;
        }

        if (appService.getDrawMode() == DrawMode.Idle) {
            Point start = e.getPoint();
            switch (appService.getShapeMode()) {
                case Line:
                    currentShape = new Line(start, start);
                    break;
                case Rectangle:
                    currentShape = new Rectangle(start, start);
                    break;
                case Ellipse:
                    currentShape = new Ellipse(start, start);
                    break;
                default:
                    return;
            }
            currentShape.setColor(appService.getColor());
            appService.setDrawMode(DrawMode.MousePressed);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (appService.getDrawMode() == DrawMode.Moving) {
            if (selectedShapeEnd != null) {
                Point finalStart = (Point) selectedShapeEnd.getLocation().clone();
                Point finalEnd = (Point) selectedShapeEnd.getEnd().clone();

                if (!finalStart.equals(oldStart) || !finalEnd.equals(oldEnd)) {
                    Command moveCommand = new MoveShapeCommand(
                            selectedShapeEnd, oldStart, oldEnd, finalStart, finalEnd
                    );
                    CommandService.ExecuteCommand(moveCommand);
                }

                appService.setDrawMode(DrawMode.Select);
                menuBar.updateUndoRedoMenuItems(appService.canUndo(), appService.canRedo());
                toolBar.updateUndoRedoButtons(appService.canUndo(), appService.canRedo());
                appService.repaint();
            }

            
        } else if (appService.getDrawMode() == DrawMode.Scaling) {
            if (selectedShapeEnd != null) {
                Point finalStart = (Point) selectedShapeEnd.getLocation().clone();
                Point finalEnd = (Point) selectedShapeEnd.getEnd().clone();

                Command scaleCommand = new ScaleShapeCommand(
                        selectedShapeEnd, oldStart, oldEnd, finalStart, finalEnd
                );
                CommandService.ExecuteCommand(scaleCommand);

                scaleHandleIndex = -1;
                appService.setDrawMode(DrawMode.Select);
                menuBar.updateUndoRedoMenuItems(appService.canUndo(), appService.canRedo());
                toolBar.updateUndoRedoButtons(appService.canUndo(), appService.canRedo());
                appService.repaint();
            }

        } else if (appService.getDrawMode() == DrawMode.MousePressed) {
            end = e.getPoint();
            appService.scale(currentShape, end);
            appService.create(currentShape);
            drawingView.createShapePreview(null);
            menuBar.updateUndoRedoMenuItems(appService.canUndo(), appService.canRedo());
            toolBar.updateUndoRedoButtons(appService.canUndo(), appService.canRedo());
            appService.setDrawMode(DrawMode.Idle);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int dx = e.getX() - pressPoint.x;
        int dy = e.getY() - pressPoint.y;

        if (appService.getDrawMode() == DrawMode.Moving) {
            if (selectedShapeEnd != null) {
                Point finalStart = new Point(oldStart.x + dx, oldStart.y + dy);
                Point finalEnd = new Point(oldEnd.x + dx, oldEnd.y + dy);

                selectedShapeEnd.getLocation().setLocation(finalStart);
                selectedShapeEnd.getEnd().setLocation(finalEnd);
                appService.repaint();
            }

        } else if (appService.getDrawMode() == DrawMode.Scaling) {
            if (selectedShapeEnd != null && scaleHandleIndex != -1) {

                int mouseX = e.getX();
                int mouseY = e.getY();

                int newX1 = 0, newY1 = 0, newX2 = 0, newY2 = 0; 

                if (selectedShapeEnd instanceof Line) {

                    if (scaleHandleIndex == 0) { 
                        newX1 = mouseX; newY1 = mouseY; 
                        newX2 = oldEnd.x; newY2 = oldEnd.y; 
                    }
                    else if (scaleHandleIndex == 4) { 
                        newX1 = oldStart.x; newY1 = oldStart.y; 
                        newX2 = mouseX; newY2 = mouseY; 
                    } else {
                        
                        return;
                    }
                }

                
                else {
                    
                    java.awt.Rectangle originalBounds = new java.awt.Rectangle(
                            Math.min(oldStart.x, oldEnd.x),
                            Math.min(oldStart.y, oldEnd.y),
                            Math.abs(oldStart.x - oldEnd.x),
                            Math.abs(oldStart.y - oldEnd.y)
                    );

                    
                    switch (scaleHandleIndex) {
                        
                        case 4: 
                            newX1 = originalBounds.x; newY1 = originalBounds.y; 
                            newX2 = mouseX; newY2 = mouseY; 
                            break;
                        case 0: 
                            newX1 = originalBounds.x + originalBounds.width; newY1 = originalBounds.y + originalBounds.height; 
                            newX2 = mouseX; newY2 = mouseY; 
                            break;
                        case 2: 
                            newX1 = originalBounds.x; newY1 = originalBounds.y + originalBounds.height; 
                            newX2 = mouseX; newY2 = mouseY; 
                            break;
                        case 6: 
                            newX1 = originalBounds.x + originalBounds.width; newY1 = originalBounds.y; 
                            newX2 = mouseX; newY2 = mouseY; 
                            break;

                        
                        case 1: 
                            newX1 = originalBounds.x; newY1 = mouseY; 
                            newX2 = originalBounds.x + originalBounds.width; newY2 = originalBounds.y + originalBounds.height; 
                            break;
                        case 5: 
                            newX1 = originalBounds.x; newY1 = originalBounds.y; 
                            newX2 = originalBounds.x + originalBounds.width; newY2 = mouseY; 
                            break;
                        case 3: 
                            newX1 = originalBounds.x; newY1 = originalBounds.y; 
                            newX2 = mouseX; newY2 = originalBounds.y + originalBounds.height; 
                            break;
                        case 7: 
                            newX1 = mouseX; newY1 = originalBounds.y; 
                            newX2 = originalBounds.x + originalBounds.width; newY2 = originalBounds.y + originalBounds.height; 
                            break;
                        default:
                            return;
                    }
                }

                
                
                selectedShapeEnd.getLocation().setLocation(newX1, newY1);
                selectedShapeEnd.getEnd().setLocation(newX2, newY2);

                appService.repaint();
            }
        }
        
        else if (appService.getDrawMode() == DrawMode.MousePressed) {
            end = e.getPoint();
            appService.scale(currentShape, end);
            drawingView.createShapePreview(currentShape);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}