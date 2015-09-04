package com.mkyong.sims;

import com.predic8.wsdl.Definitions;
import com.predic8.wsdl.WSDLParser;

import javax.xml.soap.SOAPMessage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by tigrank on 8/28/2015.
 */
public class OperationsContainer {

    private static OperationsContainer instance = null;
    private static HashMap<String, GenericSoapInputField> operationsGenericInputs = new HashMap<String, GenericSoapInputField>();
    private static Definitions definitions;

    private OperationsContainer() {
        WSDLParser parser = new WSDLParser();
        //todo will be got from property file
        definitions = parser.parse("file:///C:/Users/tigrank/Desktop/PagesWSDL/page.xml");
        operationsGenericInputs = SoapUtils.getOperationFields(definitions);
    }

    public static void initializeOperationContainer() {
        if (null == instance) {
            instance = new OperationsContainer();
        }
    }


    public static Definitions getDefinitions() {
        return definitions;
    }

    public static HashMap<String, GenericSoapInputField> getOperationsGenericInputs() {
        return operationsGenericInputs;
    }

    public static void setOperationsGenericInputs(HashMap<String, GenericSoapInputField> operationsGenericInputs) {
        instance.operationsGenericInputs = operationsGenericInputs;
    }
}
