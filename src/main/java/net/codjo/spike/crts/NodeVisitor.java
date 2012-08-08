package net.codjo.spike.crts;

public interface NodeVisitor<T> {
    T visit(Node node);
}
