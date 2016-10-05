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

package org.onosproject.yangutils.translator.tojava.utils;

import org.onosproject.yangutils.datamodel.YangCompilerAnnotation;
import org.onosproject.yangutils.datamodel.YangNode;
import org.onosproject.yangutils.translator.exception.TranslatorException;
import org.onosproject.yangutils.translator.tojava.JavaCodeGeneratorInfo;
import org.onosproject.yangutils.translator.tojava.JavaQualifiedTypeInfoTranslator;
import org.onosproject.yangutils.translator.tojava.TempJavaServiceFragmentFiles;
import org.onosproject.yangutils.utils.UtilConstants.Operation;

import java.util.List;

import static java.util.Collections.sort;
import static org.onosproject.yangutils.translator.tojava.utils.BracketType.OPEN_CLOSE_BRACKET;
import static org.onosproject.yangutils.translator.tojava.utils.BracketType.OPEN_CLOSE_BRACKET_WITH_VALUE;
import static org.onosproject.yangutils.translator.tojava.utils.BracketType.OPEN_CLOSE_DIAMOND;
import static org.onosproject.yangutils.translator.tojava.utils.JavaIdentifierSyntax.getEnumJavaAttribute;
import static org.onosproject.yangutils.translator.tojava.utils.StringGenerator.brackets;
import static org.onosproject.yangutils.translator.tojava.utils.StringGenerator.getDefaultDefinition;
import static org.onosproject.yangutils.translator.tojava.utils.StringGenerator.getImportString;
import static org.onosproject.yangutils.translator.tojava.utils.StringGenerator.signatureClose;
import static org.onosproject.yangutils.utils.UtilConstants.ARRAY_LIST;
import static org.onosproject.yangutils.utils.UtilConstants.CLASS_STRING;
import static org.onosproject.yangutils.utils.UtilConstants.COMMA;
import static org.onosproject.yangutils.utils.UtilConstants.DIAMOND_CLOSE_BRACKET;
import static org.onosproject.yangutils.utils.UtilConstants.DIAMOND_OPEN_BRACKET;
import static org.onosproject.yangutils.utils.UtilConstants.EIGHT_SPACE_INDENTATION;
import static org.onosproject.yangutils.utils.UtilConstants.EMPTY_STRING;
import static org.onosproject.yangutils.utils.UtilConstants.ENUM;
import static org.onosproject.yangutils.utils.UtilConstants.EQUAL;
import static org.onosproject.yangutils.utils.UtilConstants.FOUR_SPACE_INDENTATION;
import static org.onosproject.yangutils.utils.UtilConstants.HASH_MAP;
import static org.onosproject.yangutils.utils.UtilConstants.INT;
import static org.onosproject.yangutils.utils.UtilConstants.INT_MAX_RANGE_ATTR;
import static org.onosproject.yangutils.utils.UtilConstants.INT_MIN_RANGE_ATTR;
import static org.onosproject.yangutils.utils.UtilConstants.LIST;
import static org.onosproject.yangutils.utils.UtilConstants.LONG_MAX_RANGE_ATTR;
import static org.onosproject.yangutils.utils.UtilConstants.LONG_MIN_RANGE_ATTR;
import static org.onosproject.yangutils.utils.UtilConstants.MAP;
import static org.onosproject.yangutils.utils.UtilConstants.NEW;
import static org.onosproject.yangutils.utils.UtilConstants.NEW_LINE;
import static org.onosproject.yangutils.utils.UtilConstants.OBJECT_STRING;
import static org.onosproject.yangutils.utils.UtilConstants.PERIOD;
import static org.onosproject.yangutils.utils.UtilConstants.PRIVATE;
import static org.onosproject.yangutils.utils.UtilConstants.PROTECTED;
import static org.onosproject.yangutils.utils.UtilConstants.PUBLIC;
import static org.onosproject.yangutils.utils.UtilConstants.QUESTION_MARK;
import static org.onosproject.yangutils.utils.UtilConstants.QUEUE;
import static org.onosproject.yangutils.utils.UtilConstants.QUOTES;
import static org.onosproject.yangutils.utils.UtilConstants.SCHEMA_NAME;
import static org.onosproject.yangutils.utils.UtilConstants.SEMI_COLON;
import static org.onosproject.yangutils.utils.UtilConstants.SET;
import static org.onosproject.yangutils.utils.UtilConstants.SHORT_MAX_RANGE_ATTR;
import static org.onosproject.yangutils.utils.UtilConstants.SHORT_MIN_RANGE_ATTR;
import static org.onosproject.yangutils.utils.UtilConstants.SPACE;
import static org.onosproject.yangutils.utils.UtilConstants.STRING_DATA_TYPE;
import static org.onosproject.yangutils.utils.UtilConstants.TYPE;
import static org.onosproject.yangutils.utils.UtilConstants.UINT8_MAX_RANGE_ATTR;
import static org.onosproject.yangutils.utils.UtilConstants.UINT8_MIN_RANGE_ATTR;
import static org.onosproject.yangutils.utils.UtilConstants.UINT_MAX_RANGE_ATTR;
import static org.onosproject.yangutils.utils.UtilConstants.UINT_MIN_RANGE_ATTR;
import static org.onosproject.yangutils.utils.UtilConstants.ULONG_MAX_RANGE_ATTR;
import static org.onosproject.yangutils.utils.UtilConstants.ULONG_MIN_RANGE_ATTR;
import static org.onosproject.yangutils.utils.UtilConstants.YANG_AUGMENTED_INFO_LOWER_CASE;
import static org.onosproject.yangutils.utils.io.impl.JavaDocGen.JavaDocType.ENUM_ATTRIBUTE;
import static org.onosproject.yangutils.utils.io.impl.JavaDocGen.enumJavaDocForInnerClass;
import static org.onosproject.yangutils.utils.io.impl.JavaDocGen.getJavaDoc;
import static org.onosproject.yangutils.utils.io.impl.YangIoUtils.trimAtLast;

