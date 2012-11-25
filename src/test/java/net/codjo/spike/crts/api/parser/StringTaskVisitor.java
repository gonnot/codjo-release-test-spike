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

import net.codjo.spike.crts.api.execution.Task;
import net.codjo.spike.crts.api.execution.TaskVisitor;
public class StringTaskVisitor implements TaskVisitor {
    private static final String BRANCH = " *-- ";
    private static final String INDENT = "     ";
    private int level = 0;
    private StringBuilder result = new StringBuilder();


    public void visit(Task task) throws Exception {
        indent()
              .append(task.getName())
              .append('\n');

        level++;
        task.visitChildren(this);
        level--;
    }


    private StringBuilder indent() {
        if (level > 0) {
            for (int i = 1; i < level; i++) {
                result.append(INDENT);
            }
            result.append(BRANCH);
        }
        return result;
    }


    public String getResultingTree() {
        return result.toString();
    }
}
