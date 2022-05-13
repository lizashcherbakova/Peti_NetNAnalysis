package view;

import controller.Controller;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class MenuView extends JMenu {
    Controller controller;
    JFrame frameParent;

    public MenuView(Controller controller, JFrame parent) {
        super("File");
        this.frameParent = parent;
        this.controller = controller;
        JMenuItem open = new JMenuItem("Open");
        open.addActionListener(e-> {
            var res = JOptionPane.showConfirmDialog(frameParent, "Do you want to save current work?");
                if (res == JOptionPane.YES_OPTION) {
                    callSaveScenario();
                    callOpenScenario();
                } else if(res ==JOptionPane.NO_OPTION) {
                    callOpenScenario();
                }
        });
        JMenuItem save = new JMenuItem("Save");
        save.addActionListener(e-> {
                callSaveScenario();
        });
        add(open);
        add(save);
    }

    JFileChooser createFileChooser(String title) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(title);
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Binary files", "bin");
        fileChooser.setFileFilter(filter);
        return fileChooser;
    }

    void callSaveScenario() {
        JFileChooser fileChooser = createFileChooser("Specify a path to save");

        int userSelection = fileChooser.showSaveDialog(frameParent);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            if(!controller.requestSave(fileToSave.getAbsolutePath())){
                showMistake();
            }
        }
    }
    void callOpenScenario() {
        JFileChooser fileChooser = createFileChooser("Choose file to open");
        int result = fileChooser.showOpenDialog(frameParent);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if(!controller.requestLoad(selectedFile.getAbsolutePath())){
                showMistake();
            }
        }
    }

    void showMistake() {
        JOptionPane.showMessageDialog(frameParent, "Some error happened.");
    }
}
