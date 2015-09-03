package com.mkyong.sims;

import javax.xml.soap.SOAPMessage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by tigrank on 8/28/2015.
 */
public class OperationsContainer {

    private static OperationsContainer instance = null;
    private static HashMap<String, GenericSoapInputField> operationsGenericInputs =
            new HashMap<String, GenericSoapInputField>();

    private static List<String> currentElementXpathList = new ArrayList<String>();

    private OperationsContainer() {
        //todo will be got from property file
        operationsGenericInputs = SoapUtils.getOperationFields("file:///C:/Users/tigrank/Desktop/PagesWSDL/page.xml");

        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        System.out.println(operationsGenericInputs);
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
    }

    public static OperationsContainer getInstance() {
        if(null == instance) {
            instance = new OperationsContainer();
        }
        return instance;
    }

    public static HashMap<String, GenericSoapInputField> getOperationsGenericInputs() {
        return operationsGenericInputs;
    }

    public static void setOperationsGenericInputs(HashMap<String, GenericSoapInputField> operationsGenericInputs) {
        instance.operationsGenericInputs = operationsGenericInputs;
    }

    public static List<String> getCurrentElementXpathList() {
        return currentElementXpathList;
    }

    public static void setCurrentElementXpathList(List<String> currentElementXpathList) {
        OperationsContainer.currentElementXpathList = currentElementXpathList;
    }

    public static void addXpathIntoList(String xPath) {
        currentElementXpathList.add(xPath);
    }

    public static String getXpaths() {
        return currentElementXpathList.toString();
    }
}
