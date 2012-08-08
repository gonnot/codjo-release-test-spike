package net.codjo.spike.crts;
import org.junit.Before;
import org.junit.Test;
import static net.codjo.test.common.matcher.JUnitMatchers.*;
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
    public void test_defaultRulesFile() throws Exception {
        engine.start();

        Node node = new Node("node");
        node.setState(State.NEW);

        engine.insert(node);

        assertThat(node.getState(), is(State.WAITING));
    }
}
