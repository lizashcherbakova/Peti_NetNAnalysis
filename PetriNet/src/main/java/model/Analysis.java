package model;

import model.components.Node;
import model.components.Position;
import model.components.Transition;

import java.util.*;

public class Analysis {
    private Graph graph;
    private int numerator = 0;
    private int denominator = 0;

    private Integer[] count(Agent agent) {
        int communicationTransitions = 0;
        for (Transition transition : agent.getTransitions()) {
            for (var next : transition.getNext().entrySet()) {
                if (next.getKey().getHosts().size() > 1) {
                    ++communicationTransitions;
                    break;
                }
            }
            for (var prev : transition.getPrev().entrySet()) {
                if (prev.getKey().getHosts().size() > 1) {
                    ++communicationTransitions;
                    break;
                }
            }
        }
        return new Integer[]{communicationTransitions, agent.getTransitions().size() * 2};
    }


    public double analyze(Graph graph, Set<String> weakPoints) {
        this.graph = graph;
        for (Agent agent : graph.getAgents()) {
            Integer[] fraction = count(agent);
            numerator += fraction[0];
            denominator += fraction[1];
        }
        findWeakPoints(weakPoints);
        return (double) (denominator - numerator) / denominator;
    }

    private void findWeakPoints(Set<String> weakPoints) {
        int max = 2;
        for (var node : graph.getComponents().entrySet()) {
            int newValue;
            if (node.getValue() instanceof Position) {
                newValue = countMaxConnectivity((Position) node.getValue());
            } else {
                newValue = countMaxConnectivity((Transition) node.getValue());
            }
            if (newValue > max) {
                max = newValue;
                weakPoints.clear();
                weakPoints.add(node.getKey());
            } else if (newValue == max) {
                weakPoints.add(node.getKey());
            }
        }
    }

    private int countMaxConnectivity(Position position) {
        if (position.getHosts().size() <= 1) return 0;
        int communication = 0;
        for (Agent agent : position.getHosts()) {
            for (var node : position.getNext().entrySet()) {
                if (!node.getKey().getHosts().contains(agent) || node.getKey().getHosts().size() > 1) {
                    communication += node.getValue();
                }
            }
            for (var node : position.getPrev().entrySet()) {
                if (!node.getKey().getHosts().contains(agent) || node.getKey().getHosts().size() > 1) {
                    communication += node.getValue();
                }
            }
        }
        return communication;
    }

    private int countMaxConnectivity(Transition transition) {
        return transition.getHosts().size();
    }
}
