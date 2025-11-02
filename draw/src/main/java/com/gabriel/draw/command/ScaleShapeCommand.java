package com.gabriel.draw.command;

import com.gabriel.drawfx.command.Command;
import com.gabriel.drawfx.model.Shape;
import java.awt.Point;

public class ScaleShapeCommand implements Command {
    private final Shape shape;
    private final Point oldStart;
    private final Point oldEnd;
    private final Point newStart;
    private final Point newEnd;

    public ScaleShapeCommand(Shape shape, Point oldStart, Point oldEnd, Point newStart, Point newEnd) {
        this.shape = shape;
        this.oldStart = oldStart;
        this.oldEnd = oldEnd;
        this.newStart = newStart;
        this.newEnd = newEnd;
    }

    private void setShapePoints(Point start, Point end) {
        shape.getLocation().setLocation(start);
        shape.getEnd().setLocation(end);
    }

    @Override
    public void execute() {
        setShapePoints(newStart, newEnd);
    }

    @Override
    public void undo() {
        setShapePoints(oldStart, oldEnd);
    }

    @Override
    public void redo() {
        setShapePoints(newStart, newEnd);
    }
}