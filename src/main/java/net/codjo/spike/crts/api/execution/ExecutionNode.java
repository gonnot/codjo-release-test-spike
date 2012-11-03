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

package net.codjo.spike.crts.api.execution;
import java.util.ArrayList;
import java.util.List;
import net.codjo.spike.crts.api.execution.behaviour.NodeBehaviour;
/**
 *
 */
public class ExecutionNode {
    private final NodeBehaviour behaviour;
    private final List<ExecutionNode> subNodes = new ArrayList<ExecutionNode>();


    public ExecutionNode(NodeBehaviour behaviour) {
        this.behaviour = behaviour;
    }


    public NodeBehaviour getBehaviour() {
        return behaviour;
    }


    public void addNode(ExecutionNode node) {
        subNodes.add(node);
    }


    public void accept(ExecutionNodeVisitor visitor) throws Exception {
        visitor.visit(this);
    }


    public void visitChildren(ExecutionNodeVisitor visitor) throws Exception {
        for (ExecutionNode subNode : subNodes) {
            visitor.visit(subNode);
        }
    }
}
