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
import net.codjo.spike.crts.api.execution.ExecutionNodeBuilder;
import net.codjo.spike.crts.api.execution.ScriptBuilder;
import net.codjo.spike.crts.kernel.RuleEngine;
import net.codjo.test.common.LogString;
/**
 *
 */
class TestStory {
    private final RuleEngine engine = new RuleEngine();
    private final LogString logString;
    private ScriptBuilder builder;


    TestStory(LogString logString) {
        this.logString = logString;
    }


    public static TestStory story() {
        return story(new LogString());
    }


    public static TestStory story(LogString logString) {
        return new TestStory(logString);
    }


    public TestStory given() {
        return this;
    }


    public TestStory nothing() {
        return this;
    }


    public TestStory script(ExecutionNodeBuilder... builders) {
        this.builder = new ScriptBuilder();
        for (ExecutionNodeBuilder nodeBuilder : builders) {
            this.builder.add(nodeBuilder);
        }
        return this;
    }


    public TestStory when() {
        engine.start();
        return this;
    }


    public TestStory executeScript() throws Exception {
        new ExecutionEngine().runScript(builder);
        return this;
    }


    public TestStory listenExecutionScript(ExecutionListener listener) throws Exception {
        ExecutionEngine executionEngine = new ExecutionEngine();
        executionEngine.addListener(listener);
        executionEngine.runScript(builder);
        return this;
    }


    public TestStory then() {
        return this;
    }


    public void executionLogEquals(String expectedLog) {
        logString.assertContent(expectedLog);
    }
}
