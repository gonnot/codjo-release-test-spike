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

package net.codjo.spike.crts.kernel.model;
import net.codjo.spike.crts.api.model.Script;
import net.codjo.spike.crts.api.model.Task;
import net.codjo.spike.crts.api.model.TaskVisitor;
/**
 *
 */
public class TaskFinder {

    private TaskFinder() {
    }


    public static Task find(String taskName, Script script) throws Exception {
        FindTaskVisitor taskVisitor = new FindTaskVisitor(taskName);
        script.visitFromRoot(taskVisitor);
        return taskVisitor.foundTask;
    }


    private static class FindTaskVisitor implements TaskVisitor {
        private final String taskName;
        Task foundTask = null;


        FindTaskVisitor(String taskName) {
            this.taskName = taskName;
        }


        public void visit(Task task) throws Exception {
            if (task.getName().equals(taskName)) {
                foundTask = task;
            }
            else {
                task.visitChildren(this);
            }
        }
    }
}
