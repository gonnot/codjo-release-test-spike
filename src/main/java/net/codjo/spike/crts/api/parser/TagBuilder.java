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
import net.codjo.spike.crts.api.definition.DefinitionVisitor;
import net.codjo.spike.crts.api.definition.INode;
import net.codjo.spike.crts.api.definition.INodeChildren;
import net.codjo.spike.crts.api.execution.ExecutionNode;
/**
 *
 */
public class TagBuilder {
    private ExecutionNode parentNode;
    private INode currentSyntaxNode;


    TagBuilder(ExecutionNode parentNode, INode currentSyntaxNode) {
        this.parentNode = parentNode;
        this.currentSyntaxNode = currentSyntaxNode;
    }


    public TagBuilder readSubTag(String tagName, TagLocator locator) throws SyntaxErrorException {
        INode syntaxNodeForSubTag = findSyntaxNode(tagName);

        if (syntaxNodeForSubTag == null) {
            throw new SyntaxErrorException(String.format("'%s' is not allowed in '%s'", tagName, parentNode.getName()), locator);
        }

        ExecutionNode subTag = new ExecutionNode(tagName, null);
        parentNode.addNode(subTag);
        return new TagBuilder(subTag, syntaxNodeForSubTag);
    }


    private INode findSyntaxNode(final String tagName) {
        FindTagVisitor finder = new FindTagVisitor(tagName);
        currentSyntaxNode.getChildren().visitContent(finder);
        return finder.resultingNode;
    }


    private static class FindTagVisitor implements DefinitionVisitor {
        private final String tagName;
        private INode resultingNode = null;


        FindTagVisitor(String tagName) {
            this.tagName = tagName;
        }


        public void visitNode(INode node) {
            if (node.getId().equals(tagName)) {
                resultingNode = node;
            }
        }


        public void visitChildren(INodeChildren children) {
            children.visitContent(this);
        }
    }
}
