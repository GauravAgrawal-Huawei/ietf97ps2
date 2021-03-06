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

package org.onosproject.yangutils.translator.tojava.javamodel;

import org.onosproject.yangutils.datamodel.YangDerivedInfo;
import org.onosproject.yangutils.datamodel.YangEnumeration;
import org.onosproject.yangutils.datamodel.YangIdentity;
import org.onosproject.yangutils.datamodel.YangIdentityRef;
import org.onosproject.yangutils.datamodel.YangLeafRef;
import org.onosproject.yangutils.datamodel.YangNode;
import org.onosproject.yangutils.datamodel.YangType;
import org.onosproject.yangutils.datamodel.YangUnion;
import org.onosproject.yangutils.datamodel.utils.builtindatatype.YangDataTypes;
import org.onosproject.yangutils.translator.exception.TranslatorException;
import org.onosproject.yangutils.translator.tojava.JavaCodeGeneratorInfo;
import org.onosproject.yangutils.translator.tojava.JavaFileInfoContainer;
import org.onosproject.yangutils.translator.tojava.JavaFileInfoTranslator;
import org.onosproject.yangutils.utils.io.YangToJavaNamingConflictUtil;

import java.util.Stack;

import static org.onosproject.yangutils.translator.tojava.YangJavaModelUtils.getCurNodePackage;
import static org.onosproject.yangutils.translator.tojava.utils.JavaIdentifierSyntax.getRootPackage;
import static org.onosproject.yangutils.utils.UtilConstants.BIG_DECIMAL;
import static org.onosproject.yangutils.utils.UtilConstants.BIG_INTEGER;
import static org.onosproject.yangutils.utils.UtilConstants.BIT_SET;
import static org.onosproject.yangutils.utils.UtilConstants.BOOLEAN_DATA_TYPE;
import static org.onosproject.yangutils.utils.UtilConstants.BOOLEAN_WRAPPER;
import static org.onosproject.yangutils.utils.UtilConstants.BYTE;
import static org.onosproject.yangutils.utils.UtilConstants.BYTE_WRAPPER;
import static org.onosproject.yangutils.utils.UtilConstants.COLLECTION_IMPORTS;
import static org.onosproject.yangutils.utils.UtilConstants.INT;
import static org.onosproject.yangutils.utils.UtilConstants.INTEGER_WRAPPER;
import static org.onosproject.yangutils.utils.UtilConstants.JAVA_LANG;
import static org.onosproject.yangutils.utils.UtilConstants.JAVA_MATH;
import static org.onosproject.yangutils.utils.UtilConstants.LONG;
import static org.onosproject.yangutils.utils.UtilConstants.LONG_WRAPPER;
import static org.onosproject.yangutils.utils.UtilConstants.OBJECT_STRING;
import static org.onosproject.yangutils.utils.UtilConstants.PERIOD;
import static org.onosproject.yangutils.utils.UtilConstants.SHORT;
import static org.onosproject.yangutils.utils.UtilConstants.SHORT_WRAPPER;
import static org.onosproject.yangutils.utils.UtilConstants.SQUARE_BRACKETS;
import static org.onosproject.yangutils.utils.UtilConstants.STRING_DATA_TYPE;
import static org.onosproject.yangutils.utils.io.impl.YangIoUtils.getCamelCase;
import static org.onosproject.yangutils.utils.io.impl.YangIoUtils.getCapitalCase;
import static org.onosproject.yangutils.utils.io.impl.YangIoUtils.getPackageDirPathFromJavaJPackage;

/**
 * Represents java data types info corresponding to YANG type.
 */
public final class AttributesJavaDataType {

    /**
     * Creates an instance of attribute java data type.
     */
    private AttributesJavaDataType() {
    }

