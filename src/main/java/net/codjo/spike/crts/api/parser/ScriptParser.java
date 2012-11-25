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
import net.codjo.spike.crts.api.definition.Node;
import net.codjo.spike.crts.api.execution.Script;
import net.codjo.spike.crts.api.execution.ScriptBuilder;
/**
 *
 */
public class ScriptParser {
    private Node grammarTree;
    private ScriptBuilder scriptBuilder = new ScriptBuilder();


    public ScriptParser(Node grammarTree) {
        this.grammarTree = grammarTree;
    }


    public Script getScript() {
        return scriptBuilder.get();
    }


    public TaskBuilder readTag(String tagName, TaskLocator locator) throws SyntaxErrorException {
        if (!grammarTree.getId().equals(tagName)) {
            throw new SyntaxErrorException("Bad root tag", locator);
        }
        return new TaskBuilder(scriptBuilder.get().getRootTask(), grammarTree);
    }
}
