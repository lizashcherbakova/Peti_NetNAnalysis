package model.components;

import model.Agent;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Position extends Node implements Serializable {

    @Serial
    private static final long serialVersionUID = -1305104640987968064L;

    public Position(String id, List<Agent> agents) {
        super(id, agents);
        for(var agent : agents){
            agent.addPosition(this);
        }
    }

    public void unlinkFromAgents(){
        for(var agent : hosts){
            agent.delPosition(this);
        }
    }

    @Override
    public Node nextLink(String targetId) {
        Transition nextNode = new Transition(targetId, hosts.stream().toList());
        next.put(nextNode, 1);
        nextNode.prev.put(this,1);
        return nextNode;
    }

    @Override
    public void link(Node node) {
        this.addAgentsToList(node.hosts);
       // this.hosts.addAll(node.hosts);
        if(next.containsKey(node)){
            next.put(node, next.get(node) + 1);
            node.prev.put(this, next.get(node));
        }else{
            next.put(node, 1);
            node.prev.put(this, 1);
        }
    }

    @Override
    public boolean unlink(Node node) {
        if(node.prev.size() == 1) {
            return false;
        }
        next.remove(node);
        node.prev.remove(this);
        return true;
    }

    @Override
    public boolean clearLinks() {
        for (Node tran : next.keySet()) {
            if(tran.prev.size()==1){
                return false;
            }
        }
        for (Node tran : prev.keySet()) {
            if(tran.next.size()==1){
                return false;
            }
        }
        for (Node tran : next.keySet()) {
            tran.prev.remove(this);
        }
        for (Node tran : prev.keySet()) {
            tran.next.remove(this);
        }
        return true;
    }

    @Override
    public int NumberOfLinks(Node target) {
        if(target instanceof Position){
            return -1;
        }
        return super.NumberOfLinks(target);
    }

    @Override
    public boolean removeAgent(Agent agent) {
        for (Node tran : next.keySet()) {
            if(tran.hosts.contains(agent)){
                return false;
            }
        }
        for (Node tran : prev.keySet()) {
            if(tran.hosts.contains(agent)){
                return false;
            }
        }
        agent.delPosition(this);
        return super.removeAgent(agent);
    }

    @Override
    public void addAgent(Agent agent) {
        super.addAgent(agent);
        agent.addPosition(this);
    }

    public void addAgentsToList(HashSet<Agent> hosts ){
        for(var agent : hosts){
            agent.addPosition(this);
        }
        this.hosts.addAll(hosts);
    }
}
