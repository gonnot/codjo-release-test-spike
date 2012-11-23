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
import net.codjo.util.file.FileUtil;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import static net.codjo.spike.crts.api.definition.DefinitionBuilder.node;
@RunWith(Enclosed.class)
public class XsdWriterTest {
    public static class NoTagTest {
        @Test
        public void testGeneratedXsd() throws Exception {
            story()
                  .given()
                  .nothing()

                  .when()
                  .generateXsd()

                  .then()
                  .resultingXsdIsEquivalentTo(xsd("XsdWriter-noTag.xsd"));
        }


        @Test
        public void testXmlCompliance() throws Exception {
            story()
                  .given()
                  .nothing()

                  .when()
                  .generateXsd()

                  .then()
                  .xml("<release-test/>")
                  .isXsdCompliant();
        }


        @Test
        public void testXmlViolation() throws Exception {
            story()
                  .given()
                  .nothing()

                  .when()
                  .generateXsd()

                  .then()
                  .xml("<release-test>\n"
                       + "   <a-tag/>\n"
                       + "</release-test>")
                  .isNotXsdCompliant();
        }
    }
    public static class OneTagTest {
        @Test
        public void testGeneratedXsd() throws Exception {
            story()
                  .given()
                  .pluginDeclare(node("file-assert"))

                  .when()
                  .generateXsd()

                  .then()
                  .resultingXsdIsEquivalentTo(xsd("XsdWriter-oneTag.xsd"))
            ;
        }


        @Test
        public void testXmlCompliance() throws Exception {
            story()
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
                       + "    <file-assert/>\n"
                       + "    <file-assert/>\n"
                       + "</release-test>")
                  .isXsdCompliant();
        }


