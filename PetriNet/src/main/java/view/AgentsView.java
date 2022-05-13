package view;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class AgentsView extends JMenu {
    private final Controller controller;
    private final JFrame frameParent;

    public AgentsView(Controller controller, JFrame frame) {
        super("Show agents...");
        frameParent = frame;
        this.controller = controller;

        addCreateAgent();
       // add(menu);
    }

    void addCreateAgent() {
        JMenuItem menuItem = new JMenuItem("Enter new agent name");
        menuItem.addActionListener(e -> {
            String newName = (String) JOptionPane.showInputDialog(
                    frameParent,
                    "Input new agent's name:",
                    "Creating new agent.",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    "");
            if(newName != null) {
                if(!controller.addAgent(newName)){
                    JOptionPane.showMessageDialog(frameParent, "Invalid agent name.");
                }
            }
        });
        add(menuItem);
        addSeparator();
    }

    public void addAgent(String name, String color) {
        BufferedImage icon = new BufferedImage(10,10,BufferedImage.TYPE_INT_RGB);
        Graphics2D    graphics = icon.createGraphics();
        graphics.setPaint ( Color.decode(color));
        graphics.fillRect ( 0, 0, icon.getWidth(), icon.getHeight() );

        JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem(name, new ImageIcon(icon));
        add(menuItem);
    }

    public List<String> getSelected(){
        List<String> selected = new ArrayList<>();
        for(int i = 2; i < getItemCount(); ++i){
            if(getItem(i).isSelected()){
                selected.add(getItem(i).getText());
            }
        }
        return selected;
    }

    public void reset() {
        removeAll();
        addCreateAgent();
    }
}
