package com.gabriel.draw.controller;

import com.gabriel.draw.view.DrawingMenuBar;
import com.gabriel.draw.view.DrawingToolBar;
import com.gabriel.drawfx.ActionCommand;
import com.gabriel.drawfx.DrawMode;
import com.gabriel.drawfx.ShapeMode;
import com.gabriel.drawfx.service.AppService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionController implements ActionListener {
    AppService appService;

    public ActionController(AppService appService) {
        this.appService = appService;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        DrawingToolBar toolBar = DrawingToolBar.getInstance();
        DrawingMenuBar menuBar = DrawingMenuBar.getInstance();

        if (ActionCommand.UNDO.equals(cmd)) {
            appService.undo();
            menuBar.updateUndoRedoMenuItems(appService.canUndo(), appService.canRedo());
            toolBar.updateUndoRedoButtons(appService.canUndo(), appService.canRedo());
        }
        else if (ActionCommand.REDO.equals(cmd)) {
            appService.redo();
            menuBar.updateUndoRedoMenuItems(appService.canUndo(), appService.canRedo());
            toolBar.updateUndoRedoButtons(appService.canUndo(), appService.canRedo());
        }
        else if (ActionCommand.LINE.equals(cmd)) {
            appService.setShapeMode(ShapeMode.Line);
            appService.setDrawMode(DrawMode.Idle);
            appService.deselectAll();
            appService.repaint();
        }
        else if (ActionCommand.RECT.equals(cmd)) {
            appService.setShapeMode(ShapeMode.Rectangle);
            appService.setDrawMode(DrawMode.Idle);
            appService.deselectAll();
            appService.repaint();
        }
        else if (ActionCommand.ELLIPSE.equals(cmd)) {
            appService.setShapeMode(ShapeMode.Ellipse);
            appService.setDrawMode(DrawMode.Idle);
            appService.deselectAll();
            appService.repaint();
        }
        else if (ActionCommand.SELECT.equals(cmd)) {
            appService.setDrawMode(DrawMode.Select);
        }
        else if (ActionCommand.COLOR.equals(cmd)) {
            Color color = JColorChooser.showDialog(null, "Choose a Color", appService.getColor());
            appService.setColor(color);
        }
    }
}