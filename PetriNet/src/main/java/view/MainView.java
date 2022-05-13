package view;

import com.mxgraph.view.mxGraph;
import controller.Controller;

import javax.swing.*;
import java.awt.*;

public class MainView {
    private final JFrame frame;
    private final CreateView createView;
    private final AgentsView agentsView;
    private final GraphView graphView;
    private final AnalysisView analysisView;
    private mxGraph graph;

    public MainView(Controller controller) {
        frame = new JFrame();
        frame.setSize(900, 750);
        centreWindow();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        createView = new CreateView(controller);
        agentsView = new AgentsView(controller, frame);
        graph = new mxGraph();
        graphView = new GraphView(graph, controller);
        analysisView = new AnalysisView(controller);

        MenuView menuView = new MenuView(controller, frame);
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(menuView);
        menuBar.add(agentsView);

        frame.setLayout(new BorderLayout());
        frame.add(graphView, BorderLayout.CENTER);
        frame.add(createView, BorderLayout.NORTH);
        frame.add(analysisView, BorderLayout.SOUTH);
        frame.setJMenuBar(menuBar);
        frame.setVisible(true);
    }

    public void centreWindow() {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
    }

    public CreateView getCreateView() {
        return createView;
    }

    public AgentsView getAgentsView() {
        return agentsView;
    }

    public GraphView getGraphView() {
        return graphView;
    }

    public AnalysisView getAnalysisView() {
        return analysisView;
    }

    public mxGraph getGraph() {
        return graph;
    }

    public void setGraph(mxGraph graph) {
        this.graph = graph;
        graph.getModel().setValue(graph.getDefaultParent(), null);
        graphView.setGraph(graph);
        graphView.setVisible(false);
        graphView.setVisible(true);
        agentsView.reset();
        analysisView.reset();
    }

    public static void main(String[] args) {
        Controller controller = new Controller();
    }
}
