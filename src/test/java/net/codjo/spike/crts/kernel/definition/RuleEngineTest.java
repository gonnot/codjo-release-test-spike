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
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import static net.codjo.spike.crts.api.definition.DefinitionBuilder.node;
import static net.codjo.spike.crts.kernel.definition.NodeImplTest.assertNode;
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
        engine.declare(node("gui-test"));

        assertRootNode("release-test" +
                       " *-- gui-test");
    }


    @Test
    public void testOrphanedSubTreeIsAttachedToRoot() throws Exception {
        engine.declare(node("gui-test")
                             .containing(node("click")));

        assertRootNode("release-test" +
                       " *-- gui-test" +
                       "      *-- click");
    }


    @Test
    public void testInsertNodeInOneSpecificParentNode() {
        engine.declare(node("gui-test")
                             .containing(node("click"))
                             .containing(node("assertButton")));

        engine.declare(node("comment").asChildOf("assertButton"));

        assertRootNode("release-test" +
                       " *-- gui-test" +
                       "      *-- assertButton" +
                       "           *-- comment" +
                       "      *-- click");
    }


    @Test
    public void testInsertNodeInAllNodes() {
        engine.declare(node("gui-test")
                             .containing(node("click"))
                             .containing(node("assertButton")));

        engine.declare(node("comment").asChildOfMatchingNodes(".*"));

        assertRootNode("release-test" +
                       " *-- gui-test" +
                       "      *-- comment" +
                       "      *-- assertButton" +
                       "           *-- comment" +
                       "      *-- click" +
                       "           *-- comment");
    }


    @Test
    public void testInsertNodeInSomeSpecificNodes() {
        engine.declare(node("gui-test")
                             .containing(node("click-button"))
                             .containing(node("click-menu")));

        engine.declare(node("comment").asChildOfMatchingNodes("click-.*"));

        assertRootNode("release-test" +
                       " *-- gui-test" +
                       "      *-- click-menu" +
                       "           *-- comment" +
                       "      *-- click-button" +
                       "           *-- comment"
        );
    }


    @Test
    public void testInsertGroupInOneParentNode() {
        engine.declare(node("gui-test")
                             .containing(node("click"))
                             .containing(node("assertButton")));

        // Insert group
        engine.declare(node("group").asChildOf("gui-test")
                             .containingChildrenOf("gui-test"));

        assertRootNode("release-test" +
                       " *-- gui-test" +
                       "      *-- group" +
                       "           *-- gui-test.children" +
                       "      *-- assertButton" +
                       "      *-- click");
    }


    @Test
    @Ignore
    public void testInsertGroupNodeInParentNodes() {
        engine.declare(node("gui-test")
                             .containing(node("click"))
                             .containing(node("assertButton")),
                       node("web-test")
                             .containing(node("clickLink"))
                             .containing(node("refreshPage")));

        // Insert group
        //to-do voir comment ajouter le group

        assertRootNode("release-test" +
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
    public void testInsertIfInOneParentNode() {
        engine.declare(node("gui-test")
                             .containing(node("click"))
                             .containing(node("assertButton")));

        engine.declare(node("if").asChildOf("gui-test")
                             .containing(node("then")
                                               .containingChildrenOf("gui-test"))
                             .containing(node("else")
                                               .containingChildrenOf("gui-test")));

        assertRootNode("release-test" +
                       " *-- gui-test" +
                       "      *-- if" +
                       "           *-- else" +
                       "                *-- gui-test.children" +
                       "           *-- then" +
                       "                *-- gui-test.children" +
                       "      *-- assertButton" +
                       "      *-- click"
        );
    }


    private void assertRootNode(String expectedGraph) {
        engine.start();
        assertNode(engine.getRootNode(), expectedGraph);
    }
}
