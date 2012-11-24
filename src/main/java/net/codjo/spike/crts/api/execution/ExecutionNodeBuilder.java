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
import net.codjo.spike.crts.api.execution.behaviour.NodeBehaviour;
/**
 *
 */
public final class ExecutionNodeBuilder {
    private final ExecutionNode node;


    private ExecutionNodeBuilder(String name, NodeBehaviour behaviour) {
        this.node = new ExecutionNode(name, behaviour);
    }


    public static ExecutionNodeBuilder tagWith(String name, NodeBehaviour behaviour) {
        return new ExecutionNodeBuilder(name, behaviour);
    }


    public ExecutionNodeBuilder containing(ExecutionNodeBuilder subNodeBuilder) {
        node.addNode(subNodeBuilder.get());
        return this;
    }


    public ExecutionNode get() {
        return node;
    }
}
