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

package net.codjo.spike.crts.api.definition;
import java.util.ArrayList;
import java.util.List;
import net.codjo.spike.crts.api.execution.behaviour.EmptyBehaviour;
import net.codjo.spike.crts.kernel.definition.model.Definition;
import net.codjo.spike.crts.kernel.definition.model.LinkDefinition;
import net.codjo.spike.crts.kernel.definition.model.LinkDefinition.Type;
import net.codjo.spike.crts.kernel.definition.model.LinkToChildrenDefinition;
import net.codjo.spike.crts.kernel.definition.model.NodeDefinition;
import static net.codjo.spike.crts.kernel.definition.model.LinkDefinition.Type.BY_ID;
import static net.codjo.spike.crts.kernel.definition.model.LinkDefinition.Type.REGEXP;
/**
 *
 */
public final class GrammarBuilder {
    private NodeDefinition nodeDefinition;
    private final List<Definition> definitions = new ArrayList<Definition>();


    public static GrammarBuilder node(final String nodeId) {
        return new GrammarBuilder(new NodeDefinition(nodeId, EmptyBehaviour.class));
    }


    private GrammarBuilder(NodeDefinition nodeDefinition) {
        this.nodeDefinition = nodeDefinition;
        this.definitions.add(nodeDefinition);
    }


    public List<Definition> get() {
        return definitions;
    }


    public GrammarBuilder asChildOf(String parentId) {
        definitions.add(link(BY_ID).fromParent(parentId).to(nodeDefinition));
        return this;
    }


    public GrammarBuilder asChildOfMatchingNodes(String javaRegExp) {
        definitions.add(link(REGEXP).fromParent(javaRegExp).to(nodeDefinition));
        return this;
    }


    public GrammarBuilder containing(GrammarBuilder subGrammarBuilder) {
        definitions.add(link(BY_ID).fromParent(nodeDefinition).to(subGrammarBuilder.nodeDefinition));
        definitions.addAll(subGrammarBuilder.get());
        return this;
    }


    public GrammarBuilder containingChildrenOf(String parentNodeId) {
        definitions.add(link().fromParent(nodeDefinition).toChildrenOf(parentNodeId));
        return this;
    }


    private static LinkBuilder link(Type direct) {
        return new LinkBuilder(direct);
    }


    private static LinkToChildrenBuilder link() {
        return new LinkToChildrenBuilder();
    }


    private static class LinkBuilder {
        private final Type type;
        private String parentIdOrRegExp;


        private LinkBuilder(Type type) {
            this.type = type;
        }


        LinkBuilder fromParent(NodeDefinition node) {
            this.parentIdOrRegExp = node.getId();
            return this;
        }


        LinkBuilder fromParent(String nodeId) {
            this.parentIdOrRegExp = nodeId;
            return this;
        }


        LinkDefinition to(NodeDefinition node) {
            return new LinkDefinition(type, parentIdOrRegExp, node.getId());
        }
    }
    private static class LinkToChildrenBuilder {
        private String fromId;


        LinkToChildrenBuilder fromParent(NodeDefinition node) {
            this.fromId = node.getId();
            return this;
        }


        LinkToChildrenDefinition toChildrenOf(String toId) {
            return new LinkToChildrenDefinition(fromId, toId);
        }
    }
}
