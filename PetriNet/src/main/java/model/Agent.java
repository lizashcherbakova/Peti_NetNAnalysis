package model;

import model.components.Position;
import model.components.Transition;

import java.awt.Color;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

///  There can be restrictions for number of agents in system.
/// So we can prepare array of colors beforehand.
public class Agent implements Serializable {
    @Serial
    private static final long serialVersionUID = 2457709650055741436L;

    protected String name;
    private final Color color;
    private final Set<Position> positions = new HashSet<>();
    private final Set<Transition> transitions = new HashSet<>();
    public Agent(String name) {
        this.name = name;
        Random rand = new Random();
        color = new Color(rand.nextInt(0xFFFFFF));
    }

    public String getName() {
        return name;
    }

    public Set<Position> getPositions() {
        return positions;
    }

    public Set<Transition> getTransitions() {
        return transitions;
    }

    public String getColorHex(){
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }
    public void addPosition(Position position){
        positions.add(position);
    }
    public void addTransition(Transition transition){
        transitions.add(transition);
    }
    public void delPosition(Position position){
        positions.remove(position);
    }
    public void delTransition(Transition transition){
        transitions.remove(transition);
    }
}
