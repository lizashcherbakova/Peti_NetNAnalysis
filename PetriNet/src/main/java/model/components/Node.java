package model.components;

import model.Agent;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

abstract public class Node implements Serializable {
    @Serial
    private static final long serialVersionUID = -8587132399108681731L;

    protected String id;
    protected Map<Node, Integer> next = new HashMap<>();
    protected Map<Node, Integer> prev = new HashMap<>();
    protected HashSet<Agent> hosts = new HashSet<>();

    public Node(String id, List<Agent> agents){
        hosts.addAll(agents);
        this.id = id;
    }

    public Map<Node, Integer> getNext() {
        return next;
    }

    public Map<Node, Integer> getPrev() {
        return prev;
    }

    public int NumberOfLinks(Node target) {
        if(!next.containsKey(target)){
            return 0;
        }
        return next.get(target);
    }

    public void addAgent(Agent agent){
        hosts.add(agent);
    }

    public boolean removeAgent(Agent agent){
hosts.remove(agent);
return true;
    }

    public HashSet<Agent> getHosts(){
        return hosts;
    }

    public abstract Node nextLink(String targetId);

    public abstract void link(Node node);

    public abstract boolean unlink(Node node);

    public abstract boolean clearLinks();
    public abstract void unlinkFromAgents();
}