        @Test
        public void testXmlViolation() throws Exception {
            story()
                  .given()
                  .pluginDeclare(node("file-assert"))

                  .when()
                  .generateXsd()

                  .then()
                  .xml("<release-test>\n"
                       + "    <file-assert>\n"
                       + "       <file-assert/>\n"
                       + "    </file-assert>"
                       + "</release-test>")
                  .isNotXsdCompliant();
        }
    }
    public static class OneChildTest {
        @Test
        public void testGeneratedXsd() throws Exception {
            story()
                  .given()
                  .pluginDeclare(node("copy-to-inbox")
                                       .containing(node("variable")))

                  .when()
                  .generateXsd()

                  .then()
                  .resultingXsdIsEquivalentTo(xsd("XsdWriter-oneChild.xsd"))
            ;
        }


        @Test
        public void testGeneratedXsdWhenAddedChild() throws Exception {
            story()
                  .given()
                  .pluginDeclare(node("copy-to-inbox"))
                  .pluginDeclare(node("variable").asChildOf("copy-to-inbox"))

                  .when()
                  .generateXsd()

                  .then()
                  .resultingXsdIsEquivalentTo(xsd("XsdWriter-oneChild.xsd"))
            ;
        }


        @Test
        public void testXmlCompliance() throws Exception {
            story()
                  .given()
                  .pluginDeclare(node("copy-to-inbox")
                                       .containing(node("variable")))

                  .when()
                  .generateXsd()

                  .then()
                  .xml("<release-test>\n"
                       + "    <copy-to-inbox/>\n"
                       + "    <copy-to-inbox/>\n"
                       + "</release-test>")
                  .isXsdCompliant()

                  .xml("<release-test>\n"
                       + "    <copy-to-inbox>\n"
                       + "          <variable/>\n"
                       + "    </copy-to-inbox>\n"
                       + "</release-test>")
                  .isXsdCompliant();
        }


        @Test
        public void testXmlViolation() throws Exception {
            story()
                  .given()
                  .pluginDeclare(node("file-assert"))

                  .when()
                  .generateXsd()

                  .then()
                  .xml("<release-test>\n"
                       + "    <variable/>\n"
                       + "</release-test>")
                  .isNotXsdCompliant();
        }
    }
    public static class LinkedTagsTest {
        @Test
        public void testTagLinkedToOtherTagChild() throws Exception {
            story()
                  .given()
                  .pluginDeclare(node("gui-test")
                                       .containing(node("click")))
                  .pluginDeclare(node("group").asChildOf("gui-test")
                                       .containingChildrenOf("gui-test"))

                  .when()
                  .generateXsd()

                  .then()
                  .resultingXsdIsEquivalentTo(xsd("XsdWriter-linkedTags.xsd"))
            ;
        }


        @Test
        public void testXmlCompliance() throws Exception {
            story()
                  .given()
                  .pluginDeclare(node("gui-test")
                                       .containing(node("click")))
                  .pluginDeclare(node("group").asChildOf("gui-test")
                                       .containingChildrenOf("gui-test"))

                  .when()
                  .generateXsd()

                  .then()
                  .xml("<release-test>\n"
                       + "    <gui-test/>\n"
                       + "    <gui-test>\n"
                       + "       <click/>\n"
                       + "    </gui-test>\n"
                       + "</release-test>")
                  .isXsdCompliant()

                  .xml("<release-test>\n"
                       + "    <gui-test>\n"
                       + "       <group>\n"
                       + "          <click/>\n"
                       + "       </group>\n"
                       + "    </gui-test>\n"
                       + "</release-test>")
                  .isXsdCompliant();
        }


        @Test
        public void testXmlViolation() throws Exception {
            story()
                  .given()
                  .pluginDeclare(node("gui-test")
                                       .containing(node("click")))
                  .pluginDeclare(node("group").asChildOf("gui-test")
                                       .containingChildrenOf("gui-test"))

                  .when()
                  .generateXsd()

                  .then()
                  .xml("<release-test>\n"
                       + "    <group/>\n"
                       + "</release-test>")
                  .isNotXsdCompliant()

                  .xml("<release-test>\n"
                       + "    <click/>\n"
                       + "</release-test>")
                  .isNotXsdCompliant();
        }
    }
    public static class NotRequiredByTDDStressTest {

        @Test
        public void testXmlCompliance() throws Exception {
            story()
                  .given()
                  .pluginDeclare(node("gui-test")
                                       .containing(node("click")
                                                         .containing(node("component"))))
                  .pluginDeclare(node("group").asChildOf("click")
                                       .containingChildrenOf("click"))

                  .when()
                  .generateXsd()

                  .then()
                  .xml("<release-test>\n"
                       + "    <gui-test>\n"
                       + "       <click>\n"
                       + "          <component/>\n"
                       + "       </click>\n"
                       + "    </gui-test>\n"
                       + "</release-test>")
                  .isXsdCompliant()

                  .xml("<release-test>\n"
                       + "    <gui-test>\n"
                       + "       <click>\n"
                       + "          <component/>\n"
                       + "          <group>\n"
                       + "              <component/>\n"
                       + "          </group>\n"
                       + "       </click>\n"
                       + "    </gui-test>\n"
                       + "</release-test>")
                  .isXsdCompliant();
        }


        @Test
        public void testXmlComplianceOnComplexGraph() throws Exception {
            story()
                  .given()
                  .pluginDeclare(node("gui-test")
                                       .containingChildrenOf("doc"))

                  .pluginDeclare(node("doc")
                                       .containingChildrenOf("author-tag"))

                  .pluginDeclare(node("author-tag")
                                       .containing(node("author-name")))

                  .when()
                  .generateXsd()

                  .then()
                  .xml("<release-test>\n"
                       + "    <author-tag>\n"
                       + "       <author-name/>\n"
                       + "    </author-tag>\n"
                       + "    <doc>\n"
                       + "       <author-name/>\n"
                       + "    </doc>\n"
                       + "    <gui-test>\n"
                       + "       <author-name/>\n"
                       + "    </gui-test>\n"
                       + "</release-test>")
                  .isXsdCompliant();
        }


        @Test
        public void testXmlComplianceOnEvenMoreComplexGraph() throws Exception {
            story()
                  .given()
                  .pluginDeclare(node("gui-test")
                                       .containingChildrenOf("doc"))

                  .pluginDeclare(node("doc")
                                       .containingChildrenOf("author-tag")
                                       .containing(node("author-tag")
                                                         .containing(node("author-name"))))

                  .when()
                  .generateXsd()

                  .then()
                  .xml("<release-test>\n"
                       + "    <doc>\n"
                       + "      <author-tag>\n"
                       + "          <author-name/>\n"
                       + "      </author-tag>\n"
                       + "       <author-name/>\n"
                       + "    </doc>\n"
                       + "    <gui-test>\n"
                       + "       <author-name/>\n"
                       + "    </gui-test>\n"
                       + "</release-test>")
                  .isXsdCompliant();
        }
    }


    private static String xsd(String name) throws IOException {
        return FileUtil.loadContent(XsdWriterTest.class.getResource(name));
    }


    private static SchemaTestStory story() {
        return SchemaTestStory.init();
    }
}
