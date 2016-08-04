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

import java.io.IOException;

import org.onosproject.yangutils.datamodel.RpcNotificationContainer;
import org.onosproject.yangutils.datamodel.YangInput;
import org.onosproject.yangutils.datamodel.YangNode;
import org.onosproject.yangutils.datamodel.YangOutput;
import org.onosproject.yangutils.translator.tojava.JavaFileInfoTranslator;
import org.onosproject.yangutils.datamodel.javadatamodel.YangJavaRpc;
import org.onosproject.yangutils.translator.exception.TranslatorException;
import org.onosproject.yangutils.translator.tojava.JavaAttributeInfo;
import org.onosproject.yangutils.translator.tojava.JavaCodeGenerator;
import org.onosproject.yangutils.translator.tojava.JavaCodeGeneratorInfo;
import org.onosproject.yangutils.translator.tojava.JavaFileInfoContainer;
import org.onosproject.yangutils.translator.tojava.JavaQualifiedTypeInfoTranslator;
import org.onosproject.yangutils.translator.tojava.TempJavaCodeFragmentFiles;
import org.onosproject.yangutils.translator.tojava.TempJavaCodeFragmentFilesContainer;
import org.onosproject.yangutils.translator.tojava.TempJavaFragmentFiles;
import org.onosproject.yangutils.utils.io.YangPluginConfig;

import static org.onosproject.yangutils.datamodel.utils.DataModelUtils.getParentNodeInGenCode;
import static org.onosproject.yangutils.translator.tojava.JavaAttributeInfo.getAttributeInfoForTheData;
import static org.onosproject.yangutils.translator.tojava.JavaQualifiedTypeInfoTranslator.getQualifiedTypeInfoOfCurNode;
import static org.onosproject.yangutils.translator.tojava.YangJavaModelUtils.updatePackageInfo;
import static org.onosproject.yangutils.utils.UtilConstants.SERVICE;
import static org.onosproject.yangutils.utils.io.impl.YangIoUtils.getCapitalCase;

/**
 * Represents rpc information extended to support java code generation.
 */
