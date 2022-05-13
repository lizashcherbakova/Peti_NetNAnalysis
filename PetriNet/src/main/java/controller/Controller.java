package controller;

import com.mxgraph.model.mxCell;
import model.Agent;
import model.Analysis;
import model.FileGraph;
import model.Graph;
import view.MainView;
import view.PopupMenuView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

public class Controller {
    private MainView mainView;
    private boolean transition;
    private LinkedHashMap<String, Agent> agents = new LinkedHashMap<>();
    private Graph graph = new Graph();

    public Controller() {
        mainView = new MainView(this);
    }

    public void startDrag(boolean transition) {

        this.transition = transition;
        List<String> rez = mainView.getAgentsView().getSelected();
        mainView.getCreateView().setColor(getColor(rez));
    }

    private String getColor(Collection<String> namesAgents) {
        if (namesAgents.isEmpty()) {
            return "white";
        } else if (namesAgents.size() == 1) {
            return agents.get(namesAgents.iterator().next()).getColorHex();
        } else {
            return "blue";
        }
    }

    public String getColor(String id) {
        return getColor(graph.getAgents(id));
    }

    public Object[] requestConnect(String edge, String source, String target) {
        return graph.addEdge(source, target);
    }

    public void add(String id) {
        var agents = mainView.getAgentsView().getSelected().stream().map(x -> this.agents.get(x));
        if (transition) {
            graph.addTransition(id, agents.toList());
        } else {
            graph.addPosition(id, agents.toList());
        }
    }

    public void requestDelete(mxCell cell) {
        if ((cell.isVertex() && graph.removeNode(cell.getId()))
                || (cell.isEdge() && graph.removeEdge(cell.getSource().getId(), cell.getTarget().getId()))) {
            mainView.getGraphView().removeCell(cell);
        } else {
            mainView.getGraphView().errorAloneTransition();
        }
    }

    public boolean addAgent(String newName) {
        if (this.agents.containsKey(newName)) {
            return false;
        }
        Agent agent = graph.addAgent(newName);
        agents.put(newName, agent);
        mainView.getAgentsView().addAgent(newName, agent.getColorHex());
        return true;
    }

    public void requestMenu(mxCell cell, MouseEvent me) {
        Point pt = SwingUtilities.convertPoint(me.getComponent(), me.getPoint(),
                mainView.getGraphView());
        PopupMenuView menu;
        if (cell.isVertex()) {
            menu = new PopupMenuView(this, cell, new ArrayList<>(agents.keySet()), graph.getAgents(cell.getId()));
        } else {
            menu = new PopupMenuView(this, cell, null, graph.getAgents(cell.getId()));
        }
        menu.show(mainView.getGraphView(), pt.x, pt.y);
        me.consume();
    }

    public void addAgent(mxCell cell, String agentName, boolean checked) {
        if (checked) {
            graph.addAgent(cell.getId(), agents.get(agentName));
        } else {
            graph.removeAgent(cell.getId(), agents.get(agentName));
        }
        mainView.getGraphView().updateAllColors();
    }

    public void requestAnalyze() {
        mainView.getGraphView().updateAllColors();
        var invalidTran = graph.getInvalidTransitions();
        if (!invalidTran.isEmpty()) {
            mainView.getGraphView().highlightMistake(invalidTran);
            mainView.getGraphView().errorAloneTransition();
        }else {
            Analysis analysis = new Analysis();
            Set<String> weakComponents = new HashSet<>();
            mainView.getAnalysisView().setAnswer(analysis.analyze(graph, weakComponents));
            mainView.getGraphView().highlightWeakComp(weakComponents);
        }
    }

    public boolean requestSave(String path){
        return FileGraph.save(graph, mainView.getGraph(), path);
    }

    public boolean requestLoad(String path){
        var rez = FileGraph.load(path);
        if(rez == null)return false;
        mainView.setGraph(rez.getValue());
        this.graph = rez.getKey();
        resetAgents();
        return true;
    }

    public void resetAgents(){
        this.agents.clear();
        for(Agent agent : graph.getAgents()){
            mainView.getAgentsView().addAgent(agent.getName(), agent.getColorHex());
            this.agents.put(agent.getName(), agent);
        }
    }
}
