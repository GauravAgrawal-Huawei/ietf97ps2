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

import java.io.IOException;

import org.onosproject.yangutils.datamodel.YangAugment;
import org.onosproject.yangutils.datamodel.YangCase;
import org.onosproject.yangutils.datamodel.YangChoice;
import org.onosproject.yangutils.datamodel.YangLeafRef;
import org.onosproject.yangutils.datamodel.YangLeavesHolder;
import org.onosproject.yangutils.datamodel.YangNode;
import org.onosproject.yangutils.datamodel.YangType;
import org.onosproject.yangutils.translator.tojava.JavaAttributeInfo;
import org.onosproject.yangutils.translator.tojava.JavaFileInfoContainer;
import org.onosproject.yangutils.translator.tojava.JavaFileInfoTranslator;
import org.onosproject.yangutils.translator.tojava
        .TempJavaCodeFragmentFilesContainer;
import org.onosproject.yangutils.utils.io.YangPluginConfig;

import static org.onosproject.yangutils.datamodel.utils.builtindatatype
        .YangDataTypes.LEAFREF;
import static org.onosproject.yangutils.translator.tojava
        .GeneratedTempFileType.FILTER_CONTENT_MATCH_FOR_LEAF_LIST_MASK;
import static org.onosproject.yangutils.translator.tojava
        .GeneratedTempFileType.FILTER_CONTENT_MATCH_FOR_LEAF_MASK;
import static org.onosproject.yangutils.translator.tojava
        .GeneratedTempFileType.FILTER_CONTENT_MATCH_FOR_NODES_MASK;
import static org.onosproject.yangutils.translator.tojava.utils
        .JavaFileGeneratorUtils.getDataFromTempFileHandle;
import static org.onosproject.yangutils.utils.UtilConstants.ADD_STRING;
import static org.onosproject.yangutils.utils.UtilConstants.AND_OPERATION;
import static org.onosproject.yangutils.utils.UtilConstants.APP_INSTANCE;
import static org.onosproject.yangutils.utils.UtilConstants.BOOLEAN_DATA_TYPE;
import static org.onosproject.yangutils.utils.UtilConstants.BREAK;
import static org.onosproject.yangutils.utils.UtilConstants.BUILD;
import static org.onosproject.yangutils.utils.UtilConstants.BUILDER;
import static org.onosproject.yangutils.utils.UtilConstants.CATCH;
import static org.onosproject.yangutils.utils.UtilConstants.CLOSE_CURLY_BRACKET;
import static org.onosproject.yangutils.utils.UtilConstants.CLOSE_PARENTHESIS;
import static org.onosproject.yangutils.utils.UtilConstants.COLAN;
import static org.onosproject.yangutils.utils.UtilConstants.COMMA;
import static org.onosproject.yangutils.utils.UtilConstants.CONTINUE;
import static org.onosproject.yangutils.utils.UtilConstants
        .EIGHT_SPACE_INDENTATION;
import static org.onosproject.yangutils.utils.UtilConstants.ELSE;
import static org.onosproject.yangutils.utils.UtilConstants
        .EMPTY_PARAMETER_FUNCTION_CALL;
import static org.onosproject.yangutils.utils.UtilConstants.EQUAL;
import static org.onosproject.yangutils.utils.UtilConstants.EQUALS_STRING;
import static org.onosproject.yangutils.utils.UtilConstants.EXCEPTION_VAR;
import static org.onosproject.yangutils.utils.UtilConstants.FALSE;
import static org.onosproject.yangutils.utils.UtilConstants.FOR;
import static org.onosproject.yangutils.utils.UtilConstants
        .FOUR_SPACE_INDENTATION;
import static org.onosproject.yangutils.utils.UtilConstants.GET_CLASS;
import static org.onosproject.yangutils.utils.UtilConstants.GET_LEAF_INDEX;
import static org.onosproject.yangutils.utils.UtilConstants.GET_METHOD;
import static org.onosproject.yangutils.utils.UtilConstants.GET_METHOD_PREFIX;
import static org.onosproject.yangutils.utils.UtilConstants
        .GET_SELECT_LEAF_FLAGS;
import static org.onosproject.yangutils.utils.UtilConstants
        .GET_VALUE_LEAF_FLAGS;
import static org.onosproject.yangutils.utils.UtilConstants.IF;
import static org.onosproject.yangutils.utils.UtilConstants
        .ILLEGAL_ACCESS_EXCEPTION;
import static org.onosproject.yangutils.utils.UtilConstants.INSTANCE;
import static org.onosproject.yangutils.utils.UtilConstants
        .INVOCATION_TARGET_EXCEPTION;
import static org.onosproject.yangutils.utils.UtilConstants.INVOKE;
import static org.onosproject.yangutils.utils.UtilConstants
        .IS_ANY_SELECT_OR_CONTAINMENT_NODE_FLAG;
import static org.onosproject.yangutils.utils.UtilConstants.IS_EMPTY;
import static org.onosproject.yangutils.utils.UtilConstants
        .IS_SELECT_ALL_SCHEMA_CHILD_FLAG;
import static org.onosproject.yangutils.utils.UtilConstants.LEAF_IDENTIFIER;
import static org.onosproject.yangutils.utils.UtilConstants.MAP;
import static org.onosproject.yangutils.utils.UtilConstants.NEW;
import static org.onosproject.yangutils.utils.UtilConstants.NEW_LINE;
import static org.onosproject.yangutils.utils.UtilConstants.NOT;
import static org.onosproject.yangutils.utils.UtilConstants
        .NO_SUCH_METHOD_EXCEPTION;
import static org.onosproject.yangutils.utils.UtilConstants.NULL;
import static org.onosproject.yangutils.utils.UtilConstants.OBJECT;
import static org.onosproject.yangutils.utils.UtilConstants.OBJECT_STRING;
import static org.onosproject.yangutils.utils.UtilConstants.OPEN_CURLY_BRACKET;
import static org.onosproject.yangutils.utils.UtilConstants.OPEN_PARENTHESIS;
import static org.onosproject.yangutils.utils.UtilConstants.OR_OPERATION;
import static org.onosproject.yangutils.utils.UtilConstants.PERIOD;
import static org.onosproject.yangutils.utils.UtilConstants.PRIVATE;
import static org.onosproject.yangutils.utils.UtilConstants
        .PROCESS_SUBTREE_FILTERING;
