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
import java.util.ArrayDeque;
import net.codjo.spike.crts.api.execution.Script;
import net.codjo.spike.crts.api.parser.FileTagLocator;
import net.codjo.spike.crts.api.parser.ScriptParser;
import net.codjo.spike.crts.api.parser.SyntaxErrorException;
import net.codjo.spike.crts.api.parser.TagBuilder;
import net.codjo.spike.crts.api.parser.TagLocator;
import net.codjo.spike.crts.kernel.Node;
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
    private final ScriptParser scriptParser;


    public XmlScriptReader(Node syntaxTree) {
        scriptParser = new ScriptParser(syntaxTree);
    }


    public Script readScript(File file) throws IOException {
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        try {
            XMLReader xmlReader = XMLReaderFactory.createXMLReader();
            DefaultHandler handler = new ScriptParserHandler(file);
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
        catch (SyntaxErrorException error) {
            throw error;
        }
        catch (Exception error) {
            throw new IOException(error);
        }
        return scriptParser.getScript();
    }


    private class ScriptParserHandler extends DefaultHandler2 {
        private Locator locator;
        private ArrayDeque<TagBuilder> builders = new ArrayDeque<TagBuilder>();
        private File file;


        ScriptParserHandler(File file) {
            this.file = file;
        }


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
            displayLocalisation();
            if (builders.isEmpty()) {
                builders.push(scriptParser.readTag(qName, currentLocator()));
            }
            else {
                TagBuilder builder = builders.peek().readSubTag(qName, currentLocator());
                builders.push(builder);
            }
        }


        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            builders.pop();
            displayLocalisation();
        }


        private void displayLocalisation() {
            System.out.println(" [" + locator.getLineNumber() + "," + locator.getColumnNumber() + "]");
        }


        @Override
        public void startEntity(String name) throws SAXException {
            System.out.println("XmlReader$ScriptParserHandler.startEntity " + name);
            System.out.println("-->" + locator.getLineNumber());
        }


        @Override
        public void warning(SAXParseException exception) throws SAXException {
            System.out.println("XmlReader$ScriptParserHandler.warning");
        }


        @Override
        public void error(SAXParseException exception) throws SAXException {
            System.out.println("XmlReader$ScriptParserHandler.error");
        }


        @Override
        public void fatalError(SAXParseException exception) throws SAXException {
            System.out.println("XmlReader$ScriptParserHandler.fatalError");
        }


        private TagLocator currentLocator() {
            return new FileTagLocator(file, locator.getLineNumber(), locator.getColumnNumber());
        }
    }
}
