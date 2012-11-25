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

package net.codjo.spike.crts.kernel.definition;

import java.util.List;
import net.codjo.spike.crts.api.definition.GrammarVisitor;
import net.codjo.spike.crts.api.definition.Node;
import net.codjo.spike.crts.api.execution.behaviour.EmptyBehaviour;
import net.codjo.spike.crts.kernel.definition.model.NodeDefinition;

public class NodeImpl implements GraphElement, Node {
    private final NodeChildrenImpl children;
    private final NodeDefinition nodeDefinition;


    public NodeImpl(String name) {
        this(new NodeDefinition(name, EmptyBehaviour.class));
    }


    public NodeImpl(NodeDefinition nodeDefinition) {
        this.nodeDefinition = nodeDefinition;
        children = new NodeChildrenImpl(this);
    }


    public String getId() {
        return nodeDefinition.getId();
    }


    // used by drools
    @SuppressWarnings({"UnusedDeclaration"})
    public NodeDefinition getNodeDefinition() {
        return nodeDefinition;
    }


    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visit(this);
    }


    public void add(GraphElement node) {
        children.add(node);
    }


    public List<GraphElement> getNodes() {
        return children.getNodes();
    }


    // used by drools
    @SuppressWarnings({"UnusedDeclaration"})
    public NodeChildrenImpl getChildren() {
        return children;
    }


    public void accept(GrammarVisitor visitor) {
        visitor.visitNode(this);
    }
}
