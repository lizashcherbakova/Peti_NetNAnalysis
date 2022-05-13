package model;

import model.components.Node;
import model.components.Position;
import model.components.Transition;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class Graph implements Serializable {
    @Serial
    private static final long serialVersionUID = -3340265091768356055L;

    private final Map<String, Node> components = new HashMap<>();
    private final List<Agent> agents = new ArrayList<>();

    public Object[] addEdge(String sourceId, String targetId) {
        Object[] response = new Object[2];
        if (!components.containsKey(targetId)) {
            response[0] = 1;
            response[1] = components.get(sourceId).nextLink(targetId);
            components.put(targetId, (Node) response[1]);
            return response;
        }
        int linksNumber = components.get(sourceId).NumberOfLinks(components.get(targetId));
        if (linksNumber < 0) {
            response[0] = null;
        } else {
            components.get(sourceId).link(components.get(targetId));
            response[0] = linksNumber + 1;
        }
        response[1] = components.get(targetId);
        return response;
    }

    public boolean removeEdge(String sourceId, String targetId) {
        return components.get(sourceId).unlink(components.get(targetId));
    }

    public void addTransition(String id, List<Agent> agents) {
        components.put(id, new Transition(id, agents));
    }

    public void addPosition(String id, List<Agent> agents) {
        components.put(id, new Position(id, agents));
    }

    public Agent addAgent(String name) {
        Agent agent = new Agent(name);
        agents.add(agent);
        return agent;
    }

    public void addAgent(String nodeId, Agent agent) {
        components.get(nodeId).addAgent(agent);
    }

    public boolean removeAgent(String nodeId, Agent agent) {
        return components.get(nodeId).removeAgent(agent);
    }

    public boolean removeNode(String id) {
        if (components.get(id).clearLinks()) {
            components.get(id).unlinkFromAgents();
            components.remove(id);
            return true;
        }
        return false;
    }

    public Set<String> getAgents(String nodeId) {
        if (!components.containsKey(nodeId)) {
            return new HashSet<>();
        }
        return components.get(nodeId).getHosts().stream().map(x -> x.name).collect(Collectors.toSet());
    }

    public Set<String> getInvalidTransitions() {
        Set<String> rez = new HashSet<>();
        for (Map.Entry<String, Node> node : components.entrySet()) {
            if (node.getValue() instanceof Transition) {
                if (!((Transition) node.getValue()).isValid()) {
                    rez.add(node.getKey());
                }
            }
        }
        return rez;
    }

    public List<Agent> getAgents() {
        return agents;
    }

    public Map<String, Node> getComponents() {
        return components;
    }
}
