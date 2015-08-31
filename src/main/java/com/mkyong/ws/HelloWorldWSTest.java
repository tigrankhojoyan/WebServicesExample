package com.mkyong.ws;

import com.mkyong.bo.impl.*;
import com.mkyong.sims.GenericSoapInputField;
import com.mkyong.sims.OperationsContainer;
import com.mkyong.sims.SoapUtils;

import javax.xml.soap.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tigrank on 8/24/2015.
 */
public class HelloWorldWSTest {



    @org.junit.Before
    public void setUp() throws Exception {

    }

    @org.junit.After
    public void tearDown() throws Exception {

    }

    @org.junit.Test
    public void testSetHelloWorldBo() throws Exception {

      /*  OperationsContainer operationsContainer = OperationsContainer.getInstance();
        HashMap<String, List<GenericSoapInputField>> operationsGenericInputs = operationsContainer.getOperationsGenericInputs();
        for(String operationName: operationsGenericInputs.keySet()) {
            System.out.println("Operation name ========" + operationName);
            System.out.println("Operation generic input ========" + operationsGenericInputs.get(operationName).toString());
        }*/

        SoapUtils.getOperationFields("file:///C:/Users/tigrank/Desktop/PagesWSDL/page.xml", "cardOrder");

    }

    @org.junit.Test
    public void testGetHelloWorld() throws Exception {

        Data testData = new Data();
        testData.setIntData(23);
        testData.setStringData("test");
        testData.setAnInt(33);
        testData.setAnString("eesss");
        HashMap<String, String> keyValues = WSDLUtil.getObjectKeyValues(testData);
        WSDLUtil.printMap(keyValues);
    }

    @org.junit.Test
    public void testUpdateData() throws Exception {

        Data data = new Data();
        data.setIntData(11);
        data.setStringData("test");
        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();

        // Send SOAP Message to SOAP Server
        String url = "http://localhost:8081/hello";
        //WSDLUtil.createRequestTemplate("http://localhost:8081/hello?wsdl", null, null); file:///C:/Users/tigrank/Desktop/PagesWSDL/page.xml
        SOAPMessage soapRequestBody = WSDLUtil.createRequestTemplate("http://localhost:8081/hello?wsdl", "updateData");
        List<GenericInput> genericInputs = WSDLUtil.getSpecifiedClassFields(soapRequestBody, "updateData", "http://localhost:8081/hello?wsdl");
        HtmlGenerator htmlGenerator = new HtmlGenerator();
        htmlGenerator.setGenericInputs(genericInputs);
        Map<String, String> htmlFormMap = htmlGenerator.generateEntireHTMLForms();

        WSDLUtil.printMap(htmlFormMap);
        /*SOAPMessage soapResponse = soapConnection.call(soapRequestBody, url);
        //SOAPMessage soapResponse = soapConnection.call(WSDLUtil.createSOAPRequest(), url);

        // print SOAP Response
        System.out.print("Response SOAP Message:");
        soapResponse.writeTo(System.out);

        soapConnection.close();
        //webServiceTemplate.sendSourceAndReceiveToResult("http://localhost:8081/hello",source, result);*/
    }



}