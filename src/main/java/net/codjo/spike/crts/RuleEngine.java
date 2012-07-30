package net.codjo.spike.crts;
import java.io.File;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
    private final List<Node> nodes = new ArrayList<Node>();
    private final List<URL> rulesUrls = new ArrayList<URL>();
    private WorkingMemory workingMemory;


    public RuleEngine() {
    }


    public List<URL> getRuleFiles() {
        return rulesUrls;
    }


    public void addRulesFile(File rulesFile) {
        try {
            addRulesFile(rulesFile.toURI().toURL());
        }
        catch (MalformedURLException e) {
            throw new RuntimeException("Impossible de trouver l'url correspondant au fichier", e);
        }
    }


    public void addRulesFile(URL rulesUrl) {
        rulesUrls.add(rulesUrl);
    }


    public void checkRuleFiles() throws Exception {
        createRuleBase();
    }


    public void start() throws Exception {
        RuleBase rules = createRuleBase();
        workingMemory = rules.newStatefulSession();
    }


    public void insert(Node node) {
        checkStarted();

        workingMemory.insert(node);
        workingMemory.fireAllRules();

//        updateWorkingMemory();

        nodes.add(node);
    }


    public void retract(Node node) {
        checkStarted();

        workingMemory.retract(workingMemory.getFactHandle(node));
        workingMemory.fireAllRules();

        nodes.remove(node);

//        updateWorkingMemory();
    }


    public List<Node> getAllJobs() {
        return getJobs(null);
    }


    public List<Node> getRunningJobs() {
        return getJobs(State.RUNNING);
    }


    public List<Node> getJobs(State status) {
        checkStarted();

        List<Node> someJobs = new ArrayList<Node>();
        for (Iterator iterator = workingMemory.iterateObjects(); iterator.hasNext();) {
            @SuppressWarnings("unchecked")
            Node job = (Node)iterator.next();
            if (status == null || status == job.getState()) {
                someJobs.add(job);
            }
        }
        return someJobs;
    }


/*
    private List<Node> getOrderedWaitingJobs() {
        List<Node> waitingJobs = new ArrayList<Node>();
        for (Node job : nodes) {
            if (job.getState() == State.WAITING) {
                waitingJobs.add(job);
            }
        }
        return waitingJobs;
    }
*/


/*
    private void updateWorkingMemory() {
        for (Node waitingJob : getOrderedWaitingJobs()) {
            waitingJob.setState(State.NEW);
            FactHandle factHandle = workingMemory.getFactHandle(waitingJob);
            workingMemory.update(factHandle, waitingJob);
            workingMemory.fireAllRules();
        }
    }
*/


    private void checkStarted() {
        if (workingMemory == null) {
            throw new RuntimeException("Rule engine must be started");
        }
    }


    private RuleBase createRuleBase() throws Exception {
        Properties properties = new Properties();
        properties.put("drools.dialect.java.compiler", "JANINO");
        PackageBuilderConfiguration conf = new PackageBuilderConfiguration(properties);

        PackageBuilder builder = new PackageBuilder(conf);
        RuleBase ruleBase = RuleBaseFactory.newRuleBase();

        if (rulesUrls.isEmpty()) {
            URL resource = getClass().getResource("/META-INF/workflow.drl");
            if (resource == null) {
                resource = getClass().getResource("default-rules.drl");
            }
            rulesUrls.add(resource);
        }

        for (URL rulesUrl : rulesUrls) {
            try {
                builder.addPackageFromDrl(new InputStreamReader(rulesUrl.openStream()));
                Package pkg = builder.getPackage();
                ruleBase.addPackage(pkg);
            }
            catch (Exception e) {
                throw new Exception(String.format("Rule File load error '%s' !!!", extractFileName(rulesUrl)), e);
            }
        }

        return ruleBase;
    }


    private String extractFileName(URL rulesUrl) {
        String path = rulesUrl.getPath();
        return path.substring(path.lastIndexOf("/") + 1);
    }
}