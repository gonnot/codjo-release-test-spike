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
import net.codjo.spike.crts.api.execution.behaviour.ExecutionContext;
import net.codjo.spike.crts.api.execution.behaviour.NodeBehaviour;
import net.codjo.test.common.LogString;
/**
 *
 */
class LoggerBehaviour implements NodeBehaviour {
    private final LogString logString;
    private final String message;


    LoggerBehaviour(LogString logString, String message) {
        this.logString = logString;
        this.message = message;
    }


    public void run(ExecutionContext context) throws Exception {
        logString.call("run", message);
    }
}
