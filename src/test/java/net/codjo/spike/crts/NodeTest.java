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

import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

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

    @Test
    @Ignore
    public void testInsertCommentInOneParentNode() {
        Node node =
                node("gui-test")
                        .add(node("click"))
                        .add(node("assertButton"))
                        .get();


        // Insert comment
        //TODO voir comment ajouter la feuille commentaire

        assertNode(node, "root" +
                " *-- gui-test" +
                "      *-- click" +
                "      *-- assertButton" +
                "           *-- comment");

    }

    @Test
    @Ignore
    public void testInsertCommentInAllNodes() {
        Node node =
                node("gui-test")
                        .add(node("click"))
                        .add(node("assertButton"))
                        .get();


        // Insert comment
        //TODO voir comment ajouter la feuille commentaire

        assertNode(node, "root" +
                " *-- gui-test" +
                "      *-- comment" +
                "      *-- click" +
                "           *-- comment" +
                "      *-- assertButton" +
                "           *-- comment");

    }


    @Test
    @Ignore
    public void testInsertGroupInOneParentNode() {
        Node node =
                node("gui-test")
                        .add(node("click"))
                        .add(node("assertButton"))
                        .get();


        // Insert group
        //TODO voir comment ajouter le group

        assertNode(node, "root" +
                " *-- gui-test" +
                "      *-- click" +
                "      *-- assertButton" +
                "      *-- group" +
                "           *-- gui-test.children"
        );

    }

    @Test
    @Ignore
    public void testInsertGroupNodeInParentNodes() {
        Node node = node("root")
                .add(node("gui-test")
                        .add(node("click"))
                        .add(node("assertButton")))
                .add(node("web-test")
                        .add(node("clickLink"))
                        .add(node("refreshPage")))
                .get();


        // Insert group
        //TODO voir comment ajouter le group

        assertNode(node, "root" +
                " *-- gui-test" +
                "      *-- click" +
                "      *-- assertButton" +
                "      *-- group" +
                "           *-- gui-test.children" +
                " *-- web-test" +
                "      *-- clickLink" +
                "      *-- refreshPage" +
                "      *-- group" +
                "           *-- web-test.children"
        );

    }

    @Test
    @Ignore
    public void testInsertIfInOneParentNode() {
        Node node =
                node("gui-test")
                        .add(node("click"))
                        .add(node("assertButton"))
                        .get();


        // Insert if
        //TODO voir comment ajouter le if then else

        assertNode(node, "root" +
                " *-- gui-test" +
                "      *-- click" +
                "      *-- assertButton" +
                "      *-- if" +
                "           *-- then" +
                "                *-- gui-test.children" +
                "           *-- else" +
                "                *-- gui-test.children"

        );

    }

    static void assertNode(Node node, String expected) {
        String result = node.accept(new StringNodeVisitor());
        assertThat(result, is(expected.replaceAll("(\\w) ", "$1\n ").trim() + "\n"));
    }


    public static NodeBuilder node(final String name) {
        return new NodeBuilder(name);
    }


    public static class NodeBuilder {
        private final Node node;


        private NodeBuilder(String name) {
            node = new Node(name);
        }


        NodeBuilder add(NodeBuilder subNodeBuilder) {
            node.add(subNodeBuilder.get());
            return this;
        }


        public Node get() {
            return node;
        }
    }
}
