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

package net.codjo.spike.crts.api.parser;
import net.codjo.spike.crts.api.model.Task;
import net.codjo.spike.crts.api.model.locator.TaskLocator;
/**
 *
 */
class EnrichedTaskLocator implements TaskLocator {
    private Task currentTask;
    private TaskLocator locator;
    private Task parentTask;


    EnrichedTaskLocator(Task currentTask, TaskLocator locator, Task parentTask) {
        this.currentTask = currentTask;
        this.locator = locator;
        this.parentTask = parentTask;
    }


    public String toShortDescription() {
        return currentTask.getName() + locator.toShortDescription();
    }


    public String toLongDescription() {
        StringBuilder builder = new StringBuilder();
        builder
              .append(currentTask.getName())
              .append(locator.toLongDescription());
        if (parentTask != null) {
            builder.append("\nat ")
                  .append(parentTask.getLocator().toLongDescription());
        }
        return builder.toString();
    }
}
