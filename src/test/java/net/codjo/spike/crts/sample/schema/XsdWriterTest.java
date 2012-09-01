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

package net.codjo.spike.crts.sample.schema;
import java.io.File;
import net.codjo.spike.crts.kernel.RuleEngine;
import net.codjo.test.common.XmlUtil;
import net.codjo.test.common.fixture.DirectoryFixture;
import net.codjo.util.file.FileUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static net.codjo.spike.crts.api.definition.DefinitionBuilder.node;
/**
 *
 */
public class XsdWriterTest {
    private DirectoryFixture directory = DirectoryFixture.newTemporaryDirectoryFixture();
    private File xsdFile = new File(directory, "test-release-sample.xsd");
    private XsdWriter xsdWriter = new XsdWriter();
    private RuleEngine engine = new RuleEngine();


    @Test
    public void testNoPlugin() throws Exception {
        xsdWriter.createXsdFile(engine.getRootNode(), xsdFile);

        XmlUtil.assertEquivalent(FileUtil.loadContent(getClass().getResource("XsdWriter-noPlugin.xsd")),
                                 FileUtil.loadContent(xsdFile));
    }


    @Test
    public void testOneSimpleTag() throws Exception {
        engine.declare(node("file-assert"));
        engine.start();

        xsdWriter.createXsdFile(engine.getRootNode(), xsdFile);

        XmlUtil.assertEquivalent(FileUtil.loadContent(getClass().getResource("XsdWriter-oneSimpleTag.xsd")),
                                 FileUtil.loadContent(xsdFile));
    }


    @Test
    public void testOneSimpleTagWithOneChild() throws Exception {
        engine.declare(node("copy-to-inbox")
                             .add(node("variable")));
        engine.start();

        xsdWriter.createXsdFile(engine.getRootNode(), xsdFile);

        XmlUtil.assertEquivalent(FileUtil.loadContent(getClass().getResource("XsdWriter-oneSimpleTagWithOneChild.xsd")),
                                 FileUtil.loadContent(xsdFile));
    }


    @Before
    public void setUp() throws Exception {
        directory.doSetUp();
    }


    @After
    public void tearDown() throws Exception {
        directory.doTearDown();
    }
}
