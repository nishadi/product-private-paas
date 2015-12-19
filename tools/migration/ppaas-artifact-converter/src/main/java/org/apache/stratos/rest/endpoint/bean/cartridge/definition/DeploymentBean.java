/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.stratos.rest.endpoint.bean.cartridge.definition;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "deployment") public class DeploymentBean {

    private String baseDir;
    private List<String> dir;

    public String toString() {
        return " Base Directory: " + baseDir + " Directories: " + getDirectories();
    }

    private String getDirectories() {

        StringBuilder directoryBuilder = new StringBuilder();
        if (dir != null) {
            for (String directory : dir) {
                directoryBuilder.append(directory + " | ");
            }
        }
        return directoryBuilder.toString();
    }

    public String getBaseDir() {
        return baseDir;
    }

    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }

    public List<String> getDir() {
        return dir;
    }

    public void setDir(List<String> dir) {
        this.dir = dir;
    }
}