    /**
     * Returns java type.
     *
     * @param yangType YANG type
     * @return java type
     */
    public static String getJavaDataType(YangType<?> yangType) {

        YangDataTypes type = yangType.getDataType();

        switch (type) {
            case INT8:
                return BYTE;
            case INT16:
                return SHORT;
            case INT32:
                return INT;
            case INT64:
                return LONG;
            case UINT8:
                return SHORT;
            case UINT16:
                return INT;
            case UINT32:
                return LONG;
            case UINT64:
                return BIG_INTEGER;
            case BITS:
                return BIT_SET;
            case BINARY:
                return BYTE + SQUARE_BRACKETS;
            case DECIMAL64:
                return BIG_DECIMAL;
            case STRING:
                return STRING_DATA_TYPE;
            case BOOLEAN:
                return BOOLEAN_DATA_TYPE;
            case INSTANCE_IDENTIFIER:
                return STRING_DATA_TYPE;
            case LEAFREF:
                YangType refType = getReferredTypeFromLeafref(yangType);
                if (refType == null) {
                    return OBJECT_STRING;
                }
                return getJavaDataType(getReferredTypeFromLeafref(yangType));
            default:
                throw new TranslatorException("given data type is not supported. " +
                                                      yangType.getDataTypeName() + " in " +
                                                      yangType.getLineNumber() + " at " +
                                                      yangType.getCharPosition()
                                                      + " in " + yangType.getFileName());
        }
    }

    /**
     * Returns java import class.
     *
     * @param yangType     YANG type
     * @param isListAttr   if the attribute need to be a list
     * @param pluginConfig plugin configurations
     * @return java import class
     */
    public static String getJavaImportClass(YangType<?> yangType, boolean isListAttr,
                                            YangToJavaNamingConflictUtil pluginConfig) {

        YangDataTypes type = yangType.getDataType();

        if (isListAttr) {
            switch (type) {
                case INT8:
                    return BYTE_WRAPPER;
                case INT16:
                    return SHORT_WRAPPER;
                case INT32:
                    return INTEGER_WRAPPER;
                case INT64:
                    return LONG_WRAPPER;
                case UINT8:
                    return SHORT_WRAPPER;
                case UINT16:
                    return INTEGER_WRAPPER;
                case UINT32:
                    return LONG_WRAPPER;
                case UINT64:
                    return BIG_INTEGER;
                case DECIMAL64:
                    return BIG_DECIMAL;
                case STRING:
                    return STRING_DATA_TYPE;
                case BOOLEAN:
                    return BOOLEAN_WRAPPER;
                case ENUMERATION:
                    return getCapitalCase(
                            getCamelCase(((YangJavaEnumerationTranslator) yangType.getDataTypeExtendedInfo()).getName(),
                                         pluginConfig));
                case BITS:
                    return BIT_SET;
                case BINARY:
                    return BYTE + SQUARE_BRACKETS;
                case LEAFREF:
                    return getLeafRefImpClass(yangType, pluginConfig, true);
                case IDENTITYREF:
                    return getIdentityRefImpClass(yangType, pluginConfig);
                case EMPTY:
                    return BOOLEAN_WRAPPER;
                case UNION:
                    return getCapitalCase(getCamelCase(((YangJavaUnionTranslator) yangType
                            .getDataTypeExtendedInfo()).getName(), pluginConfig));
                case INSTANCE_IDENTIFIER:
                    return STRING_DATA_TYPE;
                case DERIVED:
                    return getCapitalCase(
                            getCamelCase(yangType.getDataTypeName(), pluginConfig));
                default:
                    throw new TranslatorException("given data type is not supported ." +
                                                          yangType.getDataTypeName() + " in " +
                                                          yangType.getLineNumber() + " at " +
                                                          yangType.getCharPosition()
                                                          + " in " + yangType.getFileName());
            }
        } else {
            switch (type) {
                case UINT64:
                    return BIG_INTEGER;
                case STRING:
                    return STRING_DATA_TYPE;
                case ENUMERATION:
                    return getCapitalCase(
                            getCamelCase(((YangJavaEnumerationTranslator) yangType.getDataTypeExtendedInfo()).getName(),
                                         pluginConfig));
                case BITS:
                    return BIT_SET;
                case DECIMAL64:
                    return BIG_DECIMAL;
                case LEAFREF:
                    return getLeafRefImpClass(yangType, pluginConfig, false);
                case IDENTITYREF:
                    return getIdentityRefImpClass(yangType, pluginConfig);
                case EMPTY:
                    return BOOLEAN_DATA_TYPE;
                case UNION:
                    return getCapitalCase(getCamelCase(((YangJavaUnionTranslator) yangType
                            .getDataTypeExtendedInfo()).getName(), pluginConfig));
                case INSTANCE_IDENTIFIER:
                    return STRING_DATA_TYPE;
                case DERIVED:
                    return getCapitalCase(
                            getCamelCase(yangType.getDataTypeName(), pluginConfig));
                default:
                    return null;
            }
        }
    }

