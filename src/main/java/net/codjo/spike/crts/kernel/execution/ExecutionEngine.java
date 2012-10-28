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
import net.codjo.spike.crts.api.execution.ExecutionListener;
import net.codjo.spike.crts.api.execution.ExecutionNode;
import net.codjo.spike.crts.api.execution.ExecutionNodeVisitor;
import net.codjo.spike.crts.api.execution.ScriptBuilder;
/**
 *
 */
public class ExecutionEngine {

    private static final ExecutionContext NO_CONTEXT = null;
    private ExecutionListener listener;


    public void runScript(ScriptBuilder builder) throws Exception {
        if (builder == null) {
            return;
        }
        builder.get().visit(new ExecutionNodeVisitor() {
            public void visit(ExecutionNode node) throws Exception {
                if (listener != null) {
                    listener.before(node);
                }
                node.getBehaviour().run(NO_CONTEXT);
                node.visitChildren(this);
            }
        });
    }


    public void addListener(ExecutionListener executionListener) {
        this.listener = executionListener;
    }
}
