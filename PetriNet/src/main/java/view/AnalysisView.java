package view;

import controller.Controller;

import javax.swing.*;
import java.awt.*;

public class AnalysisView extends JPanel {
    private final JLabel answer = new JLabel();
    public AnalysisView(Controller controller){

        setLayout(new FlowLayout());

        JButton analyze = new JButton("Analyze");
        analyze.addActionListener(e->{
            controller.requestAnalyze();
        });
        add(analyze);
        add(Box.createRigidArea(new Dimension(30, 0)));
        add(answer);
    }

    public void setAnswer(double ansNumber){
        answer.setText("Net structure indicator : " + ansNumber);
    }

    public void reset() {
        answer.setText("");
    }
}
