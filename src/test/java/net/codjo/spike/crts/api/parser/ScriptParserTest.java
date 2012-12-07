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

package net.codjo.spike.crts.api.parser;
import java.io.File;
import net.codjo.spike.crts.api.model.locator.FileTaskLocator;
import net.codjo.spike.crts.api.parser.ParserTestStory.ParserUseCase;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import static net.codjo.spike.crts.api.definition.DefinitionBuilder.node;
import static net.codjo.spike.crts.api.model.locator.TaskLocator.NO_LOCATOR;
/**
 *
 */
@RunWith(Enclosed.class)
public class ScriptParserTest {
    public static class OnlyRootTest {
        @Test
        public void testEmpty() throws Exception {
            story()
                  .given()
                  .nothing()

                  .when()
                  .readTag("release-test")

                  .then()
                  .parsedScriptTreeIs("release-test");
        }


        @Test
        public void testBadRootTag() throws Exception {
            story()
                  .given()
                  .nothing()

                  .when()
                  .run(new ParserUseCase() {
                      public void perform(ScriptParser parser) throws Exception {
                          parser.readTask("bad-root-tag", NO_LOCATOR);
                      }
                  })

                  .then()
                  .exceptionHasBeenThrown(SyntaxErrorException.class, "Bad root task (unknown location)");
        }
    }
    public static class OneLevelTest {
        @Test
        public void testOneUnknownTag() throws Exception {
            story()
                  .given()
                  .nothing()

                  .when()
                  .run(new ParserUseCase() {
                      public void perform(ScriptParser parser) throws Exception {
                          parser.readTask("release-test", NO_LOCATOR)
                                .readSubTask("unknown-tag", NO_LOCATOR);
                      }
                  })

                  .then()
                  .exceptionHasBeenThrown(SyntaxErrorException.class, "'unknown-tag' is not allowed in 'release-test' (unknown location)");
        }


        @Test
        public void testOneTag() throws Exception {
            story()
                  .given()
                  .pluginDeclare(node("pause"))

                  .when()
                  .run(new ParserUseCase() {
                      public void perform(ScriptParser parser) throws Exception {

                          TaskBuilder releaseTestTask = parser.readTask("release-test", NO_LOCATOR);
                          releaseTestTask.readSubTask("pause", NO_LOCATOR);
                      }
                  })

                  .then()
                  .parsedScriptTreeIs("release-test"
                                      + " *-- pause");
        }


        @Test
        public void testTwoTags() throws Exception {
            story()
                  .given()
                  .pluginDeclare(node("pause"))
                  .pluginDeclare(node("copy"))

                  .when()
                  .run(new ParserUseCase() {
                      public void perform(ScriptParser parser) throws Exception {
                          TaskBuilder releaseTestTask = parser.readTask("release-test", NO_LOCATOR);

                          releaseTestTask.readSubTask("copy", NO_LOCATOR);
                          releaseTestTask.readSubTask("pause", NO_LOCATOR);
                      }
                  })

                  .then()
                  .parsedScriptTreeIs("release-test"
                                      + " *-- copy"
                                      + " *-- pause");
        }
    }
    public static class MultiLevelsTest {
        @Test
        public void testNominal() throws Exception {
            story()
                  .given()
                  .pluginDeclare(node("copy"))
                  .pluginDeclare(node("file").asChildOf("copy"))

                  .when()
                  .run(new ParserUseCase() {
                      public void perform(ScriptParser parser) throws Exception {
                          parser.readTask("release-test", NO_LOCATOR)
                                .readSubTask("copy", NO_LOCATOR)
                                .readSubTask("file", NO_LOCATOR);
                      }
                  })

                  .then()
                  .parsedScriptTreeIs("release-test"
                                      + " *-- copy"
                                      + "      *-- file"
                  );
        }
    }
    public static class TaskHasLocatorTest {
        @Test
        public void testNoLocator() throws Exception {
            story()
                  .given()
                  .pluginDeclare(node("pause"))

                  .when()
                  .run(new ParserUseCase() {
                      public void perform(ScriptParser parser) throws Exception {
                          parser.readTask("release-test", NO_LOCATOR)
                                .readSubTask("pause", NO_LOCATOR);
                      }
                  })

                  .then()
                  .task("pause").hasLocator("pause(unknown location)");
        }


        @Test
        public void testFileLocator() throws Exception {
            story()
                  .given()
                  .pluginDeclare(node("pause"))

                  .when()
                  .run(new ParserUseCase() {
                      public void perform(ScriptParser parser) throws Exception {
                          parser.readTask("release-test", new FileTaskLocator(new File("script.xml"), 1, 0))
                                .readSubTask("pause", new FileTaskLocator(new File("script.xml"), 5, 0));
                      }
                  })

                  .then()
                  .task("release-test").hasLocator("release-test(script.xml:1,0)")
                  .task("pause").hasLocator("pause(script.xml:5,0)");
        }


        @Test
        public void testLocatorStackWithTwoNodes() throws Exception {
            story()
                  .given()
                  .pluginDeclare(node("pause"))

                  .when()
                  .run(new ParserUseCase() {
                      public void perform(ScriptParser parser) throws Exception {
                          parser.readTask("release-test", new FileTaskLocator(new File("script.xml"), 1, 0))
                                .readSubTask("pause", new FileTaskLocator(new File("script.xml"), 5, 0));
                      }
                  })

                  .then()
                  .task("release-test").hasLocatorStack("release-test(script.xml:1,0)")
                  .task("pause").hasLocatorStack("pause(script.xml:5,0)\n"
                                                 + "at release-test(script.xml:1,0)");
        }


        @Test
        public void testLocatorStackWithThreeNodes() throws Exception {
            story()
                  .given()
                  .pluginDeclare(node("edit-cell"))
                  .pluginDeclare(node("click").asChildOf("edit-cell"))

                  .when()
                  .run(new ParserUseCase() {
                      public void perform(ScriptParser parser) throws Exception {
                          parser.readTask("release-test", new FileTaskLocator(new File("script.xml"), 1, 0))
                                .readSubTask("edit-cell", new FileTaskLocator(new File("script.xml"), 5, 0))
                                .readSubTask("click", new FileTaskLocator(new File("script.xml"), 6, 2));
                      }
                  })

                  .then()
                  .task("click").hasLocatorStack("click(script.xml:6,2)\n"
                                                 + "at edit-cell(script.xml:5,0)\n"
                                                 + "at release-test(script.xml:1,0)");
        }
    }


    private static ParserTestStory story() {
        return ParserTestStory.init();
    }
}
