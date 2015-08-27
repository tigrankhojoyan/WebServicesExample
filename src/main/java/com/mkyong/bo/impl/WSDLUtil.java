package com.mkyong.bo.impl;

import com.predic8.wsdl.*;
import com.predic8.wstool.creator.RequestCreator;
import com.predic8.wstool.creator.RequestTemplateCreator;
import com.predic8.wstool.creator.SOARequestCreator;
import groovy.xml.MarkupBuilder;
import org.w3c.dom.*;
import org.w3c.dom.Node;

import javax.xml.soap.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by tigrank on 8/25/2015.
 */
public final class WSDLUtil {

    public static <T> Field[] getAllFields(T object) {
        Class klass = object.getClass();
        List<Field> fields = new ArrayList<Field>();
        fields.addAll(Arrays.asList(klass.getDeclaredFields()));
        if (klass.getSuperclass() != null) {
            fields.addAll(Arrays.asList(getAllFields(klass.getSuperclass())));
        }
        List<String> fieldNames = getFieldsNames(fields.toArray(new Field[]{}));
        for(String fieldName : fieldNames) {
            System.out.println("field name========" + fieldName);
        }
        return fields.toArray(new Field[]{});
    }

    public static List<String> getFieldsNames(Field[] fields) {
        List<String> fieldNames = new ArrayList<String>();
        for (Field field : fields) {
            fieldNames.add(field.getName());
        }
        return fieldNames;
    }

