package net.codjo.spike.crts;

import org.junit.Test;
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


    private static void assertNode(Node node, String expected) {
        String result = node.accept(new StringNodeVisitor());
        assertThat(result, is(expected.replaceAll("(\\w) ", "$1\n ").trim() + "\n"));
    }


    private static NodeBuilder node(final String name) {
        return new NodeBuilder(name);
    }


    private static class NodeBuilder {
        private final Node node;


        private NodeBuilder(String name) {
            node = new Node(name);
        }


        NodeBuilder add(Node subNode) {
            node.add(subNode);
            return this;
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
