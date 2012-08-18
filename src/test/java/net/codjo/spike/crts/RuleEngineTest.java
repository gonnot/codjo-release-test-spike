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

package net.codjo.spike.crts;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import static net.codjo.spike.crts.NodeTest.assertNode;
import static net.codjo.spike.crts.model.definition.DefinitionBuilder.node;
/**
 *
 */
public class RuleEngineTest {
    private RuleEngine engine;


    @Before
    public void setUp() {
        engine = new RuleEngine();
    }


    @Test
    public void testOrphanNodeAreAttachedToRoot() throws Exception {
        engine.insert(node("gui-test"));

        assertRootNode("root" +
                       " *-- gui-test");
    }


    @Test
    public void testOrphanedSubTreeIsAttachedToRoot() throws Exception {
        engine.insert(node("gui-test")
                            .add(node("click")));

        assertRootNode("root" +
                       " *-- gui-test" +
                       "      *-- click");
    }


    @Test
    public void testInsertNodeInOneSpecificParentNode() {
        engine.insert(node("gui-test")
                            .add(node("click"))
                            .add(node("assertButton")));

        engine.insert(node("comment").asChildOf("assertButton"));

        assertRootNode("root" +
                       " *-- gui-test" +
                       "      *-- assertButton" +
                       "           *-- comment" +
                       "      *-- click");
    }


    @Test
    @Ignore
    public void testInsertCommentInAllNodes() {
        engine.insert(node("gui-test")
                            .add(node("click"))
                            .add(node("assertButton")));

        // Insert comment
        //to-do voir comment ajouter la feuille commentaire

        assertRootNode("root" +
                       " *-- gui-test" +
                       "      *-- comment" +
                       "      *-- click" +
                       "           *-- comment" +
                       "      *-- assertButton" +
                       "           *-- comment");
    }


    @Test
    @Ignore
    public void testInsertGroupInOneParentNode() {
        engine.insert(node("gui-test")
                            .add(node("click"))
                            .add(node("assertButton")));

        // Insert group
        //to-do voir comment ajouter le group

        assertRootNode("root" +
                       " *-- gui-test" +
                       "      *-- click" +
                       "      *-- assertButton" +
                       "      *-- group" +
                       "           *-- gui-test.children");
    }


    @Test
    @Ignore
    public void testInsertGroupNodeInParentNodes() {
        engine.insert(node("gui-test")
                            .add(node("click"))
                            .add(node("assertButton")),
                      node("web-test")
                            .add(node("clickLink"))
                            .add(node("refreshPage")));

        // Insert group
        //to-do voir comment ajouter le group

        assertRootNode("root" +
                       " *-- gui-test" +
                       "      *-- click" +
                       "      *-- assertButton" +
                       "      *-- group" +
                       "           *-- gui-test.children" +
                       " *-- web-test" +
                       "      *-- clickLink" +
                       "      *-- refreshPage" +
                       "      *-- group" +
                       "           *-- web-test.children");
    }


    @Test
    @Ignore
    public void testInsertIfInOneParentNode() {
        engine.insert(node("gui-test")
                            .add(node("click"))
                            .add(node("assertButton")));

        // Insert if
        //to-do voir comment ajouter le if then else

        assertRootNode("root" +
                       " *-- gui-test" +
                       "      *-- click" +
                       "      *-- assertButton" +
                       "      *-- if" +
                       "           *-- then" +
                       "                *-- gui-test.children" +
                       "           *-- else" +
                       "                *-- gui-test.children");
    }


    private void assertRootNode(String expectedGraph) {
        engine.start();
        assertNode(engine.getRootNode(), expectedGraph);
    }
}
