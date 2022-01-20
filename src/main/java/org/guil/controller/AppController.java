package org.guil.controller;


import org.guil.model.ReadinData;
import org.guil.view.Gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class AppController {
    private Gui gui;
    private ReadinData reader;
    public AppController() {
        createGui();
        reader = new ReadinData();
    }

    private void createGui(){
        JFrame frame = new JFrame();
        this.gui = new Gui(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.pack();
        frame.setSize(400,400);
        frame.setLocationRelativeTo(null);
        frame.add(gui.getMainPanel(), BorderLayout.CENTER);
        frame.setVisible(true);
    }

    public void runProgram(File file){
        reader.read(file);
    }
}
