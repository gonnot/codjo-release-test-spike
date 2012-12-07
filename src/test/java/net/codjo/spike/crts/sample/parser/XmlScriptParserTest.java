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

package net.codjo.spike.crts.sample.parser;
import java.io.File;
import java.io.FileNotFoundException;
import net.codjo.spike.crts.api.parser.SyntaxErrorException;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import static net.codjo.spike.crts.api.definition.DefinitionBuilder.node;
@RunWith(Enclosed.class)
public class XmlScriptParserTest {
    public static class BasicTest {
        @Test
        public void testFileNotFound() throws Exception {
            story()
                  .given()
                  .nothing()

                  .when()
                  .readScriptFrom(new File("/unknown-path/file.xml"))

                  .then()
                  .exceptionHasBeenThrown(FileNotFoundException.class)
            ;
        }


        @Test
        public void testEmptyFile() throws Exception {
            story()
                  .given()
                  .nothing()

                  .when()
                  .readScript("<release-test/>")

                  .then()
                  .parsedScriptTreeIs("release-test")
            ;
        }
    }
    public static class NominalParsingTest {
        @Test
        public void testOneExistingTag() throws Exception {
            story()
                  .given()
                  .pluginDeclare(node("pause"))

                  .when()
                  .readScript("<release-test>\n"
                              + "    <pause/>\n"
                              + "</release-test>")

                  .then()
                  .parsedScriptTreeIs("release-test"
                                      + " *-- pause")
            ;
        }


        @Test
        public void testOneExistingSubTag() throws Exception {
            story()
                  .given()
                  .pluginDeclare(node("gui-test"))
                  .pluginDeclare(node("click").asChildOf("gui-test"))

                  .when()
                  .readScript("<release-test>\n"
                              + "    <gui-test>\n"
                              + "       <click/>\n"
                              + "    </gui-test>\n"
                              + "</release-test>")

                  .then()
                  .parsedScriptTreeIs("release-test"
                                      + " *-- gui-test"
                                      + "      *-- click")
            ;
        }


        @Test
        public void testTwoGroupOfTags() throws Exception {
            story()
                  .given()
                  .pluginDeclare(node("gui-test"))
                  .pluginDeclare(node("click").asChildOf("gui-test"))
                  .pluginDeclare(node("group").asChildOf("gui-test").containingChildrenOf("gui-test"))

                  .when()
                  .readScript("<release-test>\n"
                              + "    <gui-test>\n"
                              + "       <click/>\n"
                              + "        <group>\n"
                              + "            <click/>\n"
                              + "        </group>\n"
                              + "    </gui-test>\n"
                              + "</release-test>")

                  .then()
                  .parsedScriptTreeIs("release-test"
                                      + " *-- gui-test"
                                      + "      *-- click"
                                      + "      *-- group"
                                      + "           *-- click")
            ;
        }
    }
    public static class ErrorManagementTest {
        @Test
        public void testUnexpectedTag() throws Exception {
            story()
                  .given()
                  .nothing()

                  .when()
                  .readScript("tempo.xml",
                              "<release-test>\n"
                              + "    <unexpected-tag/>\n"
                              + "</release-test>")

                  .then()
                  .exceptionHasBeenThrown(SyntaxErrorException.class, "'unexpected-tag' is not allowed in 'release-test' [tempo.xml:(2,22)]")
            ;
        }
    }

    public static class ToBeImplementedTest {
        @Ignore
        @Test
        public void testTaskCanBeLocate() throws Exception {
            // Node can be easily found from a LocateContext : filename and path + line number
            //    stack trace idea -> /path/file.crt line 327 in tag gui-test [326,/path/file.crt] in tag release-test[1,/path/file.crt]
        }
    }


    private static ParserTestStory story() {
        return ParserTestStory.init();
    }
}