    /**
     * Returns java import package.
     *
     * @param yangType         YANG type
     * @param isListAttr       if the attribute is of list type
     * @param conflictResolver object of YANG to java naming conflict util
     * @return java import package
     */
    public static String getJavaImportPackage(YangType<?> yangType, boolean isListAttr,
                                              YangToJavaNamingConflictUtil conflictResolver) {

        YangDataTypes type = yangType.getDataType();

        if (isListAttr) {
            switch (type) {
                case INT8:
                case INT16:
                case INT32:
                case INT64:
                case UINT8:
                case UINT16:
                case UINT32:
                case BINARY:
                case STRING:
                case BOOLEAN:
                case EMPTY:
                    return JAVA_LANG;
                case UINT64:
                case DECIMAL64:
                    return JAVA_MATH;
                case ENUMERATION:
                    return getEnumsPackage(yangType, conflictResolver);
                case BITS:
                    return COLLECTION_IMPORTS;
                case LEAFREF:
                    return getLeafRefImpPkg(yangType, conflictResolver, true);
                case IDENTITYREF:
                    return getIdentityRefPackage(yangType, conflictResolver);
                case UNION:
                    return getUnionPackage(yangType, conflictResolver);
                case INSTANCE_IDENTIFIER:
                    return JAVA_LANG;
                case DERIVED:
                    return getTypeDefsPackage(yangType, conflictResolver);
                default:
                    throw new TranslatorException("given data type is not supported. " +
                                                          yangType.getDataTypeName() + " in " +
                                                          yangType.getLineNumber() + " at " +
                                                          yangType.getCharPosition()
                                                          + " in " + yangType.getFileName());
            }
        } else {
            switch (type) {
                case UINT64:
                case DECIMAL64:
                    return JAVA_MATH;
                case EMPTY:
                case STRING:
                    return JAVA_LANG;
                case ENUMERATION:
                    return getEnumsPackage(yangType, conflictResolver);
                case BITS:
                    return COLLECTION_IMPORTS;
                case LEAFREF:
                    return getLeafRefImpPkg(yangType, conflictResolver, false);
                case IDENTITYREF:
                    return getIdentityRefPackage(yangType, conflictResolver);
                case UNION:
                    return getUnionPackage(yangType, conflictResolver);
                case INSTANCE_IDENTIFIER:
                    return JAVA_LANG;
                case DERIVED:
                    return getTypeDefsPackage(yangType, conflictResolver);
                default:
                    return null;
            }
        }
    }

    /**
     * Returns java package for typedef node.
     *
     * @param type             YANG type
     * @param conflictResolver object of YANG to java naming conflict util
     * @return java package for typedef node
     */
    private static String getTypeDefsPackage(YangType<?> type, YangToJavaNamingConflictUtil conflictResolver) {
        Object var = type.getDataTypeExtendedInfo();
        if (!(var instanceof YangDerivedInfo)) {
            throw new TranslatorException("type should have been derived. " +
                                                  type.getDataTypeName() + " in " +
                                                  type.getLineNumber() + " at " +
                                                  type.getCharPosition()
                                                  + " in " + type.getFileName());
        }

        if (!(((YangDerivedInfo<?>) var).getReferredTypeDef() != null)) {
            throw new TranslatorException("derived info is not an instance of typedef. " +
                                                  type.getDataTypeName() + " in " +
                                                  type.getLineNumber() + " at " +
                                                  type.getCharPosition()
                                                  + " in " + type.getFileName());
        }

        YangJavaTypeDefTranslator typedef = (YangJavaTypeDefTranslator) ((YangDerivedInfo<?>) var).getReferredTypeDef();
        return getTypePackage(typedef, conflictResolver);
    }

    /**
     * Returns java package for union node.
     *
     * @param type             YANG type
     * @param conflictResolver object of YANG to java naming conflict util
     * @return java package for union node
     */
    private static String getUnionPackage(YangType<?> type, YangToJavaNamingConflictUtil conflictResolver) {

        if (!(type.getDataTypeExtendedInfo() instanceof YangUnion)) {
            throw new TranslatorException("type should have been union. " +
                                                  type.getDataTypeName() + " in " +
                                                  type.getLineNumber() + " at " +
                                                  type.getCharPosition()
                                                  + " in " + type.getFileName());
        }

        YangJavaUnionTranslator union = (YangJavaUnionTranslator) type.getDataTypeExtendedInfo();
        return getTypePackage(union, conflictResolver);
    }

