/*
 * codjo (Prototype)
 * =================
 *
 *    Copyright (C) 2012, 2012 by codjo.net
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
import net.codjo.spike.crts.model.definition.NodeDefinition;
import net.codjo.spike.crts.model.execution.EmptyBehaviour;

public class Node {
    private List<Node> nodes = new ArrayList<Node>();
    private NodeDefinition nodeDefinition;


    public Node(String name) {
        this(new NodeDefinition(name, EmptyBehaviour.class));
    }


    public Node(NodeDefinition nodeDefinition) {
        this.nodeDefinition = nodeDefinition;
    }


    public String getId() {
        return nodeDefinition.getId();
    }


    public NodeDefinition getNodeDefinition() {
        return nodeDefinition;
    }


    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visit(this);
    }


    public void add(Node node) {
        nodes.add(node);
    }


    public List<Node> getNodes() {
        return nodes;
    }
}
