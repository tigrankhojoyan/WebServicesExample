package com.mkyong.bo.impl;

import com.predic8.schema.ComplexType;
import com.predic8.schema.Schema;
import com.predic8.wsdl.*;
import com.predic8.wstool.creator.RequestCreator;
import com.predic8.wstool.creator.RequestTemplateCreator;
import com.predic8.wstool.creator.SOARequestCreator;
import groovy.xml.MarkupBuilder;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.*;
import javax.xml.soap.Node;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by tigrank on 8/28/2015.
 */
public final class WSDLUtilNew {

    public static void printMap(Map mp) {
        for (Object key : mp.keySet())
            System.out.println(key + " - " + mp.get(key));
        /*Iterator it = mp.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }*/
    }

    public static void getInnerFields(org.w3c.dom.Node node, String className, List<GenericInput> genericInputs, String xPath,
                                      HashMap<String, Boolean> fieldsMandatoryList) {
        String xPathClass = className.substring(4, className.length());
        System.out.println("inner main node ==== " + node.getNodeName());
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            org.w3c.dom.Node node1 = nodeList.item(i);
            if (node1.getNodeName().substring(0, 1).equals("#"))
                continue;
            if (node1.getChildNodes().getLength() > 1) {
                if (!xPath.contains(xPathClass))
                    xPath = xPath + "/" + xPathClass;
                getInnerFields(node1, className, genericInputs, xPath, fieldsMandatoryList);
            } else {
                Boolean isMandatory = true;
                if (fieldsMandatoryList.containsKey(node.getNodeName())) {
                    isMandatory = fieldsMandatoryList.get(node.getNodeName());
                }
                GenericInput genericInput = new GenericInput(className, node1.getNodeName(), node.getNodeName(),
                        isMandatory, "innerExample");
                xPathClass = node1.getNodeName().substring(4, node1.getNodeName().length());
                if (!xPath.contains(xPathClass))
                    genericInput.setxPath(xPath + "/" + xPathClass);
                genericInputs.add(genericInput);
            }
           /* System.out.println("given node name======" + node.getNodeName());
            System.out.println("inner node is equal to =========== " + node1.getNodeName());*/
        }
    }

    public static HashMap<String, List<GenericInput>> getSpecifiedClassFields(HashMap<String, SOAPMessage> requestTemplates, String wsdlURL) throws Exception {
        List<GenericInput> genericInputs = new ArrayList<GenericInput>();
        HashMap<String, List<GenericInput>> operationsGenericInputs = new HashMap<String, List<GenericInput>>();
        HashMap<String, Boolean> fieldsMandatoryList = new HashMap<String, Boolean>();
       /* System.out.println("tttttttttttttttttttttttt");
        printMap(fieldsMandatoryList);
        System.out.println("tttttttttttttttttttttttt");*/
        for(String operationName:requestTemplates.keySet()) {
            SOAPBody body = requestTemplates.get(operationName).getSOAPBody();
            NodeList nodeList = body.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                org.w3c.dom.Node node = nodeList.item(i);
                fieldsMandatoryList = getElementsOfSpecifiedClass(operationName + "Input",
                        wsdlURL);
                NodeList nodeList1 = node.getChildNodes();
                for (int j = 0; j < nodeList1.getLength(); j++) {
                    org.w3c.dom.Node node1 = nodeList1.item(j);
                    if (node1.getNodeName().equals("ns1:input")) {//"arg0"
                        NodeList nodeList2 = node1.getChildNodes();
                        for (int i1 = 0; i1 < nodeList2.getLength(); i1++) {
                            org.w3c.dom.Node node2 = nodeList2.item(i1);
                            if (node2.getNodeName().substring(0, 1).equals("#"))
                                continue;
                            //String xPath = "xpath:/" + operationName + "/input/" + node2.getNodeName();
                            String xPathClass = node2.getNodeName();
                            String xPath = xPathClass.substring(4, xPathClass.length());
                            if (node2.getChildNodes().getLength() > 1) {
                                getInnerFields(node2, node2.getNodeName(), genericInputs, xPath, fieldsMandatoryList);
                            } else {
                                Boolean isMandatory = true;
                                if (fieldsMandatoryList.containsKey(node2.getNodeName())) {
                                    isMandatory = fieldsMandatoryList.get(node2.getNodeName());
                                }
                                GenericInput genericInput = new GenericInput(node.getNodeName(), node2.getNodeName(), "Basic",
                                        isMandatory, "example");
                                genericInput.setxPath(xPath);
                                genericInputs.add(genericInput);
                            }
                        }
                    }
                }

            }
            System.out.println("operationName=======" + operationName);
            operationsGenericInputs.put(operationName, genericInputs);
            genericInputs = new ArrayList<GenericInput>();
        }
        return operationsGenericInputs;
    }

    public static HashMap<String, SOAPMessage> createRequestTemplate(String url) throws Exception {
        WSDLParser parser = new WSDLParser();
        Definitions wsdl = parser.parse(url);
        StringWriter writer = new StringWriter();
        MarkupBuilder markupBuilder = new MarkupBuilder(writer);
        SOARequestCreator creator = new SOARequestCreator(wsdl, new Object(), markupBuilder);
        HashMap<String, SOAPMessage> soapMessageList = new HashMap<String, SOAPMessage>();
        for (Service service : wsdl.getServices()) {
            System.out.println("ports count==========" + service.getPorts().size());
            for (Port port : service.getPorts()) {
                Binding binding = port.getBinding();
                PortType portType = binding.getPortType();
                System.out.println("operations count============" + portType.getOperations().size());
                for (Operation op : portType.getOperations()) {
                    RequestTemplateCreator templateCreator = new RequestTemplateCreator();
                    String operationName = op.getName();
                    creator.setCreator(templateCreator);
                    creator.createRequest(port.getName(), op.getName(), binding.getName());
                    String requestBody = writer.toString();
                    SOAPMessage requestMessage = parseSoapRequest("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + requestBody);
                    soapMessageList.put(operationName, requestMessage);
                    System.out.println("==============requestBody================");
                    System.out.println(requestBody);
                    System.out.println("==============requestBody================");
                    writer.getBuffer().setLength(0);
                }
            }
        }
        return soapMessageList;
    }

    public static SOAPMessage parseSoapRequest(String xmlString) throws SOAPException, IOException {
        MessageFactory factory = MessageFactory.newInstance();
        SOAPMessage message = factory.createMessage(
                new MimeHeaders(),
                new ByteArrayInputStream(xmlString.getBytes(Charset
                        .forName("UTF-8"))));
        /*SOAPBody body = message.getSOAPBody();
        NodeList list = body.getElementsByTagName("s11:");
        for (int i = 0; i < list.getLength(); i++) {
            System.out.println("node name ============" + list.item(i).getNodeName());
        }*/
        return message;
    }

    public static NodeList getElementsOfTheSpecifiedClass(String typeName, String wsdlURL) throws Exception {
        WSDLParser parser = new WSDLParser();
        Definitions defs = parser
                .parse(wsdlURL);
        List<Schema> schemas = defs.getSchemas();
        System.out.println("SchemaLength=========" + schemas.size());
        Schema schema = schemas.get(0);
        ComplexType type = schema.getComplexType(typeName);
        Document document = loadXMLFromString(type.getAsString());
        NodeList list = document.getElementsByTagName("xsd:element");
        return list;
    }

    public static Document loadXMLFromString(String xml) throws Exception {
        String xmlString = "<?xml version=\"1.0\" encoding=\"utf-8\"?> \n" +
                "<xs:schema elementFormDefault=\"qualified\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\"> \n" +
                xml + "</xs:schema> ";
        System.out.println(xmlString);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xmlString));
        return builder.parse(is);
    }

    public static HashMap<String, Boolean> getElementsOfSpecifiedClass(String className, String wsdlURL) throws Exception {
        HashMap<String, Boolean> elementsMandatoryList = new HashMap<String, Boolean>();
        NodeList list = getElementsOfTheSpecifiedClass(className, wsdlURL);
        for (int i = 0; i < list.getLength(); i++) {
            Element element = (Element) list.item(i);
            String name = element.getAttribute("name");
            Boolean isMandatory = true;
            String mandatoryValue = element.getAttribute("minOccurs");
            if (mandatoryValue.equals("0")) {
                isMandatory = false;
            }
            elementsMandatoryList.put("ns1:" + name, isMandatory);
        }
        return elementsMandatoryList;
    }
}