import static org.onosproject.yangutils.utils.UtilConstants.PUBLIC;
import static org.onosproject.yangutils.utils.UtilConstants.QUOTES;
import static org.onosproject.yangutils.utils.UtilConstants.RETURN;
import static org.onosproject.yangutils.utils.UtilConstants.SEMI_COLAN;
import static org.onosproject.yangutils.utils.UtilConstants
        .SIXTEEN_SPACE_INDENTATION;
import static org.onosproject.yangutils.utils.UtilConstants.SPACE;
import static org.onosproject.yangutils.utils.UtilConstants
        .SUBTREE_FILTERING_RESULT_BUILDER;
import static org.onosproject.yangutils.utils.UtilConstants
        .THIRTY_TWO_SPACE_INDENTATION;
import static org.onosproject.yangutils.utils.UtilConstants.THIS;
import static org.onosproject.yangutils.utils.UtilConstants.TO;
import static org.onosproject.yangutils.utils.UtilConstants.TRUE;
import static org.onosproject.yangutils.utils.UtilConstants.TRY;
import static org.onosproject.yangutils.utils.UtilConstants
        .TWELVE_SPACE_INDENTATION;
import static org.onosproject.yangutils.utils.UtilConstants
        .TWENTY_EIGHT_SPACE_INDENTATION;
import static org.onosproject.yangutils.utils.UtilConstants
        .TWENTY_FOUR_SPACE_INDENTATION;
import static org.onosproject.yangutils.utils.UtilConstants
        .TWENTY_SPACE_INDENTATION;
import static org.onosproject.yangutils.utils.UtilConstants.VALUE;
import static org.onosproject.yangutils.utils.UtilConstants.YANG_AUGMENTED_INFO;
import static org.onosproject.yangutils.utils.UtilConstants
        .YANG_AUGMENTED_OP_PARAM_INFO;
import static org.onosproject.yangutils.utils.io.impl.YangIoUtils.getCamelCase;
import static org.onosproject.yangutils.utils.io.impl.YangIoUtils
        .getCapitalCase;
import static org.onosproject.yangutils.utils.io.impl.YangIoUtils.getSmallCase;

/**
 * Represents generator for subtree filtering methods of generated files
 * based on the file type.
 */
public final class SubtreeFilteringMethodsGenerator {

    /**
     * private to make it a util.
     */
    private SubtreeFilteringMethodsGenerator() {
    }

    /**
     * Returns is filter content match for leaf.
     *
     * @param javaAttributeInfo java attribute
     * @param type              data type
     * @return is filter content match for leaf
     */
    public static String getSubtreeFilteringForLeaf(
            JavaAttributeInfo javaAttributeInfo, YangType<?> type) {
        String attrQualifiedType;
        String attributeName = javaAttributeInfo.getAttributeName();
        attrQualifiedType = getIfFilterContentMatchMethodImpl(attributeName,
                                                              type);
        return EIGHT_SPACE_INDENTATION + IF + SPACE + OPEN_PARENTHESIS
                + GET_VALUE_LEAF_FLAGS + EMPTY_PARAMETER_FUNCTION_CALL +
                PERIOD + GET_METHOD_PREFIX + OPEN_PARENTHESIS +
                LEAF_IDENTIFIER + PERIOD + attributeName.toUpperCase() +
                PERIOD + GET_LEAF_INDEX + CLOSE_PARENTHESIS +
                CLOSE_PARENTHESIS + SPACE + OPEN_CURLY_BRACKET + NEW_LINE +
                TWELVE_SPACE_INDENTATION + IF + SPACE + OPEN_PARENTHESIS +
                attrQualifiedType + CLOSE_PARENTHESIS + SPACE +
                OPEN_CURLY_BRACKET + NEW_LINE + SIXTEEN_SPACE_INDENTATION +
                RETURN + SPACE + FALSE + SEMI_COLAN + NEW_LINE +
                TWELVE_SPACE_INDENTATION + CLOSE_CURLY_BRACKET + SPACE +
                ELSE + SPACE + OPEN_CURLY_BRACKET + NEW_LINE +
                SIXTEEN_SPACE_INDENTATION +
                SUBTREE_FILTERING_RESULT_BUILDER + PERIOD + attributeName +
                OPEN_PARENTHESIS + APP_INSTANCE + PERIOD + attributeName +
                OPEN_PARENTHESIS + CLOSE_PARENTHESIS +
                CLOSE_PARENTHESIS + SEMI_COLAN + NEW_LINE +
                TWELVE_SPACE_INDENTATION + CLOSE_CURLY_BRACKET + NEW_LINE +
                EIGHT_SPACE_INDENTATION + CLOSE_CURLY_BRACKET + SPACE + ELSE +
                SPACE + IF + SPACE + OPEN_PARENTHESIS +
                GET_SELECT_LEAF_FLAGS + EMPTY_PARAMETER_FUNCTION_CALL +
                PERIOD + GET_METHOD_PREFIX + OPEN_PARENTHESIS +
                LEAF_IDENTIFIER + PERIOD + attributeName.toUpperCase() +
                PERIOD + GET_LEAF_INDEX + CLOSE_PARENTHESIS + SPACE +
                OR_OPERATION + SPACE + IS_SELECT_ALL_SCHEMA_CHILD_FLAG +
                CLOSE_PARENTHESIS + SPACE + OPEN_CURLY_BRACKET + NEW_LINE +
                TWELVE_SPACE_INDENTATION +
                IS_ANY_SELECT_OR_CONTAINMENT_NODE_FLAG + SPACE + EQUAL +
                SPACE + TRUE + SEMI_COLAN + NEW_LINE +
                TWELVE_SPACE_INDENTATION + SUBTREE_FILTERING_RESULT_BUILDER +
                PERIOD + attributeName + OPEN_PARENTHESIS + APP_INSTANCE +
                PERIOD + attributeName + EMPTY_PARAMETER_FUNCTION_CALL +
                CLOSE_PARENTHESIS + SEMI_COLAN + NEW_LINE +
                EIGHT_SPACE_INDENTATION + CLOSE_CURLY_BRACKET + NEW_LINE;
    }

    private static String getAttrTypeForFilterContentMatchWhenPrimitiveDataType(
            String attributeName) {
        return/* TODO: Need to check if we can expose the value leaf flag in
         interface.
                NOT + APP_INSTANCE + PERIOD + GET_VALUE_LEAF_FLAGS +
                OPEN_PARENTHESIS + CLOSE_PARENTHESIS +
                GET_METHOD_PREFIX + OPEN_PARENTHESIS + LEAF_IDENTIFIER +
                PERIOD + attributeName.toUpperCase() + PERIOD
                + GET_LEAF_INDEX + OR_OPERATION +*/
                APP_INSTANCE + PERIOD + attributeName + OPEN_PARENTHESIS +
                        CLOSE_PARENTHESIS + SPACE + NOT + EQUAL + SPACE +
                        attributeName + OPEN_PARENTHESIS +
                        CLOSE_PARENTHESIS;
    }

