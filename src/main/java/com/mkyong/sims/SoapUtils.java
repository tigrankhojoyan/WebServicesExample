package com.mkyong.sims;

import com.mkyong.bo.impl.GenericInput;
import com.predic8.schema.*;
import com.predic8.wsdl.*;
import com.predic8.wstool.creator.RequestTemplateCreator;
import com.predic8.wstool.creator.SOARequestCreator;
import groovy.xml.MarkupBuilder;
import org.codehaus.groovy.runtime.powerassert.SourceText;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class SoapUtils {

    public static void getInnerFields(org.w3c.dom.Node node, String className, List<GenericSoapInputField> genericInputs, String xPath,
                                      HashMap<String, Boolean> fieldsMandatoryList) {
        String xPathClass = className.substring(4, className.length());
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
                GenericSoapInputField genericInput = new GenericSoapInputField(className, node1.getNodeName(),
                        isMandatory, "", null);
                xPathClass = node1.getNodeName().substring(4, node1.getNodeName().length());
                if (!xPath.contains(xPathClass))
                    genericInput.setxPath(xPath + "/" + xPathClass);
                genericInputs.add(genericInput);
            }
        }
    }

    public static HashMap<String, List<GenericSoapInputField>> getSpecifiedClassFields(HashMap<String, SOAPMessage> requestTemplates, String wsdlURL) {
        List<GenericSoapInputField> genericInputs = new ArrayList<GenericSoapInputField>();
        HashMap<String, List<GenericSoapInputField>> operationsGenericInputs = new HashMap<String, List<GenericSoapInputField>>();
        try {
            for (String operationName : requestTemplates.keySet()) {
                SOAPBody body = requestTemplates.get(operationName).getSOAPBody();
                NodeList nodeList = body.getChildNodes();
                for (int i = 0; i < nodeList.getLength(); i++) {
                    org.w3c.dom.Node node = nodeList.item(i);
                    HashMap<String, Boolean> fieldsMandatoryList = getMandatoryAndOptionalOptions(operationName + "Input",
                            wsdlURL);
                    NodeList nodeList1 = node.getChildNodes();
                    for (int j = 0; j < nodeList1.getLength(); j++) {
                        org.w3c.dom.Node node1 = nodeList1.item(j);
                        if (node1.getNodeName().equals("ns1:input")) {
                            NodeList nodeList2 = node1.getChildNodes();
                            for (int i1 = 0; i1 < nodeList2.getLength(); i1++) {
                                org.w3c.dom.Node node2 = nodeList2.item(i1);
                                if (node2.getNodeName().substring(0, 1).equals("#"))
                                    continue;
                                String xPathClass = node2.getNodeName();
                                String xPath = xPathClass.substring(4, xPathClass.length());
                                if (node2.getChildNodes().getLength() > 1) {
                                    getInnerFields(node2, node2.getNodeName(), genericInputs, xPath, fieldsMandatoryList);
                                } else {
                                    Boolean isMandatory = true;
                                    if (fieldsMandatoryList.containsKey(node2.getNodeName())) {
                                        isMandatory = fieldsMandatoryList.get(node2.getNodeName());
                                    }
                                    GenericSoapInputField genericInput = new GenericSoapInputField(node.getNodeName(), node2.getNodeName(),
                                            isMandatory, "", null);
                                    genericInput.setxPath(xPath);
                                    genericInputs.add(genericInput);
                                }
                            }
                        }
                    }
                }
                operationsGenericInputs.put(operationName, genericInputs);
                genericInputs = new ArrayList<GenericSoapInputField>();
            }
        } catch (SOAPException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return operationsGenericInputs;
    }

    public static HashMap<String, SOAPMessage> createRequestsTemplates(String url) {
        WSDLParser parser = new WSDLParser();
        Definitions wsdl = parser.parse(url);
        StringWriter writer = new StringWriter();
        MarkupBuilder markupBuilder = new MarkupBuilder(writer);
        SOARequestCreator creator = new SOARequestCreator(wsdl, new Object(), markupBuilder);
        HashMap<String, SOAPMessage> soapMessageList = new HashMap<String, SOAPMessage>();
        for (Service service : wsdl.getServices()) {
            for (Port port : service.getPorts()) {
                Binding binding = port.getBinding();
                PortType portType = binding.getPortType();
                for (Operation op : portType.getOperations()) {
                    RequestTemplateCreator templateCreator = new RequestTemplateCreator();
                    String operationName = op.getName();
                    creator.setCreator(templateCreator);
                    creator.createRequest(port.getName(), op.getName(), binding.getName());
                    String requestBody = writer.toString();
                    SOAPMessage requestMessage = parseToSoapRequestMessage("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + requestBody);
                    soapMessageList.put(operationName, requestMessage);
                    writer.getBuffer().setLength(0);
                }
            }
        }
        return soapMessageList;
    }

    public static SOAPMessage parseToSoapRequestMessage(String xmlString) {
        try {
            MessageFactory factory = MessageFactory.newInstance();
            SOAPMessage message = factory.createMessage(
                    new MimeHeaders(),
                    new ByteArrayInputStream(xmlString.getBytes(Charset
                            .forName("UTF-8"))));
            return message;
        } catch (SOAPException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static NodeList getElementsOfTheSpecifiedClass(String typeName, String wsdlURL) {
        WSDLParser parser = new WSDLParser();
        Definitions definitions = parser
                .parse(wsdlURL);
        List<Schema> schemas = definitions.getSchemas();
        Schema schema = schemas.get(0);
        ComplexType type = schema.getComplexType(typeName);
        Document document = loadXMLDocumentFromString(type.getAsString());
        NodeList list = document.getElementsByTagName("xsd:element");
        return list;
    }

    public static Document loadXMLDocumentFromString(String xml) {
        String xmlString = "<?xml version=\"1.0\" encoding=\"utf-8\"?> \n" +
                "<xs:schema elementFormDefault=\"qualified\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\"> \n" +
                xml + "</xs:schema> ";
        System.out.println(xmlString);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document document = null;
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(xmlString));
            document = builder.parse(is);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return document;
    }

    public static HashMap<String, Boolean> getMandatoryAndOptionalOptions(String className, String wsdlURL) {
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

    public static void getOperationFields(String url, String operationName) {
        List<GenericInputList> genericInputLists = new ArrayList<GenericInputList>();
        WSDLParser parser = new WSDLParser();
        Definitions defs = parser.parse(url);

        for (PortType pt : defs.getPortTypes()) {
            System.out.println("  PortType Name: " + pt.getName());
            System.out.println("  PortType Operations: ");
            for (Operation op : pt.getOperations()) {
                if (op.getName().equals(operationName)) {//"cardOrder"
                    System.out.println("    Operation Name: " + op.getName());

                    for (Schema schema : defs.getSchemas()) {
                        List<String> schemaComplexTypes = SoapUtils.getSchemasComplexTypes(schema);

                        System.out.println("  TargetNamespace: \t" + schema.getTargetNamespace());

                        ComplexType inputComplexType = schema.getComplexType(schema.getComplexType(op.getName()).getSequence().getElements().get(0).getType().getLocalPart());

                        Derivation der = ((ComplexContent) inputComplexType.getModel()).getDerivation();
                        Sequence sequence = (Sequence) der.getModel();
                        System.out.println(der.getBase().getLocalPart() + "+++++++++++++++++++++++++++++>>>>>>>>>>" + sequence.getElements().size());
                        for (com.predic8.schema.Element e : sequence.getElements()) {
                            GenericInputList genericInput = new GenericInputList(e.getName(), e.getName(), true,
                                    true, "value");
                            if (e.getType() == null) {
                                System.out.println("    Element Name: " + e.getName() + "                 array");
                                genericInputLists.add(genericInput);
                            } else {
                                System.out.println("    Element Name: " + e.getName() + "                 " + e.getType().getLocalPart());
                                if (schemaComplexTypes.contains(e.getType().getLocalPart())) {
                                    List<GenericInputList> genericInputListList1 = new ArrayList<GenericInputList>();
                                    genericInputLists.add(getInnerComplexTypes(schema, e, schemaComplexTypes, genericInput, 0));
                                } else {
                                    genericInput.setIsList(false);
                                    genericInputLists.add(genericInput);
                                }
                            }
                        }

                    }
                }
            }
            System.out.println("");
        }
        System.out.println("genericInputs========" + genericInputLists.toString());
    }

    public static GenericInputList getInnerComplexTypes(Schema schema, com.predic8.schema.Element element, List<String> schemaComplexTypes,
                                                        GenericInputList genericInput,/* List<GenericInputList> genericInputListList,
                                            List<GenericInputList> genericInputListList1,*/ int deep) {
        String xPath = genericInput.getxPath();
        ComplexType inputComplexType = schema.getComplexType(element.getType().getLocalPart());
        Sequence sequence = null;
        try {
            Derivation der = ((ComplexContent) inputComplexType.getModel()).getDerivation();
            sequence = (Sequence) der.getModel();
        } catch (ClassCastException e) {
            sequence = inputComplexType.getSequence();
        }
        System.out.println("Sequence element size +++++++++++++++++++++++++++++>>>>>>>>>>" + sequence.getElements().size());
        for (com.predic8.schema.Element e : sequence.getElements()) {
            if (e.getType() == null) {
                System.out.println(" Inner Element Name: " + e.getName() + "                   " + "list");
                genericInput.addGenericInputIntoInnerList(new GenericInputList(xPath + "/" + e.getName(), e.getName(), true, false, "listValue"));
            } else {
                if (schemaComplexTypes.contains(e.getType().getLocalPart())) {
                    GenericInputList genericInputInner = new GenericInputList(xPath + "/" + e.getName(), e.getName(), false, false, "complexValue");
                    genericInput.addGenericInputIntoInnerList(genericInputInner);
                    getInnerComplexTypes(schema, e, schemaComplexTypes, genericInputInner, deep);
                }
                System.out.println(" Inner   Element Name: " + e.getName() + "                 " + e.getType().getLocalPart());
                int innerGenericListSize = genericInput.getChildGenericInputs().size();
                /*if(innerGenericListSize > 0) {
                    if(genericInput.getChildGenericInputs().get(innerGenericListSize - 1 ).getxPath().equals(xPath + "/" + e.getName()))
                        genericInput.addGenericInputIntoInnerList(new GenericInputList(xPath + "/" + e.getName(), e.getName(), false, false, "simpleValue"));
                }*/
                GenericInputList genericInputInner = new GenericInputList(xPath + "/" + e.getName(), e.getName(), false, false, "simpleValue");
                System.out.println("inner complex generic input ========= " + genericInputInner);
                genericInput.addGenericInputIntoInnerList(new GenericInputList(xPath + "/" + e.getName(), e.getName(), false, false, "simpleValue"));
            }
        }
        return genericInput;
    }

    public static List<String> getSchemasComplexTypes(Schema schema) {
        List<String> complexTypeNames = new ArrayList<String>();
        for (ComplexType ct : schema.getComplexTypes()) {
            System.out.println("    ComplexType Name: " + ct.getName());
            complexTypeNames.add(ct.getName());
        }
        return complexTypeNames;
    }

}
