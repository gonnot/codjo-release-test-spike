package net.codjo.spike.crts;
/**
 *
 */
public class Node {
    private State state = State.UNKNOWN;


    public Node() {
    }


    public Node(State state) {
        this.state = state;
    }


    public State getState() {
        return state;
    }


    public void setState(State state) {
        this.state = state;
    }
}