/**
 * Represents utility class to generate the java snippet.
 */
public final class JavaCodeSnippetGen {

    // No instantiation.
    private JavaCodeSnippetGen() {
    }

    /**
     * Returns the java file header comment.
     *
     * @return the java file header comment
     */
    public static String getFileHeaderComment() {

        /*
         * TODO return the file header.
         */
        return null;
    }

    /**
     * Returns the textual java code information corresponding to the import
     * list.
     *
     * @param importInfo import info
     * @return the textual java code information corresponding to the import
     * list
     */
    static String getImportText(JavaQualifiedTypeInfoTranslator importInfo) {
        return getImportString(importInfo.getPkgInfo(), importInfo
                .getClassInfo());
    }

    /**
     * Returns the textual java code for attribute definition in class.
     *
     * @param typePkg    Package of the attribute type
     * @param attrType   java attribute type
     * @param attrName   name of the attribute
     * @param isList     is list attribute
     * @param accessType attribute access type
     * @param annotation compiler annotation
     * @return the textual java code for attribute definition in class
     */
    public static String getJavaAttributeDefinition(String typePkg,
                                                    String attrType,
                                                    String attrName,
                                                    boolean isList,
                                                    String accessType,
                                                    YangCompilerAnnotation annotation) {
        StringBuilder attrDef = new StringBuilder(FOUR_SPACE_INDENTATION);
        attrDef.append(accessType).append(SPACE);

        if (!isList) {
            if (typePkg != null) {
                attrDef.append(typePkg).append(PERIOD);
            }

            attrDef.append(attrType).append(SPACE)
                    .append(attrName).append(SEMI_COLON)
                    .append(NEW_LINE);
        } else {
            // Add starting definition.
            addAttrStartDef(annotation, attrDef);

            if (typePkg != null) {
                attrDef.append(typePkg).append(PERIOD);
            }

            attrDef.append(attrType);

            // Add ending definition.
            addAttrEndDef(annotation, attrDef, attrName);
        }
        return attrDef.toString();
    }

    /**
     * Adds starting attribute definition.
     *
     * @param annotation compiler annotation
     * @param attrDef    JAVA attribute definition
     */
    private static void addAttrStartDef(YangCompilerAnnotation annotation,
                                        StringBuilder attrDef) {
        if (annotation != null &&
                annotation.getYangAppDataStructure() != null) {
            switch (annotation.getYangAppDataStructure().getDataStructure()) {
                case QUEUE: {
                    attrDef.append(QUEUE)
                            .append(DIAMOND_OPEN_BRACKET);
                    break;
                }
                case SET: {
                    attrDef.append(SET)
                            .append(DIAMOND_OPEN_BRACKET);
                    break;
                }
                default: {
                    attrDef.append(LIST)
                            .append(DIAMOND_OPEN_BRACKET);
                }
            }
        } else {
            attrDef.append(LIST).append(DIAMOND_OPEN_BRACKET);
        }
    }