    private static String
    getAttrTypeForFilterContentMatchWhenNonPrimitiveDataTypes(
            String attributeName) {
        return APP_INSTANCE + PERIOD + attributeName + OPEN_PARENTHESIS +
                CLOSE_PARENTHESIS + SPACE + EQUAL + EQUAL + SPACE + NULL +
                SPACE + OR_OPERATION + SPACE + NOT + OPEN_PARENTHESIS +
                attributeName + OPEN_PARENTHESIS + CLOSE_PARENTHESIS +
                PERIOD + EQUALS_STRING + OPEN_PARENTHESIS + APP_INSTANCE +
                PERIOD + attributeName + OPEN_PARENTHESIS +
                CLOSE_PARENTHESIS + CLOSE_PARENTHESIS + CLOSE_PARENTHESIS;
    }

    private static String getIfFilterContentMatchMethodImpl(
            String attributeName,
            YangType dataType) {
        String attrQualifiedType;

        if (MethodsGenerator.isPrimitiveDataType(dataType.getDataType())) {
            attrQualifiedType =
                    getAttrTypeForFilterContentMatchWhenPrimitiveDataType(
                            attributeName);
        } else if (dataType.getDataType() == LEAFREF) {
            YangType type = ((YangLeafRef) dataType.getDataTypeExtendedInfo())
                    .getEffectiveDataType();
            if (MethodsGenerator.isPrimitiveDataType(type.getDataType())) {
                attrQualifiedType =
                        getAttrTypeForFilterContentMatchWhenPrimitiveDataType(
                                attributeName);
            } else {
                attrQualifiedType =
                        getAttrTypeForFilterContentMatchWhenNonPrimitiveDataTypes(
                                attributeName);
            }
        } else {
            attrQualifiedType =
                    getAttrTypeForFilterContentMatchWhenNonPrimitiveDataTypes(
                            attributeName);
        }

        return attrQualifiedType;
    }

    /**
     * Returns is filter content match for leaf.
     *
     * @param javaAttributeInfo java attribute
     * @return is filter content match for leaf
     */
    public static String getSubtreeFilteringForLeafList(
            JavaAttributeInfo javaAttributeInfo) {
        return getSubtreeFilteringForList(javaAttributeInfo, true);
    }

    /**
     * Returns is filter content match for child node.
     *
     * @param curNode      current node
     * @param pluginConfig plugin configurations
     * @param path         path of temp file
     * @return is filter content match for child node
     */
    static String getProcessChildNodeSubtreeFiltering(YangNode curNode,
                                                      YangPluginConfig
                                                              pluginConfig,
                                                      String path)
            throws IOException {
        JavaFileInfoTranslator javaFileInfo =
                ((JavaFileInfoContainer) curNode).getJavaFileInfo();
        JavaFileInfoTranslator parentInfo;

        String instance = APP_INSTANCE;
        String name = getCapitalCase(javaFileInfo.getJavaName());
        String builderNamePrefix = getCapitalCase(javaFileInfo.getJavaName());
        if (curNode instanceof YangCase) {
            instance = INSTANCE;
            YangNode parent = curNode.getParent();
            if (parent instanceof YangChoice) {
                parentInfo = ((JavaFileInfoContainer) parent).getJavaFileInfo();
                name = getCapitalCase(parentInfo.getJavaName());
            } else if (parent instanceof YangAugment) {
                parentInfo = ((JavaFileInfoContainer) ((YangAugment) parent)
                        .getAugmentedNode()).getJavaFileInfo();
                if (parentInfo != null) {
                    name = getCapitalCase(parentInfo.getJavaName());
                } else {
                    name = getCapitalCase(getCamelCase(
                            ((YangAugment) parent).getAugmentedNode().getName(),
                            pluginConfig.getConflictResolver()));
                }
            }
        }

        String processSubtreeFilteringMethod =
                FOUR_SPACE_INDENTATION + PRIVATE + SPACE + BOOLEAN_DATA_TYPE +
                        SPACE + "processChildNodesSubTreeFiltering" +
                        OPEN_PARENTHESIS + name + SPACE + instance + COMMA +
                        SPACE + builderNamePrefix + BUILDER + SPACE +
                        "subTreeFilteringResultBuilder" + COMMA + NEW_LINE +
                        TWELVE_SPACE_INDENTATION + "Boolean " +
                        "isAnySelectOrContainmentNode, " + "boolean " +
                        "isSelectAllSchemaChild" + CLOSE_PARENTHESIS +
                        SPACE + OPEN_CURLY_BRACKET + NEW_LINE;

        if (curNode instanceof YangCase) {
            String caseName = getCapitalCase(javaFileInfo.getJavaName());
            processSubtreeFilteringMethod =
                    processSubtreeFilteringMethod + EIGHT_SPACE_INDENTATION +
                            caseName + SPACE + APP_INSTANCE + SPACE +
                            EQUAL + SPACE + OPEN_PARENTHESIS + caseName +
                            CLOSE_PARENTHESIS + SPACE + instance +
                            SEMI_COLAN + NEW_LINE;
        }

        processSubtreeFilteringMethod +=
                getDataFromTempFileHandle(FILTER_CONTENT_MATCH_FOR_NODES_MASK,
                                          ((TempJavaCodeFragmentFilesContainer) curNode)
                                                  .getTempJavaCodeFragmentFiles()
                                                  .getBeanTempFiles(), path);

        processSubtreeFilteringMethod +=
                EIGHT_SPACE_INDENTATION + RETURN + SPACE + TRUE + SEMI_COLAN +
                        NEW_LINE + FOUR_SPACE_INDENTATION +
                        CLOSE_CURLY_BRACKET + NEW_LINE + NEW_LINE;

        return processSubtreeFilteringMethod;
    }

