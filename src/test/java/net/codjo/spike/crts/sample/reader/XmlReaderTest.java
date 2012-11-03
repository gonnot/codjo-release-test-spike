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

package net.codjo.spike.crts.sample.reader;
import java.io.File;
import java.io.FileNotFoundException;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
@RunWith(Enclosed.class)
public class XmlReaderTest {
    public static class NominalTest {
        @Test()
        public void testFileNotFound() throws Exception {
            story()
                  .given()
                  .nothing()

                  .when()
                  .readScriptFrom(new File("/unknown-path/file.xml"))

                  .then()
                  .exceptionHasBeenThrown(FileNotFoundException.class)
            ;
        }


        @Test()
        public void testEmptyFile() throws Exception {
            story()
                  .given()
                  .nothing()

                  .when()
                  .readScript("<release-test/>")

                  .then()
                  .parsedTreeIs("release-test")
            ;
        }
    }

    public static class ToBeImplementedTest {
        @Ignore
        @Test
        public void testNodeCanBeLocate() throws Exception {
            // Node can be easily found from a LocateContext : filename and path + line number
            //    stack trace idea -> /path/file.crt line 327 in tag gui-test [326,/path/file.crt] in tag release-test[1,/path/file.crt]
        }
    }


    private static TestStory story() {
        return TestStory.init();
    }
}
