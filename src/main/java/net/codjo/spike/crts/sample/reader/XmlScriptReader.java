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

package net.codjo.spike.crts.sample.reader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import net.codjo.spike.crts.api.execution.ExecutionNodeBuilder;
import net.codjo.spike.crts.api.execution.Script;
import net.codjo.spike.crts.api.execution.ScriptBuilder;
import net.codjo.spike.crts.api.execution.behaviour.EmptyBehaviour;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.DefaultHandler2;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
/**
 *
 */
public class XmlScriptReader {
    private ScriptBuilder scriptBuilder = new ScriptBuilder();


    public Script readScript(File file) throws IOException {
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        try {
            XMLReader xmlReader = XMLReaderFactory.createXMLReader();
            DefaultHandler handler = new ScriptParserHandler();
            xmlReader.setContentHandler(handler);
            xmlReader.setErrorHandler(handler);
            xmlReader.setProperty("http://xml.org/sax/properties/lexical-handler", handler);
            xmlReader.setProperty("http://xml.org/sax/properties/declaration-handler", handler);
            FileReader reader = new FileReader(file);
            try {
                xmlReader.parse(new InputSource(reader));
            }
            finally {
                reader.close();
            }
        }
        catch (Exception error) {
            throw new IOException(error);
        }
        return scriptBuilder.get();
    }


    private class ScriptParserHandler extends DefaultHandler2 {
        private Locator locator;


        @Override
        public void setDocumentLocator(Locator locator) {
            this.locator = locator;
            super.setDocumentLocator(locator);
        }


        @Override
        public void elementDecl(String name, String model) throws SAXException {
            System.out.println("XmlReader$ScriptParserHandler.elementDecl " + name + " " + model);
            System.out.println("-->" + locator.getLineNumber());
        }


        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            System.out.print("read tag > " + uri + ", " + localName + ", " + qName + ", " + attributes.getLength());
            scriptBuilder.add(ExecutionNodeBuilder.tagWith(new EmptyBehaviour()));
            loc();
        }


        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            System.out.print("end tag < " + qName);
            loc();
        }


        private void loc() {
            System.out.println(" [" + locator.getLineNumber() + "," + locator.getColumnNumber() + "]");
        }


        @Override
        public void startEntity(String name) throws SAXException {
            System.out.println("XmlReader$ScriptParserHandler.startEntity " + name);
            System.out.println("-->" + locator.getLineNumber());
        }


        @Override
        public void warning(SAXParseException exception)
              throws SAXException {
            System.out.println("XmlReader$ScriptParserHandler.warning");
        }


        @Override
        public void error(SAXParseException exception)
              throws SAXException {
            System.out.println("XmlReader$ScriptParserHandler.error");
        }


        @Override
        public void fatalError(SAXParseException exception)
              throws SAXException {
            System.out.println("XmlReader$ScriptParserHandler.fatalError");
        }
    }
}
