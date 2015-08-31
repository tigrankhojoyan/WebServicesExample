package com.mkyong.ws;

import com.google.common.collect.ArrayListMultimap;
import com.mkyong.bo.impl.GenericInput;
import com.mkyong.bo.impl.HtmlGenerator;
import com.mkyong.bo.impl.WSDLUtil;
import com.predic8.wsdl.Definitions;
import com.predic8.wsdl.WSDLParser;
import com.predic8.wstool.creator.RequestCreator;
import com.predic8.wstool.creator.SOARequestCreator;
import groovy.xml.MarkupBuilder;
import org.w3c.dom.NodeList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPMessage;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import com.google.common.collect.Multimap;

/**
 * Created by tigrank on 8/25/2015.
 */
public class TestServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HashMap<String, String> requestParams = new HashMap<String, String>();
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();
            HtmlGenerator htmlGenerator = new HtmlGenerator();
            SOAPMessage soapRequestBody = htmlGenerator.getSoapRequest();
            SOAPBody body = soapRequestBody.getSOAPBody();
            String url = "http://localhost:8081/hello";
            List<GenericInput> genericInputs = htmlGenerator.getGenericInputs();
            for (GenericInput genericInput : genericInputs) {
                String tempValue = request.getParameter(genericInput.getClassName()
                        + "." + genericInput.getFieldName());
                NodeList nodeList = body.getElementsByTagName(genericInput.getFieldName());

                nodeList.item(0).setTextContent(tempValue);
                System.out.println("posted value===" + request.getParameter(genericInput.getClassName()
                        + "." + genericInput.getFieldName()));
               /* if(genericInput.getxPath().contains("xpath:/updateData/arg0/inputList"))
                    continue;*/
                requestParams.put(genericInput.getxPath(), tempValue);
            }
            //xpath:/updateData/arg0/arrayList
            //requestParams.put("xpath:/updateData/arg0/arrayList/item[0]", "item1");
           /* requestParams.put("xpath:/updateData/arg0/inputList/item[1]/inputString", "item1");
            requestParams.put("xpath:/updateData/arg0/inputList/item[1]/inputInt", "1");
            requestParams.put("xpath:/updateData/arg0/inputList/item[2]/inputInt", "2");
            requestParams.put("xpath:/updateData/arg0/inputList/item[2]/inputString", "item2");*/
            createSoapRequest(requestParams);
            SOAPMessage soapMessage = WSDLUtil.createRequestAndInitializeValues("http://localhost:8081/hello?wsdl", "updateData",
                    requestParams);
            System.out.println("Test Soap message value");
            WSDLUtil.printMap(requestParams);
            soapMessage.writeTo(System.out);
            System.out.println(genericInputs.toString());
            SOAPMessage soapResponse = soapConnection.call(soapMessage, url);
            System.out.print("Response SOAP Message:");
            soapResponse.writeTo(System.out);
            htmlGenerator.setSoapResponse(soapResponse);
            soapConnection.close();
            RequestDispatcher dispatcher =
                    getServletContext().getRequestDispatcher("/Response.htm");
            dispatcher.forward(request, response);
        } catch (Exception e) {
            System.out.println("Exception ======== " + e.getMessage());
        }
    }

    public void createSoapRequest(HashMap<String, String> formParams) {
        WSDLParser parser = new WSDLParser();

        Definitions wsdl = parser.parse("http://localhost:8081/hello?wsdl");

        StringWriter writer = new StringWriter();

        //SOARequestCreator constructor: SOARequestCreator(Definitions, Creator, MarkupBuilder)
        SOARequestCreator creator = new SOARequestCreator(wsdl, new RequestCreator(), new MarkupBuilder(writer));
        creator.setFormParams(formParams);

        //creator.createRequest(PortType name, Operation name, Binding name);
        creator.createRequest("HelloWorldWSPort", "updateData", "HelloWorldWSPortBinding");

        System.out.println("xxxxxxxxxxxxxxx");
        System.out.println(writer);
        System.out.println("xxxxxxxxxxxxxxx");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