    /**
     * Adds ending attribute definition.
     *
     * @param annotation compiler annotation
     * @param attrDef    JAVA attribute definition
     * @param attrName   name of attribute
     */
    private static void addAttrEndDef(YangCompilerAnnotation annotation,
                                      StringBuilder attrDef, String attrName) {
        if (annotation != null &&
                annotation.getYangAppDataStructure() != null) {
            attrDef.append(DIAMOND_CLOSE_BRACKET).append(SPACE)
                    .append(attrName).append(SEMI_COLON)
                    .append(NEW_LINE);
        } else {
            attrDef.append(DIAMOND_CLOSE_BRACKET).append(SPACE).append(attrName)
                    .append(SPACE).append(EQUAL).append(SPACE).append(NEW)
                    .append(SPACE).append(ARRAY_LIST).append(SEMI_COLON)
                    .append(NEW_LINE);
        }
    }

    /**
     * Returns string for enum's attribute.
     *
     * @param name  name of attribute
     * @param value value of the enum
     * @return string for enum's attribute
     */
    public static String generateEnumAttributeString(String name, int value) {
        String enumName = getEnumJavaAttribute(name);
        return enumJavaDocForInnerClass(name) + EIGHT_SPACE_INDENTATION +
                enumName.toUpperCase() + brackets(OPEN_CLOSE_BRACKET_WITH_VALUE,
                                                  value + EMPTY_STRING, null) +
                COMMA + NEW_LINE;
    }

    /**
     * Returns string for enum's attribute for enum class.
     *
     * @param name   name of attribute
     * @param value  value of the enum
     * @return string for enum's attribute
     */
    public static String generateEnumAttributeStringWithSchemaName(
            String name, int value) {
        String enumName = getEnumJavaAttribute(name);
        String str = value + COMMA + SPACE + QUOTES + name + QUOTES;
        return getJavaDoc(ENUM_ATTRIBUTE, name, false, null) +
                FOUR_SPACE_INDENTATION + enumName.toUpperCase() + brackets(
                OPEN_CLOSE_BRACKET_WITH_VALUE, str, null) + COMMA + NEW_LINE;
    }

    /**
     * Returns sorted import list.
     *
     * @param imports import list
     * @return sorted import list
     */
    public static List<String> sortImports(List<String> imports) {
        sort(imports);
        return imports;
    }

    /**
     * Returns event enum start.
     *
     * @return event enum start
     */
    static String getEventEnumTypeStart() {
        return NEW_LINE + FOUR_SPACE_INDENTATION +
                getDefaultDefinition(ENUM, TYPE, PUBLIC);
    }

    /**
     * Adds listener's imports.
     *
     * @param curNode   currentYangNode.
     * @param imports   import list
     * @param operation add or remove
     */
    public static void addListenersImport(YangNode curNode,
                                          List<String> imports,
                                          Operation operation) {
        String thisImport;
        TempJavaServiceFragmentFiles tempFiles =
                ((JavaCodeGeneratorInfo) curNode).getTempJavaCodeFragmentFiles()
                        .getServiceTempFiles();
        thisImport = tempFiles.getJavaImportData().getListenerServiceImport();
        performOperationOnImports(imports, thisImport, operation);
    }

    /**
     * Performs given operations on import list.
     *
     * @param imports   list of imports
     * @param curImport current import
     * @param operation ADD or REMOVE
     * @return import list
     */
    private static List<String> performOperationOnImports(List<String> imports,
                                                          String curImport,
                                                          Operation operation) {
        switch (operation) {
            case ADD:
                imports.add(curImport);
                break;
            case REMOVE:
                imports.remove(curImport);
                break;
            default:
                throw new TranslatorException("Invalid operation type");
        }
        sortImports(imports);
        return imports;
    }

    /**
     * Returns integer attribute for enum's class to get the values.
     *
     * @param className enum's class name
     * @return enum's attribute
     */
    static String getEnumsValueAttribute(String className) {
        return getJavaAttributeDefinition(null, INT, className,
                                          false, PRIVATE, null) +
                getJavaAttributeDefinition(null, STRING_DATA_TYPE, SCHEMA_NAME,
                                           false, PRIVATE, null);
    }

    /**
     * Returns attribute for augmentation.
     *
     * @return attribute for augmentation
     */
    static String addAugmentationAttribute() {
        String[] array = {NEW_LINE, SEMI_COLON};
        return trimAtLast(getJavaAttributeDefinition(
                null, getAugmentMapTypeString(),
                YANG_AUGMENTED_INFO_LOWER_CASE + MAP, false, PROTECTED,
                null), array) + SPACE + EQUAL + SPACE + NEW + SPACE +
                HASH_MAP + brackets(OPEN_CLOSE_DIAMOND, null, null) + brackets(
                OPEN_CLOSE_BRACKET, null, null) + signatureClose();
    }

