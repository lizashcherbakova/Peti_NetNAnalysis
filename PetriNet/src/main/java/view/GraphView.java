package view;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxICell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource;
import com.mxgraph.view.mxGraph;
import controller.Controller;
import model.components.Transition;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Set;

public class GraphView extends mxGraphComponent {
    private final Controller controller;

    public GraphView(mxGraph graph, Controller controller) {
        super(graph);
        this.controller = controller;
        //  Setting parameters for graph field.
        graph.setEdgeLabelsMovable(false);
        graph.setAllowDanglingEdges(false);
        graph.setSplitEnabled(false);
        getConnectionHandler().setCreateTarget(true);
        //  Handling moment of connecting 2 vertices.
        getConnectionHandler().addListener(null, (o, mxEventObject) -> {
            if (mxEventObject.getName().equals("connect")) {
                mxCell edge = (mxCell) mxEventObject.getProperties().get("cell");
                if(edge.getSource() == null ||  edge.getTarget() == null) {
                    graph.removeCells(new mxICell[]{
                            ((mxCell)graph.getDefaultParent()).
                                    getChildAt(((mxCell)graph.getDefaultParent()).getChildCount()-1)
                    });
                    return;
                }
                var response = controller.requestConnect(edge.getId(),
                        edge.getSource().getId(), edge.getTarget().getId());
                handleConnect(response, edge, edge.getSource(), edge.getTarget());
            }
        });
//  Handling clicking on vertices.
        getGraphControl().addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent me) {
                mxCell cell = (mxCell) getCellAt(me.getX(), me.getY());
                if (me.getButton() == MouseEvent.BUTTON3 && cell != null) {
                    controller.requestMenu(cell, me);
                }
            }
        });
    }

    /**
     * Called after drag&drop.
     * Informs controller about it.
     * Uses method of super class.
     *
     * @param cells
     * @param dx
     * @param dy
     * @param target
     * @param location
     * @return
     */
    @Override
    public Object[] importCells(Object[] cells, double dx, double dy,
                                Object target, Point location) {
        cells = super.importCells(cells, dx, dy, target, location);
        if (cells.length == 1 && location != null) {
            mxCell cell = (mxCell) cells[0];
            controller.add(cell.getId());
        }
        return cells;
    }

    void handleConnect(Object[] response, mxCell edge, mxICell source, mxICell target) {
        if (response[0] == null) {
            graph.removeCells(new mxCell[]{edge});
            return;
        } else if (!response[0].equals(1)) {
            graph.removeCells(new mxCell[]{edge});
            mxCell oldLink = (mxCell) graph.getEdgesBetween(source, target)[0];
            //   oldLink.setValue(response[0]);
            graph.getModel().setValue(oldLink, response[0]);
        }
        if (response[1] != null) {
            String request = null;
            String sourceStyle = null;
            if (response[1] instanceof Transition) {
                request = "shape=rectangle;strokeColor=black;fillColor=" + controller.getColor(target.getId());
                sourceStyle = "shape=ellipse;strokeColor=black;fillColor=" + controller.getColor(source.getId());
            } else {
                request = "shape=ellipse;strokeColor=black;fillColor=" + controller.getColor(target.getId());
                sourceStyle = "shape=rectangle;strokeColor=black;fillColor=" + controller.getColor(source.getId());
            }
            graph.setCellStyle(request, new Object[]{target});
            graph.setCellStyle(sourceStyle, new Object[]{source});
        }
    }

    public void removeCell(mxCell cell) {
        // System.out.println("Neeed to first update colors of neigbors and then delete the cell");
        graph.getModel().beginUpdate();
        graph.removeCells(new mxCell[]{cell});
        graph.getModel().endUpdate();
    }

    public void updateColor(mxCell cell, String color) {
        // String style = cell.getStyle();
        StringBuilder style = new StringBuilder();
        style.append("shape=");
        style.append(graph.getCellStyle(cell).get("shape"));
        style.append(";strokeColor=black;fillColor=");
        style.append(color);
        graph.setCellStyle(style.toString(), new Object[]{cell});
    }

    public void updateAllColors() {
        graph.getModel().beginUpdate();
        mxCell parent = (mxCell) graph.getDefaultParent();
        for (int i = 0; i < parent.getChildCount(); ++i) {
            if (parent.getChildAt(i).isVertex()) {
                updateColor((mxCell) parent.getChildAt(i), controller.getColor(parent.getChildAt(i).getId()));
            }
        }
        graph.getModel().endUpdate();
    }

    public void highlightMistake(Set<String> badTran) {
        graph.getModel().beginUpdate();
        mxCell parent = (mxCell) graph.getDefaultParent();
        for (int i = 0; i < parent.getChildCount(); ++i) {
            if (badTran.contains(parent.getChildAt(i).getId())) {
                mxCell cell = (mxCell) parent.getChildAt(i);
                String style = cell.getStyle();
                graph.setCellStyle(style.replace((String) graph.getCellStyle(cell).get("strokeColor"), "red"), new Object[]{cell});
            }
        }
        graph.getModel().endUpdate();
    }

    public void highlightWeakComp(Set<String> weakComponents) {
        graph.getModel().beginUpdate();
        mxCell parent = (mxCell) graph.getDefaultParent();
        for (int i = 0; i < parent.getChildCount(); ++i) {
            mxCell cell = (mxCell) parent.getChildAt(i);
            if (weakComponents.contains(cell.getId())) {
                String style = cell.getStyle();
                graph.setCellStyle(style.replace((String) graph.getCellStyle(cell).get("strokeColor"), "red") + ";dashed=true;strokeWidth=2", new Object[]{cell});
            }
        }
        graph.getModel().endUpdate();
    }

    public void errorAloneTransition() {
        JOptionPane.showMessageDialog(this, "Transition must be with a place.");
    }
}
