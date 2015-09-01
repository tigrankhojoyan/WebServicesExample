package com.mkyong.sims;

import javax.xml.soap.SOAPMessage;
import java.util.HashMap;
import java.util.List;

/**
 * Created by tigrank on 8/28/2015.
 */
public class OperationsContainer {

    private static OperationsContainer instance = null;
    private static HashMap<String, List<GenericSoapInputField>> operationsGenericInputs =
            new HashMap<String, List<GenericSoapInputField>>();
    private static HashMap<String, SOAPMessage> soapRequestTemplates = new HashMap<String, SOAPMessage>();

    private OperationsContainer() {

    }

    public static OperationsContainer getInstance() {
        if(null == instance) {
            instance = new OperationsContainer();
        }
        return instance;
    }

    public static HashMap<String, List<GenericSoapInputField>> getOperationsGenericInputs() {
        return operationsGenericInputs;
    }

    public static void setOperationsGenericInputs(HashMap<String, List<GenericSoapInputField>> operationsGenericInputs) {
        instance.operationsGenericInputs = operationsGenericInputs;
    }

    public static HashMap<String, SOAPMessage> getSoapRequestTemplates() {
        return soapRequestTemplates;
    }

    public static void setSoapRequestTemplates(HashMap<String, SOAPMessage> soapRequestTemplates) {
        OperationsContainer.soapRequestTemplates = soapRequestTemplates;
    }
}