    /**
     * Returns YANG enumeration's java package.
     *
     * @param type             YANG type
     * @param conflictResolver object of YANG to java naming conflict util
     * @return YANG enumeration's java package
     */
    private static String getEnumsPackage(YangType<?> type, YangToJavaNamingConflictUtil conflictResolver) {

        if (!(type.getDataTypeExtendedInfo() instanceof YangEnumeration)) {
            throw new TranslatorException("type should have been enumeration. " +
                                                  type.getDataTypeName() + " in " +
                                                  type.getLineNumber() + " at " +
                                                  type.getCharPosition()
                                                  + " in " + type.getFileName());
        }
        YangJavaEnumerationTranslator enumeration = (YangJavaEnumerationTranslator) type.getDataTypeExtendedInfo();
        return getTypePackage(enumeration, conflictResolver);
    }

    /**
     * Returns YANG identity's java package.
     *
     * @param type             YANG type
     * @param conflictResolver object of YANG to java naming conflict util
     * @return YANG identity's java package
     */
    private static String getIdentityRefPackage(YangType<?> type, YangToJavaNamingConflictUtil conflictResolver) {

        if (!(type.getDataTypeExtendedInfo() instanceof YangIdentityRef)) {
            throw new TranslatorException("type should have been identityref. " +
                                                  type.getDataTypeName() + " in " +
                                                  type.getLineNumber() + " at " +
                                                  type.getCharPosition()
                                                  + " in " + type.getFileName());
        }
        YangIdentityRef identityRef = (YangIdentityRef) type.getDataTypeExtendedInfo();
        if (identityRef.isInGrouping()) {
            return JAVA_LANG;
        }
        YangJavaIdentityTranslator identity = (YangJavaIdentityTranslator) (identityRef.getReferredIdentity());
        return getTypePackage(identity, conflictResolver);
    }

    /**
     * Returns type package.
     *
     * @param info             YANG code generator info
     * @param conflictResolver object of YANG to java naming conflict util
     * @return type java package
     */
    private static String getTypePackage(JavaCodeGeneratorInfo info,
                                         YangToJavaNamingConflictUtil conflictResolver) {
        YangNode node = (YangNode) info;
        // Check for referred schema type node for grouping scenario.
        while (node.getReferredSchema() != null) {
            node = (YangNode) node.getReferredSchema();
        }
        info = (JavaCodeGeneratorInfo) node;
        if (info.getJavaFileInfo().getPackage() == null) {
            return getPackageFromParent(node.getParent(), conflictResolver);
        }
        return info.getJavaFileInfo().getPackage();
    }

    /**
     * Returns package from parent node.
     *
     * @param parent           parent YANG node
     * @param conflictResolver object of YANG to java naming conflict util
     * @return java package from parent node
     */
    private static String getPackageFromParent(YangNode parent,
                                               YangToJavaNamingConflictUtil conflictResolver) {
        if (!(parent instanceof JavaFileInfoContainer)) {
            throw new TranslatorException("invalid child node is being processed. " +
                                                  parent.getName() + " in " +
                                                  parent.getLineNumber() + " at " +
                                                  parent.getCharPosition()
                                                  + " in " + parent.getFileName());
        }
        JavaFileInfoTranslator parentInfo = ((JavaFileInfoContainer) parent).getJavaFileInfo();
        if (parentInfo.getPackage() == null) {
            updateJavaFileInfo(parent, conflictResolver);
        }
        return parentInfo.getPackage() + PERIOD + parentInfo.getJavaName().toLowerCase();
    }

