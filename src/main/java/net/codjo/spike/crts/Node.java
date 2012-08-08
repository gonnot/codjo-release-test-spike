/*
 * codjo (Prototype)
 * =================
 *
 *    Copyright (C) 2012, $YEAR$ by codjo.net
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *    implied. See the License for the specific language governing permissions
 *    and limitations under the License.
 */

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
