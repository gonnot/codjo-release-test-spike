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
import net.codjo.spike.crts.api.definition.DefinitionBuilder;
import net.codjo.spike.crts.api.model.Script;
import net.codjo.spike.crts.api.model.Task;
import net.codjo.spike.crts.api.model.behaviour.TaskBehaviour;
import net.codjo.spike.crts.api.model.locator.TaskLocator;
import net.codjo.spike.crts.kernel.definition.RuleEngine;
import net.codjo.spike.crts.kernel.model.TaskFinder;
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


    public static interface ParserUseCase {
        public void perform(ScriptParser parser) throws Exception;
    }

    class TestStoryGiven {
        public TestStoryGiven pluginDeclare(DefinitionBuilder nodeDefinition) {
            engine.declare(nodeDefinition);
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
        private ScriptParser parser = new ScriptParser(engine.getRootNode());


        public TestStoryWhen run(ParserUseCase runner) throws Exception {
            try {
                runner.perform(parser);
            }
            catch (Exception e) {
                thrownException = e;
            }
            return this;
        }


        public TestStoryWhen readTag(String name) throws SyntaxErrorException {
            parser.readTask(name, TaskLocator.NO_LOCATOR);
            return this;
        }


        public TestStoryThen then() {
            loadedScript = parser.getScript();
            return new TestStoryThen();
        }
    }
    class TestStoryThen {
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
            assertThat(currentTask, is(notNullValue()));
            assertThat(currentTask.getName(), is(taskName));
            return this;
        }


        public TestStoryThen hasLocator(String expectedStackFormat) throws Exception {
            ensureTaskHasBeenCalled();
            assertThat(currentTask.getLocator().toShortDescription(), is(expectedStackFormat));
            return this;
        }


        public TestStoryThen hasLocatorStack(String expected) throws Exception {
            ensureTaskHasBeenCalled();
            assertThat(currentTask.getLocator().toLongDescription(), is(expected));
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
