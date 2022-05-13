package view;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.util.mxGraphTransferable;
import com.mxgraph.swing.util.mxSwingConstants;
import com.mxgraph.util.mxRectangle;
import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

public class CreateView extends JPanel {
    private Controller controller;
    private final int width = 30;
    private final int height = 30;
    private String nextColor = "white";

    public CreateView(Controller controller) {
        this.controller = controller;
        setLayout(new FlowLayout());
        add(createLabel("Transition", "rectangle", true));
        add(createLabel("Place", "ellipse", false));
    }

    public void setColor(String color) {
        nextColor = color;
    }

    private ImageIcon createIcon(boolean transition) {
        BufferedImage icon = new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = icon.createGraphics();
        if (transition) {
            graphics.setPaint(Color.white);
            graphics.fillRect(0, 0, icon.getWidth(), icon.getHeight());
            graphics.setPaint(Color.black);
            graphics.drawRect(0, 0, icon.getWidth() - 2, icon.getHeight() - 2);
        } else {
            graphics.setPaint(this.getBackground());
            graphics.fillRect(0, 0, icon.getWidth(), icon.getHeight());
            var ellipse = new Ellipse2D.Double(0, 0, icon.getWidth() - 1, icon.getHeight() - 1);
            graphics.setPaint(Color.white);
            graphics.fill(ellipse);
            graphics.setPaint(Color.black);
            graphics.draw(ellipse);
        }
        return new ImageIcon(icon);
    }

    private JLabel createLabel(String text, String shape, boolean transition) {
        final JLabel label = new JLabel();
        label.setIcon(createIcon(transition));
        label.setPreferredSize(new Dimension(80, 50));

        label.setVerticalTextPosition(JLabel.BOTTOM);
        label.setHorizontalTextPosition(JLabel.CENTER);
        label.setIconTextGap(0);
        label.setText(text);

        DragGestureListener dragGestureListener = e -> {
            controller.startDrag(transition);
            mxCell cell = new mxCell(null, new mxGeometry(0, 0, width, height),
                    "shape=" + shape + ";strokeColor=black;fillColor=" + nextColor);
            cell.setVertex(true);
            mxRectangle bounds = (mxGeometry) cell.getGeometry().clone();

            final mxGraphTransferable transferable = new mxGraphTransferable(
                    new Object[]{cell}, bounds);

            e.startDrag(null, mxSwingConstants.EMPTY_IMAGE, new Point(),
                    transferable, null);
        };

        DragSource dragSource = new DragSource();
        dragSource.createDefaultDragGestureRecognizer(label,
                DnDConstants.ACTION_COPY, dragGestureListener);
        return label;
    }
}