    /**
     * Returns is filter content match for leaf list.
     *
     * @param curNode      current node
     * @param pluginConfig plugin configurations
     * @param path         path of temp file
     * @return is filter content match for leaf list
     */
    static String getProcessLeafListSubtreeFiltering(YangNode curNode,
                                                     YangPluginConfig
                                                             pluginConfig,
                                                     String path)
            throws IOException {
        JavaFileInfoTranslator javaFileInfo =
                ((JavaFileInfoContainer) curNode).getJavaFileInfo();
        JavaFileInfoTranslator parentInfo;

        String instance = APP_INSTANCE;
        String name = getCapitalCase(javaFileInfo.getJavaName());
        String builderNamePrefix = getCapitalCase(javaFileInfo.getJavaName());
        if (curNode instanceof YangCase) {
            instance = INSTANCE;
            YangNode parent = curNode.getParent();
            if (parent instanceof YangChoice) {
                parentInfo = ((JavaFileInfoContainer) parent).getJavaFileInfo();
                name = getCapitalCase(parentInfo.getJavaName());
            } else if (parent instanceof YangAugment) {
                parentInfo = ((JavaFileInfoContainer) ((YangAugment) parent)
                        .getAugmentedNode()).getJavaFileInfo();
                if (parentInfo != null) {
                    name = getCapitalCase(parentInfo.getJavaName());
                } else {
                    name = getCapitalCase(getCamelCase(
                            ((YangAugment) parent).getAugmentedNode().getName(),
                            pluginConfig.getConflictResolver()));
                }
            }
        }

        String processSubtreeFilteringMethod =
                FOUR_SPACE_INDENTATION + PRIVATE + SPACE + BOOLEAN_DATA_TYPE +
                        SPACE + "processLeafListSubTreeFiltering" +
                        OPEN_PARENTHESIS + name + SPACE + instance + COMMA +
                        SPACE + builderNamePrefix + BUILDER + SPACE +
                        "subTreeFilteringResultBuilder" + COMMA + NEW_LINE +
                        TWELVE_SPACE_INDENTATION + "Boolean " +
                        "isAnySelectOrContainmentNode, " + "boolean " +
                        "isSelectAllSchemaChild" + CLOSE_PARENTHESIS +
                        SPACE + OPEN_CURLY_BRACKET + NEW_LINE;

        if (curNode instanceof YangCase) {
            String caseName = getCapitalCase(javaFileInfo.getJavaName());
            processSubtreeFilteringMethod =
                    processSubtreeFilteringMethod + EIGHT_SPACE_INDENTATION +
                            caseName + SPACE + APP_INSTANCE + SPACE +
                            EQUAL + SPACE + OPEN_PARENTHESIS + caseName +
                            CLOSE_PARENTHESIS + SPACE + instance +
                            SEMI_COLAN + NEW_LINE;
        }

        processSubtreeFilteringMethod += getDataFromTempFileHandle(
                FILTER_CONTENT_MATCH_FOR_LEAF_LIST_MASK,
                ((TempJavaCodeFragmentFilesContainer) curNode)
                        .getTempJavaCodeFragmentFiles()
                        .getBeanTempFiles(), path);

        processSubtreeFilteringMethod +=
                EIGHT_SPACE_INDENTATION + RETURN + SPACE + TRUE + SEMI_COLAN +
                        NEW_LINE + FOUR_SPACE_INDENTATION +
                        CLOSE_CURLY_BRACKET + NEW_LINE + NEW_LINE;

        return processSubtreeFilteringMethod;
    }

    /**
     * Returns is filter content match for leaf.
     *
     * @param curNode      current node
     * @param pluginConfig plugin configurations
     * @param path         path of temp file
     * @return is filter content match for leaf
     */
    static String getProcessLeafSubtreeFiltering(YangNode curNode,
                                                 YangPluginConfig pluginConfig,
                                                 String path)
            throws IOException {
        JavaFileInfoTranslator javaFileInfo =
                ((JavaFileInfoContainer) curNode).getJavaFileInfo();
        JavaFileInfoTranslator parentInfo;

        String instance = APP_INSTANCE;
        String name = getCapitalCase(javaFileInfo.getJavaName());
        String builderNamePrefix = getCapitalCase(javaFileInfo.getJavaName());
        if (curNode instanceof YangCase) {
            instance = INSTANCE;
            YangNode parent = curNode.getParent();
            if (parent instanceof YangChoice) {
                parentInfo = ((JavaFileInfoContainer) parent).getJavaFileInfo();
                name = getCapitalCase(parentInfo.getJavaName());
            } else if (parent instanceof YangAugment) {
                parentInfo = ((JavaFileInfoContainer) ((YangAugment) parent)
                        .getAugmentedNode()).getJavaFileInfo();
                if (parentInfo != null) {
                    name = getCapitalCase(parentInfo.getJavaName());
                } else {
                    name = getCapitalCase(getCamelCase(
                            ((YangAugment) parent).getAugmentedNode().getName(),
                            pluginConfig.getConflictResolver()));
                }
            }
        }

        String processSubtreeFilteringMethod =
                FOUR_SPACE_INDENTATION + PRIVATE + SPACE + BOOLEAN_DATA_TYPE +
                        SPACE + "processLeafSubtreeFiltering" +
                        OPEN_PARENTHESIS + name + SPACE + instance + COMMA +
                        SPACE + builderNamePrefix + BUILDER + SPACE +
                        "subTreeFilteringResultBuilder" + COMMA + NEW_LINE
                        + TWELVE_SPACE_INDENTATION + "Boolean " +
                        "isAnySelectOrContainmentNode, " + "boolean " +
                        "isSelectAllSchemaChild" + CLOSE_PARENTHESIS +
                        SPACE + OPEN_CURLY_BRACKET + NEW_LINE;

        if (curNode instanceof YangCase) {
            String caseName = getCapitalCase(javaFileInfo.getJavaName());
            processSubtreeFilteringMethod =
                    processSubtreeFilteringMethod + EIGHT_SPACE_INDENTATION +
                            caseName + SPACE + APP_INSTANCE + SPACE +
                            EQUAL + SPACE + OPEN_PARENTHESIS + caseName +
                            CLOSE_PARENTHESIS + SPACE + instance +
                            SEMI_COLAN + NEW_LINE;
        }

        processSubtreeFilteringMethod +=
                getDataFromTempFileHandle(FILTER_CONTENT_MATCH_FOR_LEAF_MASK,
                                          ((TempJavaCodeFragmentFilesContainer) curNode)
                                                  .getTempJavaCodeFragmentFiles()
                                                  .getBeanTempFiles(), path);

        processSubtreeFilteringMethod +=
                EIGHT_SPACE_INDENTATION + RETURN + SPACE + TRUE + SEMI_COLAN +
                        NEW_LINE + FOUR_SPACE_INDENTATION +
                        CLOSE_CURLY_BRACKET + NEW_LINE + NEW_LINE;

        return processSubtreeFilteringMethod;
    }

