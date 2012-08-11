/*
 * codjo (Prototype)
 * =================
 *
 *    Copyright (C) 2012, $YEAR$ by codjo.net
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

package net.codjo.spike.crts;

import org.junit.Test;
import static net.codjo.spike.crts.NodeBuilder.node;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

// @RunWith(Enclosed.class)
public class NodeTest {
    @Test
    public void testVisitOneNode() throws Exception {
        Node node = new Node("root");
        assertNode(node, "root");
    }


    @Test
    public void testVisitWithOneSubNode() throws Exception {
        Node node =
              node("root")
                    .add(node("gui-test"))
                    .get();
        assertNode(node, "root" +
                         " *-- gui-test");
    }


    @Test
    public void testVisitWithSubNode() throws Exception {
        Node node =
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
        Node node =
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


    static void assertNode(Node node, String expected) {
        String result = node.accept(new StringNodeVisitor());
        assertThat(result, is(expected.replaceAll("(\\w) ", "$1\n ").trim() + "\n"));
    }
}
