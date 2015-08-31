package com.mkyong.bo.impl;

import javax.xml.soap.SOAPMessage;
import java.util.HashMap;
import java.util.List;

/**
 * Created by tigrank on 8/28/2015.
 */
public class OperationsContainerTest {

    private static OperationsContainerTest instance = null;
    private static HashMap<String, List<GenericInput>> operationsGenericInputs =
            new HashMap<String, List<GenericInput>>();
    private static HashMap<String, SOAPMessage> soapRequestTemplates = new HashMap<String, SOAPMessage>();

    private OperationsContainerTest() throws Exception {
        soapRequestTemplates = WSDLUtilNew.createRequestTemplate("file:///C:/Users/tigrank/Desktop/PagesWSDL/page.xml");
        operationsGenericInputs = WSDLUtilNew.getSpecifiedClassFields(soapRequestTemplates, "file:///C:/Users/tigrank/Desktop/PagesWSDL/page.xml");
    }

    public static OperationsContainerTest getInstance() throws Exception {
        if(null == instance) {
            instance = new OperationsContainerTest();
        }
        return instance;
    }

    public static HashMap<String, List<GenericInput>> getOperationsGenericInputs() {
        return operationsGenericInputs;
    }

    public static void setOperationsGenericInputs(HashMap<String, List<GenericInput>> operationsGenericInputs) {
        instance.operationsGenericInputs = operationsGenericInputs;
    }

    public static HashMap<String, SOAPMessage> getSoapRequestTemplates() {
        return soapRequestTemplates;
    }

    public static void setSoapRequestTemplates(HashMap<String, SOAPMessage> soapRequestTemplates) {
        OperationsContainerTest.soapRequestTemplates = soapRequestTemplates;
    }
}