public class YangJavaRpcTranslator
        extends YangJavaRpc
        implements JavaCodeGenerator, JavaCodeGeneratorInfo {

    private static final long serialVersionUID = 806201622L;

    /**
     * Temporary file for code generation.
     */
    private transient TempJavaCodeFragmentFiles tempJavaCodeFragmentFiles;

    /**
     * Creates an instance of YANG java rpc.
     */
    public YangJavaRpcTranslator() {
        super();
        setJavaFileInfo(new JavaFileInfoTranslator());
    }

    /**
     * Returns the generated java file information.
     *
     * @return generated java file information
     */
    @Override
    public JavaFileInfoTranslator getJavaFileInfo() {

        if (javaFileInfo == null) {
            throw new TranslatorException("missing java info in java datamodel node");
        }
        return (JavaFileInfoTranslator) javaFileInfo;
    }

    /**
     * Sets the java file info object.
     *
     * @param javaInfo java file info object
     */
    @Override
    public void setJavaFileInfo(JavaFileInfoTranslator javaInfo) {
        javaFileInfo = javaInfo;
    }

    @Override
    public TempJavaCodeFragmentFiles getTempJavaCodeFragmentFiles() {
        return tempJavaCodeFragmentFiles;
    }

    @Override
    public void setTempJavaCodeFragmentFiles(TempJavaCodeFragmentFiles fileHandle) {
        tempJavaCodeFragmentFiles = fileHandle;
    }

    /**
     * Prepares the information for java code generation corresponding to YANG
     * RPC info.
     *
     * @param yangPlugin YANG plugin config
     * @throws TranslatorException translator operations fails
     */
    @Override
    public void generateCodeEntry(YangPluginConfig yangPlugin)
            throws TranslatorException {

        // Add package information for rpc and create corresponding folder.
        try {
            updatePackageInfo(this, yangPlugin);
        } catch (IOException e) {
            throw new TranslatorException("Failed to prepare generate code entry for RPC node " + getName());
        }
    }

    /**
     * Creates a java file using the YANG RPC info.
     *
     * @throws TranslatorException translator operations fails
     */
    @Override
    public void generateCodeExit()
            throws TranslatorException {
        // Get the parent module/sub-module.
        YangNode parent = getParentNodeInGenCode(this);

        // Parent should be holder of rpc or notification.
        if (!(parent instanceof RpcNotificationContainer)) {
            throw new TranslatorException("parent node of rpc can only be module or sub-module");
        }

        /*
         * Create attribute info for input and output of rpc and add it to the
         * parent import list.
         */

        JavaAttributeInfo javaAttributeInfoOfInput = null;
        JavaAttributeInfo javaAttributeInfoOfOutput = null;

        // Get the child input and output node and obtain create java attribute
        // info.
        YangNode yangNode = getChild();
        while (yangNode != null) {
            if (yangNode instanceof YangInput) {
                javaAttributeInfoOfInput = getChildNodeAsAttributeInParentService(yangNode, this);

            } else if (yangNode instanceof YangOutput) {
                javaAttributeInfoOfOutput = getChildNodeAsAttributeInParentService(yangNode, this);
            } else {
                throw new TranslatorException("RPC should contain only input/output child nodes.");
            }
            yangNode = yangNode.getNextSibling();
        }

        if (!(parent instanceof TempJavaCodeFragmentFilesContainer)) {
            throw new TranslatorException("missing parent temp file handle");
        }

        /*
         * Add the rpc information to the parent's service temp file.
         */
        try {

            ((TempJavaCodeFragmentFilesContainer) parent).getTempJavaCodeFragmentFiles().getServiceTempFiles()
                    .addJavaSnippetInfoToApplicableTempFiles(javaAttributeInfoOfInput, javaAttributeInfoOfOutput,
                            ((JavaFileInfoContainer) parent).getJavaFileInfo().getPluginConfig(), getName());

        } catch (IOException e) {
            throw new TranslatorException("Failed to generate code for RPC node " + getName());
        }
        // No file will be generated during RPC exit.
    }

    /**
     * Creates an attribute info object corresponding to a data model node and
     * return it.
     *
     * @param childNode   child data model node(input / output) for which the java code generation
     *                    is being handled
     * @param currentNode parent node (module / sub-module) in which the child node is an attribute
     * @return AttributeInfo attribute details required to add in temporary
     * files
     */
    private JavaAttributeInfo getChildNodeAsAttributeInParentService(
            YangNode childNode, YangNode currentNode) {

        YangNode parentNode = getParentNodeInGenCode(currentNode);

        String childNodeName = ((JavaFileInfoContainer) childNode).getJavaFileInfo().getJavaName();
        /*
         * Get the import info corresponding to the attribute for import in
         * generated java files or qualified access
         */
        JavaQualifiedTypeInfoTranslator qualifiedTypeInfo = getQualifiedTypeInfoOfCurNode(childNode,
                getCapitalCase(childNodeName));
        if (!(parentNode instanceof TempJavaCodeFragmentFilesContainer)) {
            throw new TranslatorException("Parent node does not have file info");
        }

        TempJavaFragmentFiles tempJavaFragmentFiles;
        tempJavaFragmentFiles = ((TempJavaCodeFragmentFilesContainer) parentNode)
                .getTempJavaCodeFragmentFiles()
                .getServiceTempFiles();

        if (tempJavaFragmentFiles == null) {
            throw new TranslatorException("Parent node does not have service file info");
        }
        boolean isQualified = addImportToService(qualifiedTypeInfo);
        return getAttributeInfoForTheData(qualifiedTypeInfo, childNodeName, null, isQualified, false);
    }

    /**
     * Adds to service class import list.
     *
     * @param importInfo import info
     * @return true or false
     */
    private boolean addImportToService(JavaQualifiedTypeInfoTranslator importInfo) {
        JavaFileInfoTranslator fileInfo = ((JavaFileInfoContainer) getParent()).getJavaFileInfo();

        if (importInfo.getClassInfo().contentEquals(SERVICE)
                || importInfo.getClassInfo().contentEquals(getCapitalCase(fileInfo.getJavaName() + SERVICE))) {
            return true;
        }

        String className;
        className = getCapitalCase(fileInfo.getJavaName()) + "Service";

        return ((TempJavaCodeFragmentFilesContainer) getParent()).getTempJavaCodeFragmentFiles()
                .getServiceTempFiles().getJavaImportData().addImportInfo(importInfo,
                        className, fileInfo.getPackage());
    }

}
