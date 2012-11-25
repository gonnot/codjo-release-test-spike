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

package net.codjo.spike.crts.kernel.definition;

import org.junit.Test;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

// @RunWith(Enclosed.class)
public class NodeImplTest {
    @Test
    public void testVisitOneNode() throws Exception {
        NodeImpl node = new NodeImpl("root");
        assertNode(node, "root");
    }


    @Test
    public void testVisitWithOneSubNode() throws Exception {
        NodeImpl node =
              node("root")
                    .add(node("gui-test"))
                    .get();
        assertNode(node, "root" +
                         " *-- gui-test");
    }


    @Test
    public void testVisitWithSubNode() throws Exception {
        NodeImpl node =
              node("root")
                    .add(node("gui-test"))
                    .add(node("web-test"))
                    .get();
        assertNode(node, "root" +
                         " *-- gui-test" +
                         " *-- web-test");
    }


    @Test
    public void testVisitWithDeepSubNode() throws Exception {
        NodeImpl node =
              node("root")
                    .add(node("gui-test")
                               .add(node("assertButton")
                                          .add(node("documentation")))
                               .add(node("click")))
                    .add(node("web-test"))
                    .get();

        assertNode(node, "root" +
                         " *-- gui-test" +
                         "      *-- assertButton" +
                         "           *-- documentation" +
                         "      *-- click" +
                         " *-- web-test");
    }


    static void assertNode(NodeImpl node, String expected) {
        String result = node.accept(new StringNodeVisitor());
        assertThat(result, is(expected.replaceAll("(\\w) ", "$1\n ").trim() + "\n"));
    }


    private static NodeBuilder node(final String name) {
        return new NodeBuilder(name);
    }


    private static class NodeBuilder {
        private final NodeImpl node;


        private NodeBuilder(String name) {
            node = new NodeImpl(name);
        }


        public NodeBuilder add(NodeBuilder subNodeBuilder) {
            node.add(subNodeBuilder.get());
            return this;
        }


        public NodeImpl get() {
            return node;
        }
    }
}
