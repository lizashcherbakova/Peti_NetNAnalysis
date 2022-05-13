package model.components;

import model.Agent;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class Transition extends Node implements Serializable {

    @Serial
    private static final long serialVersionUID = -2373785094668751241L;

    public Transition(String id, List<Agent> agents) {
        super(id, agents);
        for(var agent : agents){
            agent.addTransition(this);
        }
    }

    public boolean isValid(){
        return prev.size() > 0 && next.size() > 0;
    }

    public void unlinkFromAgents(){
        for(var agent : hosts){
            agent.delTransition(this);
        }
    }

    @Override
    public Node nextLink(String targetId) {
        Position nextNode = new Position(targetId, hosts.stream().toList());
        next.put(nextNode, 1);
        nextNode.prev.put(this, 1);
        return nextNode;
    }

    @Override
    public void link(Node node) {
       // node.hosts.addAll(hosts);
        ((Position)node).addAgentsToList(hosts);
        if (next.containsKey(node)) {
            next.replace(node, next.get(node) + 1);
            node.prev.replace(this, next.get(node));
        } else {
            next.put(node, 1);
            node.prev.put(this, 1);
        }
    }

    @Override
    public boolean unlink(Node node) {
        if(next.size() == 1) {
            return false;
        }
        next.remove(node);
        node.prev.remove(this);
        return true;
    }

    @Override
    public boolean clearLinks() {
        for (Node position : next.keySet()) {
            position.prev.remove(this);
        }
        for (Node position : prev.keySet()) {
            position.next.remove(this);
        }
        return true;
    }

    @Override
    public int NumberOfLinks(Node target) {
        if (target instanceof Transition) {
            return -1;
        }
        return super.NumberOfLinks(target);
    }

    @Override
    public void addAgent(Agent agent) {
        super.addAgent(agent);
        for (Node position : next.keySet()) {
            position.addAgent(agent);
        }
        for (Node position : prev.keySet()) {
            position.addAgent(agent);
        }
        agent.addTransition(this);
    }

    @Override
    public boolean removeAgent(Agent agent) {
        if(super.removeAgent(agent)){
            agent.delTransition(this);
            return true;
        }
        return false;
    }
}