    /**
     * Returns is filter content match for leaf.
     *
     * @param curNode      current node
     * @param pluginConfig plugin configurations
     * @return is filter content match for leaf
     */
    static String getProcessSubtreeFilteringStart(YangNode curNode,
                                                  YangPluginConfig
                                                          pluginConfig) {
        JavaFileInfoTranslator javaFileInfo =
                ((JavaFileInfoContainer) curNode).getJavaFileInfo();
        JavaFileInfoTranslator parentInfo;

        String instance = APP_INSTANCE;
        String name = getCapitalCase(javaFileInfo.getJavaName());
        String builderNamePrefix = getCapitalCase(javaFileInfo.getJavaName());
        if (curNode instanceof YangCase) {
            instance = INSTANCE;
            YangNode parent = curNode.getParent();
            if (parent instanceof YangChoice) {
                parentInfo = ((JavaFileInfoContainer) parent).getJavaFileInfo();
                name = getCapitalCase(parentInfo.getJavaName());
            } else if (parent instanceof YangAugment) {
                parentInfo = ((JavaFileInfoContainer) ((YangAugment) parent)
                        .getAugmentedNode()).getJavaFileInfo();
                if (parentInfo != null) {
                    name = getCapitalCase(parentInfo.getJavaName());
                } else {
                    name = getCapitalCase(getCamelCase(
                            ((YangAugment) parent).getAugmentedNode().getName(),
                            pluginConfig.getConflictResolver()));
                }
            }
        }
        String processSubtreeFilteringMethod =
                MethodsGenerator.getOverRideString() + FOUR_SPACE_INDENTATION +
                        PUBLIC + SPACE + name + SPACE +
                        PROCESS_SUBTREE_FILTERING + OPEN_PARENTHESIS + name +
                        SPACE + instance + COMMA + SPACE + BOOLEAN_DATA_TYPE +
                        SPACE + "isSelectAllSchemaChild" +
                        CLOSE_PARENTHESIS + SPACE + OPEN_CURLY_BRACKET +
                        NEW_LINE + EIGHT_SPACE_INDENTATION +
                        builderNamePrefix + BUILDER + SPACE +
                        SUBTREE_FILTERING_RESULT_BUILDER + SPACE + EQUAL +
                        SPACE + NEW + SPACE + builderNamePrefix + BUILDER +
                        OPEN_PARENTHESIS + CLOSE_PARENTHESIS + SEMI_COLAN +
                        NEW_LINE + EIGHT_SPACE_INDENTATION + "Boolean" + SPACE +
                        IS_ANY_SELECT_OR_CONTAINMENT_NODE_FLAG + SPACE +
                        EQUAL + SPACE + FALSE + SEMI_COLAN + NEW_LINE;

        if (curNode instanceof YangCase) {
            String caseName = getCapitalCase(javaFileInfo.getJavaName());
            processSubtreeFilteringMethod =
                    processSubtreeFilteringMethod + EIGHT_SPACE_INDENTATION +
                            caseName + SPACE + APP_INSTANCE + SPACE +
                            EQUAL + SPACE + OPEN_PARENTHESIS + caseName +
                            CLOSE_PARENTHESIS + SPACE + instance +
                            SEMI_COLAN + NEW_LINE;
        }

        return processSubtreeFilteringMethod;
    }

    /**
     * Get the body for process subtree filtering.
     *
     * @param curNode node for which the code is being generated
     * @return body of subtree filtering
     */
    static String getProcessSubtreeFunctionBody(YangNode curNode) {

        String method = "";

        if (curNode instanceof YangLeavesHolder) {
            if (((YangLeavesHolder) curNode).getListOfLeaf() != null
                    &&
                    !((YangLeavesHolder) curNode).getListOfLeaf().isEmpty()) {
                method += MethodsGenerator
                        .getIfConditionBegin(EIGHT_SPACE_INDENTATION, NOT +
                                "processLeafSubtreeFiltering(appInstance, " +
                                "subTreeFilteringResultBuilder," + NEW_LINE +
                                TWELVE_SPACE_INDENTATION +
                                "isAnySelectOrContainmentNode, " +
                                "isSelectAllSchemaChild)");

                method += TWELVE_SPACE_INDENTATION + RETURN + SPACE + NULL +
                        SEMI_COLAN + NEW_LINE;

                method += MethodsGenerator.getBlockEnd(EIGHT_SPACE_INDENTATION);
            }
        }

        if (curNode instanceof YangLeavesHolder) {
            if (((YangLeavesHolder) curNode).getListOfLeafList() != null
                    &&
                    !((YangLeavesHolder) curNode).getListOfLeafList()
                            .isEmpty()) {
                method += MethodsGenerator
                        .getIfConditionBegin(EIGHT_SPACE_INDENTATION, NOT +
                                "processLeafListSubTreeFiltering(appInstance," +
                                " subTreeFilteringResultBuilder," + NEW_LINE
                                + TWELVE_SPACE_INDENTATION +
                                "isAnySelectOrContainmentNode, " +
                                "isSelectAllSchemaChild)");

                method += TWELVE_SPACE_INDENTATION + RETURN + SPACE + NULL +
                        SEMI_COLAN + NEW_LINE;

                method += MethodsGenerator.getBlockEnd(EIGHT_SPACE_INDENTATION);
            }
        }

        if (curNode.getChild() != null) {

            method += MethodsGenerator
                    .getIfConditionBegin(EIGHT_SPACE_INDENTATION, NOT +
                            "processChildNodesSubTreeFiltering(appInstance, " +
                            "subTreeFilteringResultBuilder," + NEW_LINE +
                            TWELVE_SPACE_INDENTATION +
                            "isAnySelectOrContainmentNode, " +
                            "isSelectAllSchemaChild)");

            method += TWELVE_SPACE_INDENTATION + RETURN + SPACE + NULL +
                    SEMI_COLAN + NEW_LINE;

            method += MethodsGenerator.getBlockEnd(EIGHT_SPACE_INDENTATION);
        }

        return method;
    }

    /**
     * Returns is filter content match for node.
     *
     * @param attr attribute info
     * @return is filter content match for node
     */
    public static String getSubtreeFilteringForNode(JavaAttributeInfo attr) {
        boolean isList = attr.isListAttr();
        if (isList) {
            return getSubtreeFilteringForList(attr, false);
        } else {
            return getSubtreeFilteringForChildNode(attr);
        }
    }

