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

        JMenuBar menuBar = new JMenuBar();
        JMenu csvSettings = new JMenu();
        JMenuItem amountOfColumns = new JMenuItem();


        menuBar.add(csvSettings);
        menuBar.setLayout(new FlowLayout());
        menuBar.setAlignmentX(FlowLayout.LEFT);

        csvSettings.add(amountOfColumns);
        csvSettings.setText("csv settings");

        amountOfColumns.setText("amount of columns");
        amountOfColumns.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame1 = new JFrame();
                frame1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                // create a panel
                JPanel panel1 = new JPanel();
                panel1.setLayout(new BorderLayout());

                // create a slider
                JSpinner spinner = new JSpinner(new SpinnerNumberModel(1,0,20,1));
//                JSlider slider = new JSlider(0, 20, 1);
//
//                // paint the ticks and tracks
//                slider.setPaintTrack(true);
//                slider.setPaintTicks(true);
//                slider.setPaintLabels(true);
//
//                // set spacing
//                slider.setMajorTickSpacing(5);
//                slider.setMinorTickSpacing(1);


                JButton confirmButton = new JButton("Confirm");
                confirmButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        setAmountOfColumns((Integer) spinner.getValue());
                        frame1.dispose();
                    }
                });

                panel1.add(spinner, BorderLayout.NORTH);
                panel1.add(confirmButton, BorderLayout.CENTER);
                frame1.add(panel1);
                frame1.pack();
                frame1.setVisible(true);
            }
        });


        frame.add(gui.getMainPanel(), BorderLayout.CENTER);
        frame.add(menuBar, BorderLayout.PAGE_START);
        frame.setVisible(true);
    }

    public void runProgram(File file){
        reader.read(file);
    }

    public void setAmountOfColumns(int val){
        reader.setAmountOfColumns(val);
    }
}
