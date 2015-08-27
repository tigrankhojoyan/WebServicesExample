package com.mkyong.ws;

import com.mkyong.bo.impl.Data;
import com.mkyong.bo.impl.GenericInput;
import com.mkyong.bo.impl.HtmlGenerator;
import com.mkyong.bo.impl.WSDLUtil;
import com.predic8.schema.restriction.RestrictionUtil;
import com.predic8.wsdl.*;
import com.predic8.wstool.creator.RequestTemplateCreator;
import com.predic8.wstool.creator.SOARequestCreator;
import groovy.xml.MarkupBuilder;
import org.w3c.dom.NodeList;

import javax.xml.soap.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
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
        HtmlGenerator htmlGenerator = HtmlGenerator.getInstance();
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