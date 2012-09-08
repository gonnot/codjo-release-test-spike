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
import java.io.StringWriter;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import net.codjo.spike.crts.kernel.RuleEngine;
import net.codjo.util.file.FileUtil;
import org.hamcrest.Description;
import org.intellij.lang.annotations.Language;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.internal.matchers.TypeSafeMatcher;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import static net.codjo.spike.crts.api.definition.DefinitionBuilder.node;
import static net.codjo.test.common.XmlUtil.assertEquivalent;
import static net.codjo.test.common.matcher.JUnitMatchers.*;
/**
 *
 */
public class XsdWriterTest {
    private XsdWriter xsdWriter = new XsdWriter();
    private RuleEngine engine = new RuleEngine();


    @Test
    public void test_noTag_xsdCheck() throws Exception {
        String resultingXsd = createXsd(engine, xsdWriter);

        assertEquivalent(expectedXsd("XsdWriter-noPlugin.xsd"),
                         resultingXsd);
    }


    @Test
    public void test_oneTag() throws Exception {
/*
        XsdWriterTestUtil.init()
              .simulatePluginInit()
              .pluginDeclare(node("file-assert"))

              .simulatePluginEndOfInit()
              .generateXsd()

              .assertXmlCompliant(xml("<release-test/>"));

        XsdWriterTestUtil.init()
              .given()
              .pluginDeclare(node("file-assert"))

              .when()
              .generateXsd()

              .then()

              .xml("<release-test/>")
              .isXsdCompliant()

              .xml("<release-test>\n"
                   + "    <file-assert/>\n"
                   + "</release-test>")
              .isXsdCompliant()

              .xml("<release-test>\n"
                   + "    <file-assert>\n"
                   + "       <file-assert/>\n"
                   + "    </file-assert>"
                   + "</release-test>")
              .isNotXsdCompliant()
        ;
*/

        engine.declare(node("file-assert"));
        engine.start();

        String resultingXsd = createXsd(engine, xsdWriter);

        assertXmlCompliant(resultingXsd, xml("<release-test/>"));

        assertXmlCompliant(resultingXsd, xml("<release-test>\n"
                                             + "    <file-assert/>\n"
                                             + "</release-test>"));

        assertXmlCompliant(resultingXsd, xml("<release-test>\n"
                                             + "    <file-assert/>\n"
                                             + "    <file-assert/>\n"
                                             + "</release-test>"));

        assertXmlNotCompliant(resultingXsd, xml("<release-test>\n"
                                                + "    <file-assert>\n"
                                                + "       <file-assert/>\n"
                                                + "    </file-assert>"
                                                + "</release-test>"));
    }


    @Test
    public void test_oneTag_xsdCheck() throws Exception {
        engine.declare(node("file-assert"));
        engine.start();

        String resultingXsd = createXsd(engine, xsdWriter);

        assertEquivalent(expectedXsd("XsdWriter-oneTag.xsd"),
                         resultingXsd);
    }


    @Test
    public void test_tagWithOneChild_xsdCheck() throws Exception {
        engine.declare(node("copy-to-inbox")
                             .add(node("variable")));
        engine.start();

        String resultingXsd = createXsd(engine, xsdWriter);

        assertEquivalent(expectedXsd("XsdWriter-tagWithOneChild.xsd"),
                         resultingXsd);
    }


    @Test
    public void testTagWithOneAddedChild() throws Exception {
        engine.declare(node("copy-to-inbox"));
        engine.declare(node("variable").asChildOf("copy-to-inbox"));
        engine.start();

        String resultingXsd = createXsd(engine, xsdWriter);

        assertEquivalent(expectedXsd("XsdWriter-tagWithOneChild.xsd"),
                         resultingXsd);
    }


    @Test
    @Ignore
    public void testTagLinkedToOtherTagChild() throws Exception {
        engine.declare(node("gui-test")
                             .add(node("click")));
        engine.declare(node("group").asChildOf("gui-test")
                             .addChildrenOf("gui-test"));
        engine.start();

        String resultingXsd = createXsd(engine, xsdWriter);

        assertEquivalent(expectedXsd("XsdWriter-tagLinkedToOtherTagChild.xsd"), resultingXsd);
    }


    private static String expectedXsd(String name) throws IOException {
        return FileUtil.loadContent(XsdWriterTest.class.getResource(name));
    }


    private static String createXsd(RuleEngine engine, XsdWriter xsdWriter) throws IOException {
        StringWriter xsd = new StringWriter();
        xsdWriter.createXsd(engine.getRootNode(), xsd);
        return xsd.toString();
    }

    // ------------------------------------------------------------------------------------------------------------------------------
    // Code below should be moved in codjo-test-common
    // ------------------------------------------------------------------------------------------------------------------------------


    public static void assertXmlNotCompliant(String xml, File xsdFile) throws SAXException, ParserConfigurationException, IOException {
        assertThat(xml, is(not(xsdCompliantWith(xsdFile))));
    }


    public static void assertXmlNotCompliant(String xsdContent, String xml) throws SAXException, ParserConfigurationException, IOException {
        assertThat(xml, is(not(xsdCompliantWith(xsdContent))));
    }


    private static void assertXmlCompliant(String resultingXsd, String xml) throws Exception {
        assertThat(xml, is(xsdCompliantWith(resultingXsd)));
    }


    private static String xml(@Language("XML") String xml) throws SAXException, ParserConfigurationException, IOException {
        return xml;
    }


    public static IsXsdCompliant xsdCompliantWith(File xsdFile) throws IOException {
        return new IsXsdCompliant(xsdFile);
    }


    public static IsXsdCompliant xsdCompliantWith(String xsdContent) {
        return new IsXsdCompliant(xsdContent);
    }


    public static class IsXsdCompliant extends TypeSafeMatcher<String> {
        private String xsdContent;
        private Exception failingValidation;


        public IsXsdCompliant(File xsdFile) throws IOException {
            this.xsdContent = FileUtil.loadContent(xsdFile);
        }


        public IsXsdCompliant(String xsdContent) {
            this.xsdContent = xsdContent;
        }


        @Override
        public boolean matchesSafely(String xml) {
            try {
                SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                Schema schema = factory.newSchema(new StreamSource(new StringReader(xsdContent)));
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
