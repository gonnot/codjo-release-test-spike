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
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import net.codjo.spike.crts.kernel.RuleEngine;
import net.codjo.test.common.XmlUtil;
import net.codjo.test.common.fixture.DirectoryFixture;
import net.codjo.util.file.FileUtil;
import org.hamcrest.Description;
import org.intellij.lang.annotations.Language;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.internal.matchers.TypeSafeMatcher;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import static net.codjo.spike.crts.api.definition.DefinitionBuilder.node;
import static net.codjo.test.common.matcher.JUnitMatchers.*;
/**
 *
 */
public class XsdWriterTest {
    private DirectoryFixture directory = DirectoryFixture.newTemporaryDirectoryFixture();
    private File xsdFile = new File(directory, "test-release-sample.xsd");
    private XsdWriter xsdWriter = new XsdWriter();
    private RuleEngine engine = new RuleEngine();


    @Test
    public void testNoPlugin() throws Exception {
        xsdWriter.createXsdFile(engine.getRootNode(), xsdFile);

        XmlUtil.assertEquivalent(FileUtil.loadContent(getClass().getResource("XsdWriter-noPlugin.xsd")),
                                 FileUtil.loadContent(xsdFile));
    }


    @Test
    public void testOneSimpleTag() throws Exception {
        engine.declare(node("file-assert"));
        engine.start();

        xsdWriter.createXsdFile(engine.getRootNode(), xsdFile);

        XmlUtil.assertEquivalent(FileUtil.loadContent(getClass().getResource("XsdWriter-oneSimpleTag.xsd")),
                                 FileUtil.loadContent(xsdFile));
    }


    @Test
    public void testDefineOneTag() throws Exception {
        engine.declare(node("file-assert"));
        engine.start();

        xsdWriter.createXsdFile(engine.getRootNode(), xsdFile);

        assertXmlCompliant(xml("<release-test/>"));

        assertXmlCompliant(xml("<release-test>\n"
                               + "    <file-assert/>\n"
                               + "</release-test>"));

        assertXmlCompliant(xml("<release-test>\n"
                               + "    <file-assert/>\n"
                               + "    <file-assert/>\n"
                               + "</release-test>"));

        assertXmlNotCompliant(xml("<release-test>\n"
                                  + "    <file-assert>\n"
                                  + "       <file-assert/>\n"
                                  + "    </file-assert>"
                                  + "</release-test>"));
    }


    private void assertXmlNotCompliant(String xml) throws SAXException, ParserConfigurationException, IOException {
        assertThat(xml, is(not(xsdCompliant(xsdFile))));
    }


    private void assertXmlCompliant(String xml) throws Exception {
        assertThat(xml, is(xsdCompliant(xsdFile)));
    }


    @Test
    public void testTagWithOneChild() throws Exception {
        engine.declare(node("copy-to-inbox")
                             .add(node("variable")));
        engine.start();

        xsdWriter.createXsdFile(engine.getRootNode(), xsdFile);

        XmlUtil.assertEquivalent(FileUtil.loadContent(getClass().getResource("XsdWriter-tagWithOneChild.xsd")),
                                 FileUtil.loadContent(xsdFile));
    }


    @Test
    public void testTagWithOneAddedChild() throws Exception {
        engine.declare(node("copy-to-inbox"));
        engine.declare(node("variable").asChildOf("copy-to-inbox"));
        engine.start();

        xsdWriter.createXsdFile(engine.getRootNode(), xsdFile);

        XmlUtil.assertEquivalent(FileUtil.loadContent(getClass().getResource("XsdWriter-tagWithOneChild.xsd")),
                                 FileUtil.loadContent(xsdFile));
    }


    @Test
    @Ignore
    public void testTagLinkedToOtherTagChild() throws Exception {
        engine.declare(node("gui-test")
                             .add(node("click")));
        engine.declare(node("group").asChildOf("gui-test")
                             .addChildrenOf("gui-test"));
        engine.start();

        xsdWriter.createXsdFile(engine.getRootNode(), xsdFile);

        XmlUtil.assertEquivalent(FileUtil.loadContent(getClass().getResource("XsdWriter-tagLinkedToOtherTagChild.xsd")),
                                 FileUtil.loadContent(xsdFile));
    }


    @Before
    public void setUp() throws Exception {
        directory.doSetUp();
    }


    @After
    public void tearDown() throws Exception {
        directory.doTearDown();
    }


    private static String xml(@Language("XML") String xml) throws SAXException, ParserConfigurationException, IOException {
        return xml;
    }


    private static IsXsdCompliant xsdCompliant(File xsdFile) {
        return new IsXsdCompliant(xsdFile);
    }


    public static class IsXsdCompliant extends TypeSafeMatcher<String> {
        private File xsdFile;
        private Exception failingValidation;


        public IsXsdCompliant(File xsdFile) {
            this.xsdFile = xsdFile;
        }


        @Override
        public boolean matchesSafely(String xml) {
            try {
                SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                Schema schema = factory.newSchema(xsdFile);
                Validator validator = schema.newValidator();

                DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                Document document = parser.parse(new InputSource(new StringReader(xml)));
                validator.validate(new DOMSource(document));
                return true;
            }
            catch (Exception e) {
                this.failingValidation = e;
                return false;
            }
        }


        public void describeTo(Description description) {
            if (failingValidation != null) {
                description.appendText("Not compliant with XSD : " + failingValidation.getLocalizedMessage());
            }
            else {
                description.appendText("Compliant with XSD");
            }
        }
    }
}
