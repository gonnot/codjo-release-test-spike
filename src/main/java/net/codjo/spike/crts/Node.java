package net.codjo.spike.crts;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private State state = State.UNKNOWN;
    private final String name;
    private List<Node> nodes = new ArrayList<Node>();


    public Node(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }


    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visit(this);
    }


    void add(Node node) {
        nodes.add(node);
    }


    List<Node> getNodes() {
        return nodes;
    }

    public State getState() {
        return state;
    }


    public void setState(State state) {
        this.state = state;
    }
}
