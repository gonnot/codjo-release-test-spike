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
import net.codjo.spike.crts.api.definition.GrammarVisitor;
import net.codjo.spike.crts.api.definition.Node;
import net.codjo.spike.crts.api.definition.NodeChildren;
import net.codjo.spike.crts.api.execution.Task;
import net.codjo.spike.crts.api.execution.locator.TaskLocator;
/**
 *
 */
public class TaskBuilder {
    private Task parentTask;
    private Node currentGrammarNode;


    TaskBuilder(Task parentTask, Node currentGrammarNode) {
        this.parentTask = parentTask;
        this.currentGrammarNode = currentGrammarNode;
    }


    public TaskBuilder readSubTask(String taskName, TaskLocator locator) throws SyntaxErrorException {
        Node grammarNodeForSubTask = findGrammarNodeFor(taskName);

        if (grammarNodeForSubTask == null) {
            throw new SyntaxErrorException(String.format("'%s' is not allowed in '%s'", taskName, parentTask.getName()), locator);
        }

        Task subTag = new Task(taskName, null);
        parentTask.addTask(subTag);
        return new TaskBuilder(subTag, grammarNodeForSubTask);
    }


    private Node findGrammarNodeFor(final String taskName) {
        FindTaskVisitor finder = new FindTaskVisitor(taskName);
        currentGrammarNode.getChildren().visitContent(finder);
        return finder.resultingNode;
    }


    private static class FindTaskVisitor implements GrammarVisitor {
        private final String taskName;
        private Node resultingNode = null;


        FindTaskVisitor(String taskName) {
            this.taskName = taskName;
        }


        public void visitNode(Node node) {
            if (node.getId().equals(taskName)) {
                resultingNode = node;
            }
        }


        public void visitChildren(NodeChildren children) {
            children.visitContent(this);
        }
    }
}