    public static void printMap(Map mp) {
        Iterator it = mp.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }
    }

    public static <T> HashMap<String, String> getObjectKeyValues(T object) throws NoSuchFieldException, IllegalAccessException {
        Field[] allFields = getAllFields(object.getClass());
        List<String> fieldNames = getFieldsNames(allFields);
        System.out.println("allFields========" + fieldNames);
        HashMap<String, String> keyValues = new HashMap<String, String>();
        Class<?> clazz = object.getClass();
        for (String fieldName : fieldNames) {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            Object fieldValue = field.get(object);
            keyValues.put(fieldName,fieldValue.toString());
        }
        return keyValues;
    }

    public static SOAPMessage createRequestAndInitializeValues(String url, String className,
                                                               HashMap<String, String> requestParameters) throws IOException, SOAPException {
        WSDLParser parser = new WSDLParser();
        Definitions wsdl = parser.parse(url);
        StringWriter writer = new StringWriter();
        MarkupBuilder markupBuilder = new MarkupBuilder(writer);
        SOARequestCreator creator = new SOARequestCreator(wsdl, new RequestCreator(), markupBuilder);
        System.out.println("1111111");
        creator.setFormParams(requestParameters);
        System.out.println("22222222");
        for (Service service : wsdl.getServices()) {
            for (Port port : service.getPorts()) {
                Binding binding = port.getBinding();
                PortType portType = binding.getPortType();
                for (Operation op : portType.getOperations()) {
                    //RequestTemplateCreator templateCreator = new RequestTemplateCreator();
                    if (op.getName().equals(className)) {
                        //creator.setCreator(templateCreator);
                        System.out.println("333333333");
                        creator.createRequest(port.getName(), op.getName(), binding.getName());
                        System.out.println("44444444");
                        String requestBody = writer.toString();
                        SOAPMessage requestMessage = parseSoapRequest("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + requestBody,
                                null, null);
                        System.out.println(requestBody);
                        return requestMessage;
                    }
                    writer.getBuffer().setLength(0);
                }
            }
        }
        return null;
    }

    public static SOAPMessage createRequestTemplate(String url, List<String> keys, List<String> values) throws Exception {
        WSDLParser parser = new WSDLParser();
        Definitions wsdl = parser.parse(url);
        StringWriter writer = new StringWriter();
        MarkupBuilder markupBuilder = new MarkupBuilder(writer);
        SOARequestCreator creator = new SOARequestCreator(wsdl, new Object(), markupBuilder);
        for (Service service : wsdl.getServices()) {
            System.out.println("ports count==========" + service.getPorts().size());
            for (Port port : service.getPorts()) {
                Binding binding = port.getBinding();
                PortType portType = binding.getPortType();
                System.out.println("operations count============" + portType.getOperations().size());
                for (Operation op : portType.getOperations()) {
                    RequestTemplateCreator templateCreator = new RequestTemplateCreator();
                    System.out.println("name==============" + op.getName());
                    if (op.getName().equals("updateData")) {
                        creator.setCreator(templateCreator);
                        System.out.println("creator==========" + creator.getCreator().toString());
                        creator.createRequest(port.getName(), op.getName(), binding.getName());
                        String requestBody = writer.toString();
                        SOAPMessage requestMessage = parseSoapRequest("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + requestBody, keys, values);
                        System.out.println(requestBody);
                        return requestMessage;
                    }
                    writer.getBuffer().setLength(0);
                }
            }
        }
        return null;
    }

    public static SOAPMessage createSOAPRequest() throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();

        String serverURI = "http://ws.mkyong.com/";

        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("ws", serverURI);

        // SOAP Body
        SOAPBody soapBody = envelope.getBody();
        SOAPElement soapBodyElem = soapBody.addChildElement("updateData", "ws");
        SOAPElement args = soapBodyElem.addChildElement("arg0");
        SOAPElement intData = args.addChildElement("intData");
        intData.setTextContent("21");
        SOAPElement stringData = args.addChildElement("stringData");
        stringData.setTextContent("test");

        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", serverURI + "VerifyEmail");

        soapMessage.saveChanges();

        /* Print the request message */
        System.out.print("Request SOAP Message:");
        soapMessage.writeTo(System.out);
        System.out.println();

        return soapMessage;
    }

    public static SOAPMessage parseSoapRequest(String xmlString, List<String> fields, List<String> values) throws SOAPException, IOException {
        MessageFactory factory = MessageFactory.newInstance();
        SOAPMessage message = factory.createMessage(
                new MimeHeaders(),
                new ByteArrayInputStream(xmlString.getBytes(Charset
                        .forName("UTF-8"))));
        SOAPBody body = message.getSOAPBody();
        NodeList list = body.getElementsByTagName("s11:");
        for (int i = 0; i < list.getLength(); i++) {
            System.out.println("node name ============" + list.item(i).getNodeName());
        }

        return message;
    }

    public static void getInnerFields(Node node, String className, List<GenericInput> genericInputs, String xPath) {
        System.out.println("inner main node ==== " + node.getNodeName());
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node1 = nodeList.item(i);
            if(node1.getNodeName().substring(0,1).equals("#"))
                continue;
            if(node1.getChildNodes().getLength() > 1) {
                xPath = xPath + "/" + className;
                getInnerFields(node1, className, genericInputs, xPath);
            } else {
                GenericInput genericInput = new GenericInput(className, node1.getNodeName(), node.getNodeName(),
                true, "innerExample");
                genericInput.setxPath(xPath + "/" + node1.getNodeName());
                genericInputs.add(genericInput);
            }
            System.out.println("given node name======" + node.getNodeName());
            System.out.println("inner node is equal to =========== " +node1.getNodeName());
        }
    }

    public static List<GenericInput> getSpecifiedClassFields(SOAPMessage requestBody, String className) throws SOAPException {
        List<GenericInput> genericInputs = new ArrayList<GenericInput>();
        SOAPBody body = requestBody.getSOAPBody();
        NodeList nodeList = body.getChildNodes();
        for(int i = 0; i < nodeList.getLength(); i++) {
            org.w3c.dom.Node node = nodeList.item(i);
            if(node.getNodeName().equals("ns1:" + className)) {
                NodeList nodeList1 = node.getChildNodes();
                for(int j = 0; j < nodeList1.getLength(); j++) {
                    Node node1 = nodeList1.item(j);
                    if(node1.getNodeName().equals("arg0")) {
                        NodeList nodeList2 = node1.getChildNodes();
                        for (int i1 = 0; i1 < nodeList2.getLength(); i1++) {
                            Node node2 = nodeList2.item(i1);
                            if(node2.getNodeName().substring(0,1).equals("#"))
                                continue;
                            String xPath = "xpath:/" + className + "/arg0/" + node2.getNodeName();
                            if(node2.getChildNodes().getLength() > 1) {
                                getInnerFields(node2, node2.getNodeName(), genericInputs, xPath);
                            } else {
                                System.out.println("nodeName=======" + node2.getNodeName());
                                GenericInput genericInput = new GenericInput(className, node2.getNodeName(), "Basic",
                                true, "example");
                                genericInput.setxPath(xPath);
                                genericInputs.add(genericInput);
                            }
                        }
                    }
                }
            }
            //System.out.println("nodeName======" + nodeList.item(i).getNodeName());
        }
        System.out.println("GenericInputList=======" + genericInputs);
        return genericInputs;
    }

    public static String returnStringFromHashMap(HashMap<String, String> hashMap) {
        String returningValue = "";
        Iterator it = hashMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            returningValue += pair.getValue();
            it.remove(); // avoids a ConcurrentModificationException
        }
        return returningValue;
    }

    /*public static List<GenericInput> getSpecifiedClassFields(SOAPMessage requestBody, String className) throws SOAPException {
        List<GenericInput> genericInputs = new ArrayList<GenericInput>();
        SOAPBody body = requestBody.getSOAPBody();
        NodeList nodeList = body.getChildNodes();
        for(int i = 0; i < nodeList.getLength(); i++) {
            org.w3c.dom.Node node = nodeList.item(i);
            if(node.getNodeName().equals(className)) {
                NodeList nodeList1 = node.getChildNodes();
                for(int j = 0; j < nodeList1.getLength(); j++) {
                    Node node1 = nodeList1.item(j);
                    if(node1.getNodeName().equals("ns1:input")) {
                        NodeList nodeList2 = node1.getChildNodes();
                        for (int i1 = 0; i1 < nodeList2.getLength(); i1++) {
                            Node node2 = nodeList2.item(i1);
                            if(node2.getNodeName().substring(0,1).equals("#"))
                                continue;
                            System.out.println("innerNodesCount==========" + node2.getChildNodes().getLength());
                            System.out.println("nodeName=======" + node2.getNodeName());
                            if(node2.getChildNodes().getLength() > 1) {
                                NodeList nodeList3 = node2.getChildNodes();
                                for(int j1 = 0; j1 < nodeList3.getLength(); j1++) {
                                    Node node3 = nodeList3.item(j1);
                                    if(node3.getNodeName().substring(0,1).equals("#"))
                                        continue;
                                    System.out.println("innerNodesCount==========" + node3.getChildNodes().getLength());
                                    System.out.println("innerNodeName============================" + node3.getNodeName());
                                }
                            }
                        }
                    }
                }
            }
            //System.out.println("nodeName======" + nodeList.item(i).getNodeName());
        }
        return null;
    }*/
}
