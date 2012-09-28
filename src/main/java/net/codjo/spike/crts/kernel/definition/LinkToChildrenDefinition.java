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

package net.codjo.spike.crts.kernel.definition;
/**
 *
 */
public class LinkToChildrenDefinition implements Definition {
    private final String from;
    private final String to;


    public LinkToChildrenDefinition(String from, String to) {
        this.from = from;
        this.to = to;
    }


    // used by drools
    @SuppressWarnings({"UnusedDeclaration"})
    public String getFrom() {
        return from;
    }


    // used by drools
    @SuppressWarnings({"UnusedDeclaration"})
    public String getTo() {
        return to;
    }
}