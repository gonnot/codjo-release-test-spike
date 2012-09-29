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
import net.codjo.spike.crts.api.execution.ExecutionContext;
import net.codjo.spike.crts.api.execution.NodeBehaviour;
import net.codjo.test.common.LogString;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import static net.codjo.spike.crts.api.execution.ExecutionBuilder.tagWith;
import static net.codjo.spike.crts.kernel.execution.TestStory.story;
@RunWith(Enclosed.class)
public class ExecutionEngineTest {
    public static class ExecutionFlowTest {
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
                  .script(tagWith(logBehaviour(logger)))

                  .when()
                  .executeScript()

                  .then()
                  .executionLogEquals("run()");
        }
    }


    private static NodeBehaviour logBehaviour(final LogString logString) {
        return new NodeBehaviour() {
            public void run(ExecutionContext context) throws Exception {
                logString.call("run");
            }
        };
    }
}

