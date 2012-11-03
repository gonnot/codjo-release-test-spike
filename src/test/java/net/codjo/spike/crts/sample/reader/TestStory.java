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
import net.codjo.spike.crts.api.definition.DefinitionBuilder;
import net.codjo.spike.crts.api.execution.Script;
import net.codjo.spike.crts.kernel.RuleEngine;
import net.codjo.test.common.fixture.DirectoryFixture;
import net.codjo.util.file.FileUtil;
import org.intellij.lang.annotations.Language;
import static net.codjo.test.common.matcher.JUnitMatchers.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
/**
 *
 */
class TestStory {
    private final RuleEngine engine = new RuleEngine();
    private final XmlReader reader = new XmlReader();
    private Exception thrownException;
    private Script loadedScript;


    static TestStory init() {
        return new TestStory();
    }


    public TestStoryGiven given() {
        return new TestStoryGiven();
    }


    class TestStoryGiven {
        public TestStoryGiven pluginDeclare(DefinitionBuilder node) {
            engine.declare(node);
            return this;
        }


        public TestStoryGiven nothing() {
            return this;
        }


        public TestStoryWhen when() {
            engine.start();
            return new TestStoryWhen();
        }
    }
    class TestStoryWhen {
        public TestStoryWhen readScriptFrom(File file) throws Exception {
            try {
                loadedScript = reader.readScript(file);
            }
            catch (Exception e) {
                thrownException = e;
            }
            return this;
        }


        public TestStoryWhen readScript(@Language("XML") String xmlScript) throws Exception {
            DirectoryFixture directory = DirectoryFixture.newTemporaryDirectoryFixture();

            directory.doSetUp();

            File scriptFile = new File(directory, "script.crt");
            try {
                FileUtil.saveContent(scriptFile, xmlScript);
                readScriptFrom(scriptFile);
            }
            finally {
                directory.doTearDown();
            }
            return this;
        }


        public TestStoryThen then() {
            return new TestStoryThen();
        }
    }
    class TestStoryThen {
        public TestStoryThen exceptionHasBeenThrown(Class<? extends Exception> failure) {
            assertThat(thrownException, describedAs("Failure when reading script", is(notNullValue())));
            assertThat(thrownException, is(failure));
            return this;
        }


        public TestStoryThen parsedTreeIs(String expectedTree) throws Exception {
            StringNodeVisitor visitor = new StringNodeVisitor();
            loadedScript.visitFromRoot(visitor);
            assertThat(visitor.result.toString(), is(expectedTree.replaceAll("(\\w) ", "$1\n ").trim() + "\n"));
            return this;
        }
    }
}
