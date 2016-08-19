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

package org.onosproject.yangutils.translator.tojava;

/**
 * Represents type of temporary files generated.
 */
public final class GeneratedTempFileType {

    /**
     * Attributes definition temporary file.
     */
    public static final int ATTRIBUTES_MASK = 1; //0x1

    /**
     * Getter methods for interface.
     */
    public static final int GETTER_FOR_INTERFACE_MASK = 2; //0x2

    /**
     * Getter methods for class.
     */
    public static final int GETTER_FOR_CLASS_MASK = 4; //0x4

    /**
     * Setter methods for interface.
     */
    public static final int SETTER_FOR_INTERFACE_MASK = 8; //0x8

    /**
     * Setter methods for class.
     */
    public static final int SETTER_FOR_CLASS_MASK = 16; //0x10

    /**
     * Constructor method of class.
     */
    public static final int CONSTRUCTOR_IMPL_MASK = 32; //0x20

    /**
     * Hash code implementation of class.
     */
    public static final int HASH_CODE_IMPL_MASK = 64; //0X40

    /**
     * Equals implementation of class.
     */
    public static final int EQUALS_IMPL_MASK = 128; //0x80

    /**
     * To string implementation of class.
     */
    public static final int TO_STRING_IMPL_MASK = 256; //0x100

    /**
     * Of string implementation of class.
     */
    public static final int OF_STRING_IMPL_MASK = 512; //0x200

    /**
     * Constructor for type class like typedef, union.
     */
    public static final int CONSTRUCTOR_FOR_TYPE_MASK = 1024; //0x400

    /**
     * From string implementation of class.
     */
    public static final int FROM_STRING_IMPL_MASK = 2048; //0x800

    /**
     * Enum implementation of class.
     */
    public static final int ENUM_IMPL_MASK = 4096; //0x1000

    /**
     * Rpc interface of module / sub module.
     */
    public static final int RPC_INTERFACE_MASK = 8192; //0x2000

    /**
     * Rpc implementation of module / sub module.
     */
    public static final int RPC_IMPL_MASK = 16384; //0x4000

    /**
     * Event enum implementation of class.
     */
    public static final int EVENT_ENUM_MASK = 32768; //0X8000

    /**
     * Event method implementation of class.
     */
    public static final int EVENT_METHOD_MASK = 65536; //0x10000

    /**
     * Event subject attribute implementation of class.
     */
    public static final int EVENT_SUBJECT_ATTRIBUTE_MASK = 131072; //0X20000

    /**
     * Event subject getter implementation of class.
     */
    public static final int EVENT_SUBJECT_GETTER_MASK = 262144; //0x40000

    /**
     * Event subject setter implementation of class.
     */
    public static final int EVENT_SUBJECT_SETTER_MASK = 524288; //80000

    /**
     * Add to list method interface for class.
     */
    public static final int ADD_TO_LIST_INTERFACE_MASK = 1048576; //0X100000

    /**
     * Add to list method implementation for class.
     */
    public static final int ADD_TO_LIST_IMPL_MASK = 2097152; //0X200000

    /**
     * Leaf identifier enum attributes for class.
     */
    public static final int LEAF_IDENTIFIER_ENUM_ATTRIBUTES_MASK = 4194304; //0X400000

    /**
     * Is filter content match for leaves class.
     */
    public static final int FILTER_CONTENT_MATCH_FOR_LEAF_MASK = 8388608; //0X800000

    /**
     * Is filter content match for leaf lists class.
     */
    public static final int FILTER_CONTENT_MATCH_FOR_LEAF_LIST_MASK = 16777216; //0X1000000

    /**
     * Is filter content match for nodes class.
     */
    public static final int FILTER_CONTENT_MATCH_FOR_NODES_MASK = 33554432; //0X2000000

    /**
     * Edit config class content for class.
     */
    public static final int EDIT_CONTENT_MASK = 67108864; //0X4000000

    /**
     * Creates an instance of generated temp file type.
     */
    private GeneratedTempFileType() {
    }
}
