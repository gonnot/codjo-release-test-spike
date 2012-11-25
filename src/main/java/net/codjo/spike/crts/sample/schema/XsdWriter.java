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

package net.codjo.spike.crts.sample.schema;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import net.codjo.spike.crts.api.definition.GrammarVisitor;
import net.codjo.spike.crts.api.definition.Node;
import net.codjo.spike.crts.api.definition.NodeChildren;
/**
 *
 */
public class XsdWriter {
    public void createXsd(Node rootNode, Writer outputWriter) throws IOException {
        PrintWriter writer = new PrintWriter(outputWriter);
        writer.println("<?xml version='1.0' encoding='iso-8859-1' standalone='yes'?>\n"
                       + "<xs:schema xmlns:xs='http://www.w3.org/2001/XMLSchema' elementFormDefault='qualified'>\n"
                       + "    <xs:annotation>\n"
                       + "        <xs:appinfo>XML schema for Tests Release files</xs:appinfo>\n"
                       + "        <xs:documentation xml:lang='en'>\n"
                       + "            XSD Schema for test release using plugins : n/a.\n"
                       + "        </xs:documentation>\n"
                       + "    </xs:annotation>\n"
                       + "\n"
                       + "    <xs:element name='release-test' type='release-test-type'/>\n"
                       + "\n");

        rootNode.accept(new XsdVisitor(writer));
        writer.println("</xs:schema>");
    }


    private static class XsdVisitor implements GrammarVisitor {
        private PrintWriter writer;


        XsdVisitor(PrintWriter writer) {
            this.writer = writer;
        }


        public void visitNode(Node node) {
            generateNodeGroupTag(node);
            generateNodeTypeTag(node);

            node.getChildren().visitContent(this);
        }


        public void visitChildren(NodeChildren children) {
        }


        private void generateNodeGroupTag(Node node) {
            if (node.getChildren().isEmpty()) {
                return;
            }

            writer.println("<xs:group name='" + node.getId() + "-children'>");
            writer.println("  <xs:choice>");
            node.getChildren().visitContent(new GroupContentBuilder(writer));
            writer.println("  </xs:choice>");
            writer.println("</xs:group>");
        }


        private void generateNodeTypeTag(Node node) {
            if (node.getChildren().isEmpty()) {
                writer.println("<xs:complexType name='" + node.getId() + "-type' />");
                return;
            }

            writer.println("<xs:complexType name='" + node.getId() + "-type'>");
            writer.println("  <xs:choice minOccurs='0' maxOccurs='unbounded'>");
            writer.println("     <xs:group ref='" + node.getId() + "-children'/>");
            writer.println("  </xs:choice>");
            writer.println("</xs:complexType>");
        }
    }
    private static class GroupContentBuilder implements GrammarVisitor {
        private PrintWriter writer;


        GroupContentBuilder(PrintWriter writer) {
            this.writer = writer;
        }


        public void visitNode(Node node) {
            String childId = node.getId();
            writer.println("<xs:element name='" + childId + "' type='" + childId + "-type'/>");
        }


        public void visitChildren(NodeChildren children) {
            String childId = children.getOwnerId();
            writer.println("<xs:group ref='" + childId + "-children'/>");
        }
    }
}
