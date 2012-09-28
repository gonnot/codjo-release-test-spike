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

package net.codjo.spike.crts.kernel;
import java.util.ArrayList;
import java.util.List;
import net.codjo.spike.crts.api.definition.DefinitionVisitor;
import net.codjo.spike.crts.api.definition.INodeChildren;
/**
 *
 */
public class NodeChildren implements GraphElement, INodeChildren {
    private List<GraphElement> nodes = new ArrayList<GraphElement>();
    private Node parentNode;


    public NodeChildren(Node parentNode) {
        this.parentNode = parentNode;
    }


    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visitChildren(parentNode, this);
    }


    public void add(GraphElement node) {
        nodes.add(node);
    }


    public List<GraphElement> getNodes() {
        return nodes;
    }


    public void accept(DefinitionVisitor visitor) {
        visitor.visitChildren(this);
    }


    public boolean isEmpty() {
        return nodes.isEmpty();
    }


    public void visitContent(DefinitionVisitor visitor) {
        for (GraphElement node : nodes) {
            node.accept(visitor);
        }
    }


    public String getOwnerId() {
        return parentNode.getId();
    }
}
