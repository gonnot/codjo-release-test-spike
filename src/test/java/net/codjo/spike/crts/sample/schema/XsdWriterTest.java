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
import org.junit.Ignore;
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
                  .resultingXsdIsEquivalentTo(xsd("XsdWriter-noTag.xsd"))
            ;
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
                                       .add(node("variable")))

                  .when()
                  .generateXsd()

                  .then()
                  .resultingXsdIsEquivalentTo(xsd("XsdWriter-tagWithOneChild.xsd"))
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
                  .resultingXsdIsEquivalentTo(xsd("XsdWriter-tagWithOneChild.xsd"))
            ;
        }


        @Test
        public void testXmlCompliance() throws Exception {
            story()
                  .given()
                  .pluginDeclare(node("copy-to-inbox")
                                       .add(node("variable")))

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
    public static class LinkedTagTest {
        @Test
        @Ignore
        public void testTagLinkedToOtherTagChild() throws Exception {
            story()
                  .given()
                  .pluginDeclare(node("gui-test")
                                       .add(node("click")))
                  .pluginDeclare(node("group").asChildOf("gui-test")
                                       .addChildrenOf("gui-test"))

                  .when()
                  .generateXsd()

                  .then()
                  .resultingXsdIsEquivalentTo(xsd("XsdWriter-tagLinkedToOtherTagChild.xsd"))
            ;
        }
    }


    private static String xsd(String name) throws IOException {
        return FileUtil.loadContent(XsdWriterTest.class.getResource(name));
    }


    private static TestStory story() {
        return TestStory.init();
    }
}
