/*MIT License

Copyright (c) 2022 Guil02

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package org.guil.view;



import org.guil.controller.AppController;

import javax.swing.*;
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
