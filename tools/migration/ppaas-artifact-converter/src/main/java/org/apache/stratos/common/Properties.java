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

package org.apache.stratos.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Had to wrap {@link Property} array using a class, since there's a bug in current
 * stub generation.
 */
public class Properties implements Serializable {

    private static final long serialVersionUID = -9094584151615076171L;
    private static final Logger log = LoggerFactory.getLogger(Properties.class);
    private List<Property> properties;

    public Properties() {
        this.properties = new ArrayList<Property>();
    }

    public Property[] getProperties() {
        return properties.toArray(new Property[properties.size()]);
    }

    public void addProperty(Property property) {
        try {
            this.properties.add((Property) property.clone());
        } catch (CloneNotSupportedException e) {
            String msg = "JSON syntax exception in fetching auto scale policies";
            log.error(msg, e);
        }
    }

    public Property getProperty(String name) {
        for (Property property : properties) {
            if (property.getName().equals(name)) {
                return property;
            }
        }
        return null;
    }

    public void setProperties(Property[] properties) {
        this.properties = new ArrayList<Property>();
        Collections.addAll(this.properties, properties.clone());
    }

    @Override public String toString() {
        return "Properties [properties=" + properties + "]";
    }

    @Override public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((properties == null) ? 0 : properties.hashCode());
        return result;
    }

    @Override public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Properties other = (Properties) obj;
        if (properties == null) {
            if (other.properties != null)
                return false;
        } else if (!Arrays.equals(other.getProperties(), this.getProperties()))
            return false;
        return true;
    }

}