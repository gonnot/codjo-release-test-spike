/*
 * codjo (Prototype)
 * =================
 *
 *    Copyright (C) 2012, $YEAR$ by codjo.net
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

package net.codjo.spike.crts;

public class StringNodeVisitor implements NodeVisitor<String> {
    private static final String BRANCH = " *-- ";
    private static final String INDENT = "     ";
    private int level = 0;


    public String visit(Node node) {
        StringBuilder builder = new StringBuilder();

        if (level > 0) {
            for (int i = 1; i < level; i++) {
                builder.append(INDENT);
            }
            builder.append(BRANCH);
        }
        builder.append(node.getName()).append('\n');

        level++;
        for (Node subNode : node.getNodes()) {
            builder.append(subNode.accept(this));
        }
        level--;

        return builder.toString();
    }
}
