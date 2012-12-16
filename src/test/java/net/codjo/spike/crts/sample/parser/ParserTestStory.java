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
import net.codjo.spike.crts.api.definition.DefinitionBuilder;
import net.codjo.spike.crts.api.model.Script;
import net.codjo.spike.crts.api.model.Task;
import net.codjo.spike.crts.api.model.behaviour.TaskBehaviour;
import net.codjo.spike.crts.api.parser.StringTaskVisitor;
import net.codjo.spike.crts.kernel.definition.RuleEngine;
import net.codjo.spike.crts.kernel.model.TaskFinder;
import net.codjo.test.common.fixture.DirectoryFixture;
import net.codjo.util.file.FileUtil;
import org.intellij.lang.annotations.Language;
import static net.codjo.test.common.matcher.JUnitMatchers.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
/**
 *
 */
class ParserTestStory {
    private final RuleEngine engine = new RuleEngine();
    private Exception thrownException;
    private Script loadedScript;
    private Task currentTask;


    static ParserTestStory init() {
        return new ParserTestStory();
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
            XmlScriptParser scriptParser = new XmlScriptParser(engine.getRootNode());
            try {
                loadedScript = scriptParser.readScript(file);
            }
            catch (Exception e) {
                thrownException = e;
            }
            return this;
        }


        public TestStoryWhen readScript(@Language("XML") String scriptContent) throws Exception {
            return readScript("script.crt", scriptContent);
        }


        public TestStoryWhen readScript(String scriptName, @Language("XML") String scriptContent) throws Exception {
            DirectoryFixture directory = DirectoryFixture.newTemporaryDirectoryFixture();

            directory.doSetUp();

            File scriptFile = new File(directory, scriptName);
            try {
                FileUtil.saveContent(scriptFile, scriptContent);
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
            return exceptionHasBeenThrown(failure, null);
        }


        public TestStoryThen exceptionHasBeenThrown(Class<? extends Exception> failure, String message) {
            assertThat(thrownException, describedAs("Failure when reading script", is(notNullValue())));
            assertThat(thrownException, is(failure));
            if (message != null) {
                assertThat(thrownException.getMessage(), is(message));
            }
            return this;
        }


        public TestStoryThen parsedScriptTreeIs(String expectedTree) throws Exception {
            StringTaskVisitor visitor = new StringTaskVisitor();
            loadedScript.visitFromRoot(visitor);
            assertThat(visitor.getResultingTree(), is(expectedTree.replaceAll("(\\w) ", "$1\n ").trim() + "\n"));
            return this;
        }


        public TestStoryThen task(String taskName) throws Exception {
            currentTask = TaskFinder.find(taskName, loadedScript);
            return this;
        }


        public TestStoryThen hasLocator(String expectedStackFormat) throws Exception {
            ensureTaskHasBeenCalled();
            assertThat(currentTask.getLocator().toShortDescription(), is(expectedStackFormat));
            return this;
        }


        public TestStoryThen hasLocatorStack(String expectedStackFormat) throws Exception {
            ensureTaskHasBeenCalled();
            assertThat(currentTask.getLocator().toLongDescription(), is(expectedStackFormat));
            return this;
        }


        public void hasBehaviourInstanceOf(Class<? extends TaskBehaviour> behaviourClass) {
            ensureTaskHasBeenCalled();
            assertThat(currentTask.getBehaviour(), is(notNullValue()));
            assertThat(currentTask.getBehaviour(), is(behaviourClass));
        }


        private void ensureTaskHasBeenCalled() {
            assertThat(currentTask, describedAs("please call .task(<name>) before using this method", is(notNullValue())));
        }
    }
}