    /**
     * Returns is filter content match close.
     *
     * @return is filter content match close
     */
    static String getProcessSubTreeFilteringEnd() {
        String method = MethodsGenerator
                .getIfConditionBegin(EIGHT_SPACE_INDENTATION,
                                     NOT + IS_SELECT_ALL_SCHEMA_CHILD_FLAG +
                                             SPACE + AND_OPERATION + SPACE +
                                             NOT +
                                             IS_ANY_SELECT_OR_CONTAINMENT_NODE_FLAG);

        method += TWELVE_SPACE_INDENTATION + RETURN + SPACE +
                PROCESS_SUBTREE_FILTERING + OPEN_PARENTHESIS + APP_INSTANCE +
                COMMA + SPACE + TRUE + CLOSE_PARENTHESIS + SEMI_COLAN +
                NEW_LINE;

        method += MethodsGenerator.getBlockEnd(EIGHT_SPACE_INDENTATION);

        method += EIGHT_SPACE_INDENTATION + RETURN + SPACE +
                SUBTREE_FILTERING_RESULT_BUILDER + PERIOD + BUILD +
                EMPTY_PARAMETER_FUNCTION_CALL + SEMI_COLAN + NEW_LINE +
                FOUR_SPACE_INDENTATION + CLOSE_CURLY_BRACKET + NEW_LINE;

        return method;
    }

    /**
     * Returns filter content match for child nodes.
     *
     * @param javaAttributeInfo attribute to be added
     * @return filter content match for child nodes
     */
    private static String getSubtreeFilteringForChildNode(
            JavaAttributeInfo javaAttributeInfo) {
        String name = javaAttributeInfo.getAttributeName();
        name = getSmallCase(name);
        String type = javaAttributeInfo.getImportInfo().getClassInfo();
        if (javaAttributeInfo.isQualifiedName()) {
            type = javaAttributeInfo.getImportInfo().getPkgInfo() + PERIOD +
                    type;
        }

        String method = MethodsGenerator
                .getIfConditionBegin(EIGHT_SPACE_INDENTATION, name + "()  != " +
                        "null");

        method += TWELVE_SPACE_INDENTATION +
                IS_ANY_SELECT_OR_CONTAINMENT_NODE_FLAG + SPACE + EQUAL + SPACE +
                TRUE + SEMI_COLAN + NEW_LINE;

        method += TWELVE_SPACE_INDENTATION + IF + SPACE + OPEN_PARENTHESIS +
                APP_INSTANCE + PERIOD + name + OPEN_PARENTHESIS +
                CLOSE_PARENTHESIS + SPACE + NOT
                + EQUAL + SPACE + NULL + CLOSE_PARENTHESIS + SPACE +
                OPEN_CURLY_BRACKET + NEW_LINE;

        method += SIXTEEN_SPACE_INDENTATION + type + SPACE + "result = " +
                name + PERIOD + PROCESS_SUBTREE_FILTERING + OPEN_PARENTHESIS +
                APP_INSTANCE + PERIOD + name + EMPTY_PARAMETER_FUNCTION_CALL
                + COMMA + SPACE + FALSE + CLOSE_PARENTHESIS + SEMI_COLAN +
                NEW_LINE;

        method += SIXTEEN_SPACE_INDENTATION + "if (result != null) {" +
                NEW_LINE;

        method += TWENTY_SPACE_INDENTATION + SUBTREE_FILTERING_RESULT_BUILDER +
                PERIOD + name + OPEN_PARENTHESIS + "result" +
                CLOSE_PARENTHESIS + SEMI_COLAN + NEW_LINE +
                SIXTEEN_SPACE_INDENTATION + CLOSE_CURLY_BRACKET + NEW_LINE;

        //if app instance is not null
        method += MethodsGenerator.getBlockEnd(TWELVE_SPACE_INDENTATION);

        //if query instance is not null
        method += MethodsGenerator.getBlockEnd(EIGHT_SPACE_INDENTATION);

        return method;
    }

