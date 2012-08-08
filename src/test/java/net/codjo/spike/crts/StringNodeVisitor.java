package net.codjo.spike.crts;

public class StringNodeVisitor implements NodeVisitor<String> {
    private static final String BRANCH = " *-- ";
    private static final String INDENT = "     ";
    private int level = 0;

    public String visit(Node node) {
        StringBuilder builder = new StringBuilder();

        if (level > 0) {
            for (int i = 1; i < level; i++) {
                builder.append(INDENT);
            }
            builder.append(BRANCH);
        }
        builder.append(node.getName()).append('\n');

        level++;
        for (Node subNode : node.getNodes()) {
            builder.append(subNode.accept(this));
        }
        level--;

        return builder.toString();
    }
}
