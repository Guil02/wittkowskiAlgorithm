package org.guil.view;



import org.guil.controller.AppController;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Gui {
    private JButton fileChooserButton;
    private JPanel mainPanel;
    private JButton proceedButton;
    private JLabel warningLabel;

    private File chosenFile;
    private AppController controller;

    public Gui(AppController controller) {
        this.controller = controller;
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));


        warningLabel = new JLabel();
        warningLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        warningLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
        mainPanel.add(warningLabel);

        fileChooserButton = new JButton("Choose file");
        fileChooserButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        fileChooserButton.setAlignmentY(Component.CENTER_ALIGNMENT);
        mainPanel.add(fileChooserButton);

        proceedButton = new JButton("Proceed");
        proceedButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        proceedButton.setAlignmentY(Component.CENTER_ALIGNMENT);
        mainPanel.add(proceedButton);




        fileChooserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showFileChooser();
            }
        });

        proceedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(chosenFile!=null){
                    controller.runProgram(chosenFile);
                    warningLabel.setText("");
                }
                else{
                    warningLabel.setText("Please choose a file");
                }
            }
        });


    }

    private void showFileChooser() {
        JFileChooser chooser = new JFileChooser();
        FileFilter filter = new FileNameExtensionFilter(".csv", "csv");
        chooser.setFileFilter(filter);
        File workingDirectory = new File(System.getProperty("user.dir"));
        chooser.setCurrentDirectory(workingDirectory);
        int result = chooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            this.chosenFile = chooser.getSelectedFile();
            warningLabel.setText("");
            System.out.println(chosenFile.getName());
        }
    }

    public JPanel getMainPanel(){
        return mainPanel;
    }
}