    /**
     * Returns filter content match for list types.
     *
     * @param javaAttributeInfo attribute information
     * @param isLeafList        if for leaf list
     * @return filter content match for list types
     */
    private static String getSubtreeFilteringForList(
            JavaAttributeInfo javaAttributeInfo, boolean isLeafList) {
        String capitalCaseName =
                getCapitalCase(javaAttributeInfo.getAttributeName());
        String name = javaAttributeInfo.getAttributeName();
        String type = javaAttributeInfo.getImportInfo().getClassInfo();
        if (javaAttributeInfo.isQualifiedName()) {
            type = javaAttributeInfo.getImportInfo().getPkgInfo() + PERIOD +
                    type;
        }

        /*
         * If select all schema child
         */
        String method = MethodsGenerator
                .getIfConditionBegin(EIGHT_SPACE_INDENTATION,
                                     IS_SELECT_ALL_SCHEMA_CHILD_FLAG);

        method = method + MethodsGenerator
                .getCollectionIteratorForLoopBegin(TWELVE_SPACE_INDENTATION,
                                                   type + SPACE + name,
                                                   APP_INSTANCE + PERIOD +
                                                           name +
                                                           EMPTY_PARAMETER_FUNCTION_CALL);

        method = method + SIXTEEN_SPACE_INDENTATION +
                SUBTREE_FILTERING_RESULT_BUILDER + PERIOD + ADD_STRING +
                getCapitalCase(TO) + capitalCaseName + OPEN_PARENTHESIS +
                name + CLOSE_PARENTHESIS + SEMI_COLAN + NEW_LINE;

        method += MethodsGenerator.getBlockEnd(
                TWELVE_SPACE_INDENTATION); // Close collection Iteration loop

        //If need to explicitly participate in query
        method += MethodsGenerator
                .getElseIfConditionBegin(EIGHT_SPACE_INDENTATION,
                                         name + EMPTY_PARAMETER_FUNCTION_CALL +
                                                 SPACE + NOT + EQUAL +
                                                 SPACE + NULL);

        if (!isLeafList) {
            method += TWELVE_SPACE_INDENTATION +
                    IS_ANY_SELECT_OR_CONTAINMENT_NODE_FLAG + SPACE + EQUAL +
                    SPACE + TRUE + SEMI_COLAN + NEW_LINE;
        }

        //If there is any parameter in the query condition
        method += MethodsGenerator
                .getIfConditionBegin(TWELVE_SPACE_INDENTATION, NOT + name +
                        EMPTY_PARAMETER_FUNCTION_CALL + PERIOD + IS_EMPTY);

        if (isLeafList) {
            /*
             * If there is no app instance to perform content match
             */
            method += MethodsGenerator
                    .getIfConditionBegin(SIXTEEN_SPACE_INDENTATION,
                                         APP_INSTANCE + PERIOD + name +
                                                 EMPTY_PARAMETER_FUNCTION_CALL +
                                                 SPACE + EQUAL + EQUAL + SPACE +
                                                 NULL + SPACE + OR_OPERATION
                                                 + SPACE + APP_INSTANCE +
                                                 PERIOD + name +
                                                 EMPTY_PARAMETER_FUNCTION_CALL +
                                                 PERIOD + IS_EMPTY);

            method += TWENTY_SPACE_INDENTATION + RETURN + SPACE + FALSE +
                    SEMI_COLAN + NEW_LINE;

            method += MethodsGenerator.getBlockEnd(SIXTEEN_SPACE_INDENTATION);

            // for instance iterator
            method += MethodsGenerator.getCollectionIteratorForLoopBegin(
                    SIXTEEN_SPACE_INDENTATION, type + SPACE + name,
                    name + EMPTY_PARAMETER_FUNCTION_CALL);

            method += TWENTY_SPACE_INDENTATION + BOOLEAN_DATA_TYPE + SPACE +
                    "flag" + SPACE + EQUAL + SPACE + FALSE + SEMI_COLAN +
                    NEW_LINE;

            // for app instance iterator
            method += MethodsGenerator
                    .getCollectionIteratorForLoopBegin(TWENTY_SPACE_INDENTATION,
                                                       type + SPACE + name +
                                                               "2",
                                                       APP_INSTANCE + PERIOD +
                                                               name +
                                                               EMPTY_PARAMETER_FUNCTION_CALL);

            //the content match leaf list attribute value matches
            method += MethodsGenerator
                    .getIfConditionBegin(TWENTY_FOUR_SPACE_INDENTATION,
                                         name + PERIOD + EQUALS_STRING
                                                 + OPEN_PARENTHESIS + name +
                                                 "2" + CLOSE_PARENTHESIS);

            method += TWENTY_EIGHT_SPACE_INDENTATION + "flag" + SPACE + EQUAL +
                    SPACE + TRUE + SEMI_COLAN + NEW_LINE;

            method += TWENTY_EIGHT_SPACE_INDENTATION +
                    SUBTREE_FILTERING_RESULT_BUILDER + PERIOD + ADD_STRING +
                    getCapitalCase(TO) + capitalCaseName + OPEN_PARENTHESIS +
                    name + "2" + CLOSE_PARENTHESIS +
                    SEMI_COLAN + NEW_LINE + TWENTY_EIGHT_SPACE_INDENTATION +
                    BREAK + SEMI_COLAN + NEW_LINE;

            //the content match leaf list attribute value matches
            method +=
                    MethodsGenerator.getBlockEnd(TWENTY_FOUR_SPACE_INDENTATION);

            // for app instance iterator
            method += MethodsGenerator.getBlockEnd(TWENTY_SPACE_INDENTATION);

            //if the content match failed
            method += MethodsGenerator
                    .getIfConditionBegin(TWENTY_SPACE_INDENTATION, "!flag");

            method += TWENTY_FOUR_SPACE_INDENTATION + RETURN + SPACE + FALSE +
                    SEMI_COLAN + NEW_LINE;

            method += MethodsGenerator
                    .getBlockEnd(TWENTY_SPACE_INDENTATION); // if flag == false

            method += MethodsGenerator.getBlockEnd(
                    SIXTEEN_SPACE_INDENTATION); // for instance iterator


        } else {

            /*if there is any app instance entry*/
            method += MethodsGenerator
                    .getIfConditionBegin(SIXTEEN_SPACE_INDENTATION,
                                         APP_INSTANCE + PERIOD + name +
                                                 EMPTY_PARAMETER_FUNCTION_CALL +
                                                 SPACE + NOT + EQUAL + SPACE +
                                                 NULL + SPACE + AND_OPERATION +
                                                 SPACE + NOT + APP_INSTANCE +
                                                 PERIOD + name +
                                                 EMPTY_PARAMETER_FUNCTION_CALL +
                                                 PERIOD + IS_EMPTY);

            /*
             * loop all the query condition instance(s)
             */
            method += MethodsGenerator
                    .getCollectionIteratorForLoopBegin(TWENTY_SPACE_INDENTATION,
                                                       type + SPACE + name,
                                                       name +
                                                               EMPTY_PARAMETER_FUNCTION_CALL);

            //loop all the app instance(s)
            method += MethodsGenerator.getCollectionIteratorForLoopBegin(
                    TWENTY_FOUR_SPACE_INDENTATION, type + SPACE + name + "2",
                    APP_INSTANCE + PERIOD + name +
                            EMPTY_PARAMETER_FUNCTION_CALL);

            method += TWENTY_EIGHT_SPACE_INDENTATION + type + SPACE +
                    "result = " + name + PERIOD +
                    PROCESS_SUBTREE_FILTERING + OPEN_PARENTHESIS + name + "2" +
                    COMMA + SPACE + FALSE + CLOSE_PARENTHESIS + SEMI_COLAN +
                    NEW_LINE;

            method += TWENTY_EIGHT_SPACE_INDENTATION + "if (result != null) {" +
                    NEW_LINE;

            method += THIRTY_TWO_SPACE_INDENTATION +
                    SUBTREE_FILTERING_RESULT_BUILDER + PERIOD + ADD_STRING +
                    getCapitalCase(TO) + capitalCaseName + OPEN_PARENTHESIS +
                    "result" + CLOSE_PARENTHESIS +
                    SEMI_COLAN + NEW_LINE + TWENTY_EIGHT_SPACE_INDENTATION +
                    CLOSE_CURLY_BRACKET + NEW_LINE;

            //loop all the app instance(s)
            method +=
                    MethodsGenerator.getBlockEnd(TWENTY_FOUR_SPACE_INDENTATION);

            //loop all the query condition instance(s)
            method += MethodsGenerator.getBlockEnd(TWENTY_SPACE_INDENTATION);

            //if there is any app instance entry
            method += MethodsGenerator.getBlockEnd(SIXTEEN_SPACE_INDENTATION);
        }

        method += TWELVE_SPACE_INDENTATION + "} else {" + NEW_LINE;

        if (isLeafList) {
            method += SIXTEEN_SPACE_INDENTATION +
                    IS_ANY_SELECT_OR_CONTAINMENT_NODE_FLAG + SPACE +
                    EQUAL + SPACE + TRUE + SEMI_COLAN + NEW_LINE;
        }

        method += MethodsGenerator
                .getIfConditionBegin(SIXTEEN_SPACE_INDENTATION,
                                     APP_INSTANCE + PERIOD + name
                                             + EMPTY_PARAMETER_FUNCTION_CALL +
                                             SPACE + NOT + EQUAL + SPACE +
                                             NULL + SPACE + AND_OPERATION +
                                             SPACE + NOT + APP_INSTANCE +
                                             PERIOD + name +
                                             EMPTY_PARAMETER_FUNCTION_CALL +
                                             PERIOD + IS_EMPTY);

        method = method + MethodsGenerator
                .getCollectionIteratorForLoopBegin(TWENTY_SPACE_INDENTATION,
                                                   type + SPACE + name,
                                                   APP_INSTANCE + PERIOD +
                                                           name +
                                                           EMPTY_PARAMETER_FUNCTION_CALL);

        method = method + TWENTY_FOUR_SPACE_INDENTATION +
                SUBTREE_FILTERING_RESULT_BUILDER + PERIOD + ADD_STRING
                + getCapitalCase(TO) + capitalCaseName + OPEN_PARENTHESIS +
                name + CLOSE_PARENTHESIS + SEMI_COLAN + NEW_LINE;

        method += MethodsGenerator.getBlockEnd(
                TWENTY_SPACE_INDENTATION); // Close collection Iteration loop

        method += MethodsGenerator
                .getBlockEnd(SIXTEEN_SPACE_INDENTATION); // close  if condition

        method += MethodsGenerator
                .getBlockEnd(TWELVE_SPACE_INDENTATION); // close  else condition

        method += MethodsGenerator.getBlockEnd(
                EIGHT_SPACE_INDENTATION); // close  else if condition

        return method;

    }

