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
/**
 *
 */
public class NodeChildren implements Visitable {
    private List<Visitable> nodes = new ArrayList<Visitable>();
    private Node parentNode;


    public NodeChildren(Node parentNode) {
        this.parentNode = parentNode;
    }


    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visitChildren(parentNode, this);
    }


    public void add(Visitable node) {
        nodes.add(node);
    }


    public List<Visitable> getNodes() {
        return nodes;
    }
}