    /**
     * Update the referred data model nodes java file info, this will be called,
     * when the linked node is yet to translate. Then resolve until the parent hierarchy.
     *
     * @param yangNode         node whose java info needs to be updated
     * @param conflictResolver yang plugin config
     */
    public static void updateJavaFileInfo(YangNode yangNode,
                                          YangToJavaNamingConflictUtil conflictResolver) {
        Stack<YangNode> nodesToUpdatePackage = new Stack<>();

        /*
         * Add the nodes to be updated for package info in a stack.
         */
        while (yangNode != null
                && ((JavaFileInfoContainer) yangNode)
                .getJavaFileInfo().getPackage() == null) {
            nodesToUpdatePackage.push(yangNode);
            yangNode = yangNode.getParent();
        }

        /*
         * If the package is not updated till root node, then root package needs to
         * be updated.
         */
        if (yangNode == null) {
            yangNode = nodesToUpdatePackage.pop();
            String pkg;
            if (yangNode instanceof YangJavaModuleTranslator) {
                YangJavaModuleTranslator module = (YangJavaModuleTranslator) yangNode;
                pkg = getRootPackage(module.getVersion(), module.getModuleNamespace(),
                                     module.getRevision(), conflictResolver);
            } else if (yangNode instanceof YangJavaSubModuleTranslator) {
                YangJavaSubModuleTranslator submodule = (YangJavaSubModuleTranslator) yangNode;
                pkg = getRootPackage(submodule.getVersion(),
                                     submodule.getNameSpaceFromModule(),
                                     submodule.getRevision(), conflictResolver);
            } else {
                throw new TranslatorException("Invalid root node of data model tree " +
                                                      yangNode.getName() + " in " +
                                                      yangNode.getLineNumber() + " at " +
                                                      yangNode.getCharPosition()
                                                      + " in " + yangNode.getFileName());
            }

            ((JavaCodeGeneratorInfo) yangNode).getJavaFileInfo()
                    .setJavaName(getCamelCase(yangNode.getName(), conflictResolver));
            ((JavaCodeGeneratorInfo) yangNode).getJavaFileInfo()
                    .setPackage(pkg);
            ((JavaCodeGeneratorInfo) yangNode).getJavaFileInfo()
                    .setPackageFilePath(getPackageDirPathFromJavaJPackage(
                            ((JavaCodeGeneratorInfo) yangNode).getJavaFileInfo()
                                    .getPackage()));
        }

        /*
         * Parent of the node in stack is updated with java info,
         * all the nodes can be popped and updated
         */
        while (nodesToUpdatePackage.size() != 0) {
            yangNode = nodesToUpdatePackage.pop();
            ((JavaCodeGeneratorInfo) yangNode).getJavaFileInfo()
                    .setJavaName(getCamelCase(yangNode.getName(), conflictResolver));
            ((JavaCodeGeneratorInfo) yangNode).getJavaFileInfo()
                    .setPackage(getCurNodePackage(yangNode));
            ((JavaCodeGeneratorInfo) yangNode).getJavaFileInfo()
                    .setPackageFilePath(getPackageDirPathFromJavaJPackage(
                            ((JavaCodeGeneratorInfo) yangNode).getJavaFileInfo()
                                    .getPackage()));
        }
    }

    /**
     * Returns the referred type from leaf/leaf-list.
     *
     * @param type current type in leaf
     * @return type from the leafref
     */
    private static YangType<?> getReferredTypeFromLeafref(YangType type) {
        YangLeafRef<?> lri = (YangLeafRef<?>) type.getDataTypeExtendedInfo();
        return lri.isInGrouping() ? null : lri.getEffectiveDataType();
    }

    /**
     * Returns leaf ref import string.
     *
     * @param type   YANG type
     * @param cnfg   YANG to java naming conflict util
     * @param isList true if list, false otherwise
     * @return import class
     */
    public static String getLeafRefImpClass(
            YangType type, YangToJavaNamingConflictUtil cnfg, boolean isList) {
        YangType<?> rt = getReferredTypeFromLeafref(type);
        return rt == null ? OBJECT_STRING : getJavaImportClass(rt, isList,
                                                               cnfg);
    }

    /**
     * Returns identity ref import class.
     *
     * @param type YANG type
     * @param cnfg YANG to java naming conflict util
     * @return import class
     */
    public static String getIdentityRefImpClass(
            YangType type, YangToJavaNamingConflictUtil cnfg) {
        YangIdentityRef ir = (YangIdentityRef) type.getDataTypeExtendedInfo();
        if (ir.isInGrouping()) {
            return OBJECT_STRING;
        }
        YangIdentity identity = ir.getReferredIdentity();
        return getCapitalCase(getCamelCase(identity.getName(), cnfg));
    }

    /**
     * Returns leaf ref import package.
     *
     * @param type   YANG type
     * @param cnfg   YANG to java naming conflict util
     * @param isList true if list, false otherwise
     * @return import package
     */
    private static String getLeafRefImpPkg(
            YangType type, YangToJavaNamingConflictUtil cnfg, boolean isList) {
        YangType<?> rt = getReferredTypeFromLeafref(type);
        return rt == null ? JAVA_LANG : getJavaImportPackage(rt, isList, cnfg);
    }
}
