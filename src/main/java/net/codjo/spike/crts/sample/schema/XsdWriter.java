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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import net.codjo.spike.crts.api.definition.DefinitionVisitor;
import net.codjo.spike.crts.api.definition.INode;
/**
 *
 */
public class XsdWriter {
    public void createXsdFile(INode rootNode, File xsdFile) throws IOException {
        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(xsdFile)));
        try {
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
        finally {
            writer.close();
        }
    }


    private static class XsdVisitor implements DefinitionVisitor {
        private PrintWriter writer;


        XsdVisitor(PrintWriter writer) {
            this.writer = writer;
        }


        public void visitNode(INode parent, List<? extends INode> children) {
            generateNodeTypeTag(parent, children);

            for (INode child : children) {
                child.accept(this);
            }
        }


        private void generateNodeTypeTag(INode parent, List<? extends INode> children) {
            writer.println("<xs:complexType name='" + parent.getId() + "-type'>");
            if (!children.isEmpty()) {
                writer.println("  <xs:choice minOccurs='0' maxOccurs='unbounded'>");
            }

            for (INode child : children) {
                String childId = child.getId();
                writer.println("<xs:element name='" + childId + "' type='" + childId + "-type'/>");
            }

            if (!children.isEmpty()) {
                writer.println("  </xs:choice>");
            }
            writer.println("</xs:complexType>");
        }
    }
}
