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
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;
import net.codjo.spike.crts.api.definition.GrammarBuilder;
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
    private NodeImpl rootNode = new NodeImpl("release-test");


    public RuleEngine() {
        RuleBase rules = createRuleBase();
        workingMemory = rules.newStatefulSession();
        workingMemory.setGlobal("rootNode", rootNode);
    }


    public void declare(GrammarBuilder... builders) {
        for (GrammarBuilder builder : builders) {
            for (Definition definition : builder.get()) {
                workingMemory.insert(definition);
            }
        }
    }


    public void start() {
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
            throw new RuntimeException(String.format("Rule File load error '%s' !!!", resource), e);
        }
        return ruleBase;
    }


    public NodeImpl getRootNode() {
        return rootNode;
    }
}