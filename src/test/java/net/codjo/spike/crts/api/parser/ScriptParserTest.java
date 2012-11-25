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
import net.codjo.spike.crts.api.parser.ParserTestStory.ParserUseCase;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import static net.codjo.spike.crts.api.definition.DefinitionBuilder.node;
import static net.codjo.spike.crts.api.parser.TagLocator.NO_LOCATOR;
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
                          parser.readTag("bad-root-tag", NO_LOCATOR);
                      }
                  })

                  .then()
                  .exceptionHasBeenThrown(SyntaxErrorException.class, "Bad root tag [unknown location]");
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

                          TagBuilder releaseTestTag = parser.readTag("release-test", NO_LOCATOR);
                          releaseTestTag.readSubTag("unknown-tag", NO_LOCATOR);
                      }
                  })

                  .then()
                  .exceptionHasBeenThrown(SyntaxErrorException.class, "'unknown-tag' is not allowed in 'release-test' [unknown location]");
        }


        @Test
        public void testOneTag() throws Exception {
            story()
                  .given()
                  .pluginDeclare(node("pause"))

                  .when()
                  .run(new ParserUseCase() {
                      public void perform(ScriptParser parser) throws Exception {

                          TagBuilder releaseTestTag = parser.readTag("release-test", NO_LOCATOR);
                          releaseTestTag.readSubTag("pause", NO_LOCATOR);
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
                          TagBuilder releaseTestTag = parser.readTag("release-test", NO_LOCATOR);

                          releaseTestTag.readSubTag("copy", NO_LOCATOR);
                          releaseTestTag.readSubTag("pause", NO_LOCATOR);
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

                          TagBuilder releaseTestTag = parser.readTag("release-test", NO_LOCATOR);
                          TagBuilder copyTag = releaseTestTag.readSubTag("copy", NO_LOCATOR);
                          copyTag.readSubTag("file", NO_LOCATOR);
                      }
                  })

                  .then()
                  .parsedScriptTreeIs("release-test"
                                      + " *-- copy"
                                      + "      *-- file"
                  );
        }
    }


    private static ParserTestStory story() {
        return ParserTestStory.init();
    }
}