    //Returns method string for op params augmented syntax
    static String getAugmentableSubTreeFiltering() {
        return EIGHT_SPACE_INDENTATION + FOR + SPACE + OPEN_PARENTHESIS +
                OBJECT_STRING + SPACE + getSmallCase(YANG_AUGMENTED_INFO) +
                SPACE + COLAN + SPACE + THIS + PERIOD +
                getSmallCase(YANG_AUGMENTED_INFO) + MAP +
                OPEN_PARENTHESIS + CLOSE_PARENTHESIS + PERIOD
                + VALUE + "s" + OPEN_PARENTHESIS + CLOSE_PARENTHESIS +
                CLOSE_PARENTHESIS + SPACE + OPEN_CURLY_BRACKET +
                NEW_LINE + TWELVE_SPACE_INDENTATION + OBJECT_STRING + SPACE +
                getSmallCase(YANG_AUGMENTED_OP_PARAM_INFO) + SPACE + EQUAL +
                SPACE + APP_INSTANCE + PERIOD +
                getSmallCase(YANG_AUGMENTED_INFO) + OPEN_PARENTHESIS +
                getSmallCase(YANG_AUGMENTED_INFO) + PERIOD +
                GET_CLASS + CLOSE_PARENTHESIS + SEMI_COLAN + NEW_LINE +
                TWELVE_SPACE_INDENTATION + OBJECT + SPACE +
                PROCESS_SUBTREE_FILTERING + SEMI_COLAN
                + NEW_LINE + TWELVE_SPACE_INDENTATION + TRY + SPACE +
                OPEN_CURLY_BRACKET + NEW_LINE +
                SIXTEEN_SPACE_INDENTATION +
                "Class<?>[] interfaces = " + getSmallCase(YANG_AUGMENTED_INFO) +
                ".getClass().getInterfaces();" +
                NEW_LINE + SIXTEEN_SPACE_INDENTATION +
                PROCESS_SUBTREE_FILTERING + SPACE + EQUAL + SPACE +
                getSmallCase(YANG_AUGMENTED_INFO) + PERIOD + GET_CLASS +
                NEW_LINE + TWENTY_SPACE_INDENTATION + PERIOD +
                GET_METHOD + OPEN_PARENTHESIS + QUOTES +
                PROCESS_SUBTREE_FILTERING + QUOTES + COMMA + SPACE +
                "interfaces[0]" + CLOSE_PARENTHESIS + PERIOD + INVOKE +
                OPEN_PARENTHESIS + getSmallCase(YANG_AUGMENTED_INFO) +
                COMMA + NEW_LINE + TWENTY_FOUR_SPACE_INDENTATION +
                getSmallCase(YANG_AUGMENTED_OP_PARAM_INFO) +
                CLOSE_PARENTHESIS + SEMI_COLAN + NEW_LINE +
                SIXTEEN_SPACE_INDENTATION + IF + SPACE + OPEN_PARENTHESIS +
                PROCESS_SUBTREE_FILTERING + SPACE + NOT + EQUAL + SPACE +
                NULL + CLOSE_PARENTHESIS + SPACE + OPEN_CURLY_BRACKET +
                NEW_LINE + TWENTY_SPACE_INDENTATION +
                SUBTREE_FILTERING_RESULT_BUILDER + PERIOD + "add" +
                YANG_AUGMENTED_INFO + OPEN_PARENTHESIS +
                PROCESS_SUBTREE_FILTERING + COMMA + SPACE +
                PROCESS_SUBTREE_FILTERING + PERIOD + GET_CLASS +
                CLOSE_PARENTHESIS + SEMI_COLAN + NEW_LINE +
                SIXTEEN_SPACE_INDENTATION + CLOSE_CURLY_BRACKET +
                NEW_LINE + TWELVE_SPACE_INDENTATION + CLOSE_CURLY_BRACKET +
                SPACE + CATCH + SPACE + OPEN_PARENTHESIS +
                NO_SUCH_METHOD_EXCEPTION + " | " +
                INVOCATION_TARGET_EXCEPTION + " | " + ILLEGAL_ACCESS_EXCEPTION +
                SPACE + EXCEPTION_VAR + CLOSE_PARENTHESIS + SPACE +
                OPEN_CURLY_BRACKET + NEW_LINE + SIXTEEN_SPACE_INDENTATION +
                CONTINUE + SEMI_COLAN + NEW_LINE +
                TWELVE_SPACE_INDENTATION + CLOSE_CURLY_BRACKET + NEW_LINE +
                EIGHT_SPACE_INDENTATION + CLOSE_CURLY_BRACKET + NEW_LINE;
    }
}
