package net.codjo.spike.crts;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;
import org.drools.RuleBase;
import org.drools.RuleBaseFactory;
import org.drools.WorkingMemory;
import org.drools.compiler.PackageBuilder;
import org.drools.compiler.PackageBuilderConfiguration;
import org.drools.rule.Package;
/**
 *
 */
@SuppressWarnings({"NullableProblems"})
public class RuleEngine {
    private WorkingMemory workingMemory;


    public RuleEngine() {
        RuleBase rules = createRuleBase();
        workingMemory = rules.newStatefulSession();
    }


    public void insert(Node... nodes) {
        for (Node node : nodes) {
            workingMemory.insert(node);
        }
        workingMemory.fireAllRules();
    }


    private RuleBase createRuleBase() {
        Properties properties = new Properties();
        properties.put("drools.dialect.java.compiler", "JANINO");
        PackageBuilderConfiguration configuration = new PackageBuilderConfiguration(properties);

        PackageBuilder builder = new PackageBuilder(configuration);
        RuleBase ruleBase = RuleBaseFactory.newRuleBase();

        URL resource = getClass().getResource("default-rules.drl");
        try {
            builder.addPackageFromDrl(new InputStreamReader(resource.openStream()));
            Package pkg = builder.getPackage();
            ruleBase.addPackage(pkg);
        }
        catch (Exception e) {
            throw new RuntimeException(String.format("Rule File load error '%s' !!!", extractFileName(resource)), e);
        }
        return ruleBase;
    }


    private String extractFileName(URL rulesUrl) {
        String path = rulesUrl.getPath();
        return path.substring(path.lastIndexOf("/") + 1);
    }
}