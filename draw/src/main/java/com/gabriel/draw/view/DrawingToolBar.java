package com.gabriel.draw.view;

import com.gabriel.drawfx.ActionCommand;

import javax.swing.*;
import java.awt.event.ActionListener;

public class DrawingToolBar extends JToolBar {
    private final JButton lineButton = new JButton();
    private final JButton rectangleButton = new JButton();
    private final JButton ellipseButton = new JButton();
    private final JButton undoButton = new JButton();
    private final JButton redoButton = new JButton();
    private final JButton colorButton = new JButton();
    private final JButton selectButton = new JButton(); 
    private static DrawingToolBar instance;


    public DrawingToolBar(ActionListener actionListener) {
        super();
        instance = this;
        setFloatable(false);

        selectButton.setIcon(new ImageIcon(getClass().getResource("/images/select.png")));
        selectButton.setToolTipText("Select");
        selectButton.setActionCommand(ActionCommand.SELECT);
        selectButton.addActionListener(actionListener);
        add(selectButton);

        undoButton.setIcon(new ImageIcon(getClass().getResource("/images/undo.png")));
        undoButton.setToolTipText("Undo");
        undoButton.setActionCommand(ActionCommand.UNDO);
        undoButton.addActionListener(actionListener);
        add(undoButton);

        redoButton.setIcon(new ImageIcon(getClass().getResource("/images/redo.png")));
        redoButton.setToolTipText("Redo");
        redoButton.setActionCommand(ActionCommand.REDO);
        redoButton.addActionListener(actionListener);
        add(redoButton);

        lineButton.setIcon(new ImageIcon(getClass().getResource("/images/line.png")));
        lineButton.setToolTipText("Line");
        lineButton.setActionCommand(ActionCommand.LINE);
        lineButton.addActionListener(actionListener);
        add(lineButton);

        rectangleButton.setIcon(new ImageIcon(getClass().getResource("/images/rectangle.png")));
        rectangleButton.setToolTipText("Rectangle");
        rectangleButton.setActionCommand(ActionCommand.RECT);
        rectangleButton.addActionListener(actionListener);
        add(rectangleButton);

        ellipseButton.setIcon(new ImageIcon(getClass().getResource("/images/ellipse.png")));
        ellipseButton.setToolTipText("Ellipse");
        ellipseButton.setActionCommand(ActionCommand.ELLIPSE);
        ellipseButton.addActionListener(actionListener);
        add(ellipseButton);

        colorButton.setIcon(new ImageIcon(getClass().getResource("/images/color.png")));
        colorButton.setToolTipText("Color");
        colorButton.setActionCommand(ActionCommand.COLOR);
        colorButton.addActionListener(actionListener);
        add(colorButton);
    }


    public void updateUndoRedoButtons(boolean canUndo, boolean canRedo) {
        undoButton.setEnabled(canUndo);
        redoButton.setEnabled(canRedo);
    }

    public static DrawingToolBar getInstance() {
        return instance;
    }
}