/*
 * Copyright 2016-present Open Networking Laboratory
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.onosproject.yangutils.datamodel;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents YANG data node identifier which is a combination of name and namespace.
 * Namespace will be present only if node is module/sub-module or augmented node.
 */
public class YangSchemaNodeIdentifier implements Serializable {

    private static final long serialVersionUID = 806201648L;

    // Name of YANG data node.
    private String name;

    // Namespace of YANG data node.
    private String namespace;

    /**
     * Creates an instance of YANG data node identifier.
     */
    public YangSchemaNodeIdentifier() {
    }

    /**
     * Returns the name of the node.
     *
     * @return name of the node
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name of the node.
     *
     * @param name name of the node
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns namespace of the node.
     *
     * @return namespace of the node
     */
    public String getNameSpace() {
        return namespace;
    }

    /**
     * Sets namespace of the node.
     *
     * @param namespace namespace of the node
     */
    public void setNameSpace(String namespace) {
        this.namespace = namespace;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }
        if (obj instanceof YangSchemaNodeIdentifier) {
            final YangSchemaNodeIdentifier other = (YangSchemaNodeIdentifier) obj;
            return Objects.equals(name, other.name) && Objects.equals(namespace, other.namespace);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, namespace);
    }
}
