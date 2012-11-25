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

package net.codjo.spike.crts.kernel.execution;
import net.codjo.spike.crts.api.execution.ExecutionListener;
import net.codjo.spike.crts.api.model.Task;
import net.codjo.spike.crts.api.model.behaviour.ExecutionContext;
import net.codjo.spike.crts.api.model.behaviour.TaskBehaviour;
import net.codjo.test.common.LogString;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import static net.codjo.spike.crts.api.execution.SimpleTaskBuilder.task;
import static net.codjo.spike.crts.kernel.execution.ExecutionTestStory.story;
@RunWith(Enclosed.class)
public class ExecutionEngineTest {
    public static class BasicExecutionFlowTest {
        @Test
        public void testEmptyGraph() throws Exception {
            story()
                  .given()
                  .nothing()

                  .when()
                  .executeScript()

                  .then()
                  .executionLogEquals("");
        }


        @Test
        public void testOneTag() throws Exception {
            LogString logger = new LogString();

            story(logger)
                  .given()
                  .script(task("n/a", logBehaviour(logger, "tag1")))

                  .when()
                  .executeScript()

                  .then()
                  .executionLogEquals("run(tag1)");
        }


        @Test
        public void testTwoTags() throws Exception {
            LogString logger = new LogString();

            story(logger)
                  .given()
                  .script(task("n/a", logBehaviour(logger, "tag1")),
                          task("n/a", logBehaviour(logger, "tag2")))

                  .when()
                  .executeScript()

                  .then()
                  .executionLogEquals("run(tag1), "
                                      + "run(tag2)");
        }


        @Test
        public void testTagsWithSubTags() throws Exception {
            LogString logger = new LogString();

            story(logger)
                  .given()
                  .script(task("n/a", logBehaviour(logger, "tag1"))
                                .containing(task("n/a", logBehaviour(logger, "tag1-1"))),
                          task("n/a", logBehaviour(logger, "tag2")))

                  .when()
                  .executeScript()

                  .then()
                  .executionLogEquals("run(tag1), "
                                      + "run(tag1-1), "
                                      + "run(tag2)");
        }
    }
    public static class MonitoringTest {
        @Test
        public void testExecutionCanBeMonitoredThroughListener() throws Exception {
            final LogString logger = new LogString();
            story(logger)
                  .given()
                  .script(task("n/a", logBehaviour(logger, "tag1")))

                  .when()
                  .listenExecutionScriptWith(logBeforeAndAfterExecution(logger))

                  .then()
                  .executionLogEquals("before(LoggerBehaviour)"
                                      + ", run(tag1)"
                                      + ", after(LoggerBehaviour)");
        }


        private static ExecutionListener logBeforeAndAfterExecution(final LogString logger) {
            return new ExecutionListener() {
                public void before(Task task) {
                    logger.call("before", task.getBehaviour().getClass().getSimpleName());
                }


                public void after(Task task) {
                    logger.call("after", task.getBehaviour().getClass().getSimpleName());
                }
            };
        }
    }
    public static class ExecutionWorkflowControlledByBehaviourTest {
        @Test
        public void testSkipSubTaskExecution() throws Exception {
            final LogString logger = new LogString();

            story(logger)
                  .given()
                  .script(task("n/a", skipSubTagBehaviour(logger, "father"))
                                .containing(task("n/a", logBehaviour(logger, "skipped-tag"))))

                  .when()
                  .executeScript()

                  .then()
                  .executionLogEquals("run(father)");
        }
    }
    public static class FeaturesToBeImplementedTest {
        @Test
        @Ignore
        public void testDynamicallyAddStaticTag() throws Exception {
            // for loadScript tag type
        }


        @Test
        @Ignore
        public void testDynamicallyAddOneShotTag() throws Exception {
            // tag that are discarded after one execution
        }


        @Test
        @Ignore
        public void testIfTag() throws Exception {
            // tag can block sub-tag execution
        }


        @Test
        @Ignore
        public void testIterateTag() throws Exception {
            // tag can execute n times sub tags (sub-script)
        }


        @Test
        @Ignore
        public void testContextAreLocalToTag() throws Exception {
            // Execution context are visible from the current task and sub-tasks
        }


        @Test
        @Ignore
        public void testContextCanBeUsedToPushData() throws Exception {
            // Execution context are visible from the current task and sub-tasks
        }


        @Test
        @Ignore
        public void testByDefaultSubTagsAreExecuted() throws Exception {
            // Even if a tag do not handle sub-tags, we can add some
        }


        @Test
        @Ignore
        public void testTagCanCatchFailuresFromSubTag() throws Exception {
            // e.g. try catch tag
        }


        @Test
        @Ignore
        public void testAllTagHaveLocalisationData() throws Exception {
            // for loadScript tag type
        }


        @Test
        @Ignore
        public void testErrorHasToGiveCristalClearInformation() throws Exception {
            // where(tag localisation) - what (tag input, error msg, ...) - when (timestamp, after which tag execution, before...)
            // notified in a listener
        }
    }


    private static TaskBehaviour skipSubTagBehaviour(final LogString logger, final String tagName) {
        return new TaskBehaviour() {
            public void run(ExecutionContext context) throws Exception {
                logger.call("run", tagName);
                context.executionWorkflow().skipBodyExecution();
            }
        };
    }


    private static TaskBehaviour logBehaviour(final LogString logString, final String tagName) {
        return new LoggerBehaviour(logString, tagName);
    }
}

