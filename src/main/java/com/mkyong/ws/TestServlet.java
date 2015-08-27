package com.mkyong.ws;

import com.mkyong.bo.impl.GenericInput;
import com.mkyong.bo.impl.HtmlGenerator;
import com.mkyong.bo.impl.WSDLUtil;
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
import java.util.HashMap;
import java.util.List;

/**
 * Created by tigrank on 8/25/2015.
 */
public class TestServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HashMap<String, String> requestParams = new HashMap<String, String>();
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();
            HtmlGenerator htmlGenerator = HtmlGenerator.getInstance();
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
                requestParams.put(genericInput.getxPath(), tempValue);
            }
            requestParams.put("arrayList", "anyValue");
            requestParams.put("arrayList", "anyValue1");
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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
