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

package net.codjo.spike.crts.model.definition;
import java.util.ArrayList;
import java.util.List;
import net.codjo.spike.crts.model.execution.EmptyBehaviour;
/**
 *
 */
public class DefinitionBuilder {
    private NodeDefinition nodeDefinition;
    private final List<Definition> definitions = new ArrayList<Definition>();


    public static DefinitionBuilder node(final String nodeId) {
        return new DefinitionBuilder(new NodeDefinition(nodeId, EmptyBehaviour.class));
    }


    private DefinitionBuilder(NodeDefinition nodeDefinition) {
        this.nodeDefinition = nodeDefinition;
        this.definitions.add(nodeDefinition);
    }


    public DefinitionBuilder add(DefinitionBuilder subDefinitionBuilder) {
        definitions.add(link().fromParent(nodeDefinition).to(subDefinitionBuilder.nodeDefinition));
        definitions.addAll(subDefinitionBuilder.get());
        return this;
    }


    public List<Definition> get() {
        return definitions;
    }


    public DefinitionBuilder asChildOf(String parentId) {
        definitions.add(link().fromParent(parentId).to(nodeDefinition));
        return this;
    }


    private static LinkBuilder link() {
        return new LinkBuilder();
    }


    private static class LinkBuilder {
        private String parentId;


        LinkBuilder fromParent(NodeDefinition node) {
            this.parentId = node.getId();
            return this;
        }


        LinkBuilder fromParent(String nodeId) {
            this.parentId = nodeId;
            return this;
        }


        LinkDefinition to(NodeDefinition node) {
            return new LinkDefinition(parentId, node.getId());
        }
    }
}
