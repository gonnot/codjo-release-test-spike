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
/**
 *
 */
public class TagBuilder {
    private Task parentNode;
    private Node currentSyntaxNode;


    TagBuilder(Task parentNode, Node currentSyntaxNode) {
        this.parentNode = parentNode;
        this.currentSyntaxNode = currentSyntaxNode;
    }


    public TagBuilder readSubTag(String tagName, TaskLocator locator) throws SyntaxErrorException {
        Node syntaxNodeForSubTag = findSyntaxNode(tagName);

        if (syntaxNodeForSubTag == null) {
            throw new SyntaxErrorException(String.format("'%s' is not allowed in '%s'", tagName, parentNode.getName()), locator);
        }

        Task subTag = new Task(tagName, null);
        parentNode.addNode(subTag);
        return new TagBuilder(subTag, syntaxNodeForSubTag);
    }


    private Node findSyntaxNode(final String tagName) {
        FindTagVisitor finder = new FindTagVisitor(tagName);
        currentSyntaxNode.getChildren().visitContent(finder);
        return finder.resultingNode;
    }


    private static class FindTagVisitor implements GrammarVisitor {
        private final String tagName;
        private Node resultingNode = null;


        FindTagVisitor(String tagName) {
            this.tagName = tagName;
        }


        public void visitNode(Node node) {
            if (node.getId().equals(tagName)) {
                resultingNode = node;
            }
        }


        public void visitChildren(NodeChildren children) {
            children.visitContent(this);
        }
    }
}