    /**
     * Returns augment map return type.
     *
     * @return augment map return type
     */
    static String getAugmentMapTypeString() {
        return MAP + DIAMOND_OPEN_BRACKET + CLASS_STRING + DIAMOND_OPEN_BRACKET +
                QUESTION_MARK + DIAMOND_CLOSE_BRACKET + COMMA + SPACE +
                OBJECT_STRING + DIAMOND_CLOSE_BRACKET;
    }

    /**
     * Adds attribute for int ranges.
     *
     * @param modifier modifier for attribute
     * @param addFirst true if int need to be added fist.
     * @return attribute for int ranges
     */
    static String addStaticAttributeIntRange(String modifier,
                                             boolean addFirst) {
        if (addFirst) {
            return getTypeConflictAttributeStrings(modifier,
                                                   INT_MIN_RANGE_ATTR,
                                                   INT_MAX_RANGE_ATTR);
        }
        return getTypeConflictAttributeStrings(modifier,
                                               UINT_MIN_RANGE_ATTR,
                                               UINT_MAX_RANGE_ATTR);
    }

    /**
     * Adds attribute for long ranges.
     *
     * @param modifier modifier for attribute
     * @param addFirst if need to be added first
     * @return attribute for long ranges
     */
    static String addStaticAttributeLongRange(String modifier,
                                              boolean addFirst) {
        if (addFirst) {
            return getTypeConflictAttributeStrings(modifier,
                                                   LONG_MIN_RANGE_ATTR,
                                                   LONG_MAX_RANGE_ATTR);
        }
        return getTypeConflictAttributeStrings(modifier,
                                               ULONG_MIN_RANGE_ATTR,
                                               ULONG_MAX_RANGE_ATTR);
    }

    /**
     * Adds attribute for long ranges.
     *
     * @param modifier modifier for attribute
     * @param addFirst if need to be added first
     * @return attribute for long ranges
     */
    static String addStaticAttributeShortRange(String modifier,
                                               boolean addFirst) {
        if (addFirst) {
            return getTypeConflictAttributeStrings(modifier,
                                                   SHORT_MIN_RANGE_ATTR,
                                                   SHORT_MAX_RANGE_ATTR);
        }
        return getTypeConflictAttributeStrings(modifier,
                                               UINT8_MIN_RANGE_ATTR,
                                               UINT8_MAX_RANGE_ATTR);
    }

    /**
     * Returns attribute for conflicting type in union.
     *
     * @param modifier modifier
     * @param attr1    attribute one
     * @param att2     attribute two
     * @return attribute for conflicting type in union
     */
    private static String getTypeConflictAttributeStrings(String modifier,
                                                          String attr1,
                                                          String att2) {
        return NEW_LINE + FOUR_SPACE_INDENTATION + modifier + SPACE +
                attr1 + FOUR_SPACE_INDENTATION + modifier +
                SPACE + att2;
    }

    /**
     * Returns operation type enum.
     *
     * @return operation type enum
     */
    static String getOperationTypeEnum() {
        return "    /**\n" +
                "     * Specify the node specific operation in protocols " +
                "like NETCONF.\n" +
                "     * Applicable in protocol edit operation, not applicable" +
                " in query operation\n" +
                "     */\n" +
                "    public enum OnosYangNodeOperationType {\n" +
                "        MERGE,\n" +
                "        REPLACE,\n" +
                "        CREATE,\n" +
                "        DELETE,\n" +
                "        REMOVE,\n" +
                "        NONE\n" +
                "    }\n";
    }

    /**
     * Returns operation type enum, leaf value set attribute and select leaf
     * attribute.
     *
     * @return operation attributes for value and select leaf flags
     */
    static String getOperationAttributes() {
        return "    /**\n" +
                "     * Identify the leafs whose value are explicitly set\n" +
                "     * Applicable in protocol edit and query operation\n" +
                "     */\n" +
                "    private BitSet valueLeafFlags = new BitSet();\n" +
                "\n" +
                "    /**\n" +
                "     * Identify the leafs to be selected, in a query operation\n" +
                "     */\n" +
                "    private BitSet selectLeafFlags = new BitSet();\n";
    }

    /**
     * Returns operation type enum, leaf value set attribute and select leaf
     * attribute for constructor.
     *
     * @return operation attributes for constructor
     */
    static String getOperationAttributeForConstructor() {
        return "        this.valueLeafFlags = builderObject.getValueLeafFlags();\n" +
                "        this.selectLeafFlags = builderObject.getSelectLeafFlags();\n";
    }

    /**
     * Returns attribute in constructor for yang augmented info map.
     *
     * @return augment info map
     */
    static String getYangAugmentedMapObjectForConstruct() {
        return "        this.yangAugmentedInfoMap = builderObject" +
                ".yangAugmentedInfoMap();\n";
    }
}
