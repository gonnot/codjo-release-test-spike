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
import net.codjo.spike.crts.api.execution.behaviour.TaskBehaviour;
/**
 *
 */
public final class SimpleTaskBuilder {
    private final Task task;


    private SimpleTaskBuilder(String name, TaskBehaviour behaviour) {
        this.task = new Task(name, behaviour);
    }


    public static SimpleTaskBuilder task(String name, TaskBehaviour behaviour) {
        return new SimpleTaskBuilder(name, behaviour);
    }


    public SimpleTaskBuilder containing(SimpleTaskBuilder subTaskBuilder) {
        task.addTask(subTaskBuilder.get());
        return this;
    }


    public Task get() {
        return task;
    }
}
