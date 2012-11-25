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
import net.codjo.spike.crts.api.execution.behaviour.TaskBehaviour;
/**
 *
 */
public class Task {
    private final TaskBehaviour behaviour;
    private final List<Task> subNodes = new ArrayList<Task>();
    private final String name;


    public Task(String name, TaskBehaviour behaviour) {
        this.name = name;
        this.behaviour = behaviour;
    }


    public TaskBehaviour getBehaviour() {
        return behaviour;
    }


    public void addNode(Task node) {
        subNodes.add(node);
    }


    public void accept(TaskVisitor visitor) throws Exception {
        visitor.visit(this);
    }


    public void visitChildren(TaskVisitor visitor) throws Exception {
        for (Task subNode : subNodes) {
            visitor.visit(subNode);
        }
    }


    public String getName() {
        return name;
    }
}
