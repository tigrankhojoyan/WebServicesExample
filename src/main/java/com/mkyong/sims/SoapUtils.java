package com.mkyong.sims;

import com.predic8.schema.ComplexContent;
import com.predic8.schema.ComplexType;
import com.predic8.schema.Derivation;
import com.predic8.schema.Element;
import com.predic8.schema.Schema;
import com.predic8.schema.Sequence;
import com.predic8.wsdl.Definitions;
import com.predic8.wsdl.Operation;
import com.predic8.wsdl.PortType;
import com.predic8.wstool.creator.RequestCreator;
import com.predic8.wstool.creator.SOARequestCreator;
import groovy.xml.MarkupBuilder;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class SoapUtils {

    public static String generateSOAPEnvelop(HttpServletRequest request) {
        Definitions wsdl = OperationsContainer.getDefinitions();

        StringWriter writer = new StringWriter();

        Map<String, String[]> requestParams = request.getParameterMap();
        Map<String, String> formParams = getFormParams(requestParams);

        SOARequestCreator creator = new SOARequestCreator(wsdl, new RequestCreator(), new MarkupBuilder(writer));
        creator.setFormParams(formParams);

        creator.createRequest(request.getParameter("wsdlPortTypeName"), request.getParameter("wsdlOperationName"), request.getParameter("wsdlBindingName"));

        return correctSoapEnvelopXml(writer.toString());
    }

    private static String correctSoapEnvelopXml(String envelopXml) {
        return envelopXml.replaceAll("ns[1-9]", "urn").replaceAll("s11", "soapenv").replaceAll(" *\\<.*\\/\\>\\n", "");
    }

    private static Map<String, String> getFormParams(Map<String, String[]> requestParams) {
        Map<String, String> formParams = new HashMap<String, String>();
        for (Map.Entry<String, String[]> entry : requestParams.entrySet()) {
            String xpath = "xpath:";
            if (entry.getKey().startsWith(xpath) && StringUtils.isNotEmpty(entry.getValue()[0])) {
                formParams.put(entry.getKey(), entry.getValue()[0]);
            }
        }
        return formParams;
    }

    public static HashMap<String, GenericSoapInputField> getOperationFields(Definitions definitions) {
        HashMap<String, GenericSoapInputField> operationsGenericInputs = new HashMap<String, GenericSoapInputField>();
        for (PortType portType : definitions.getPortTypes()) {
            for (Operation operation : portType.getOperations()) {
                GenericSoapInputField operationInput = new GenericSoapInputField();
                operationInput.setFieldName(operation.getName());
                for (Schema schema : definitions.getSchemas()) {
                    GenericSoapInputField parentInput = new GenericSoapInputField();
                    Element operationInputElement = schema.getComplexType(operation.getName()).getSequence().getElements().get(0);
                    ComplexType parentComplexType = schema.getComplexType(operationInputElement.getType().getLocalPart());
                    parentInput.setXpath("xpath:/" + operation.getName() + "/" + operationInputElement.getName());
                    parentInput.setIsMandatory(true);
                    parentInput.setFieldName(parentComplexType.getName());
                    operationInput.addChild(parentInput);
                    addChildElements(parentComplexType, parentInput);
                }
                operationsGenericInputs.put(operation.getName(), operationInput);
            }
        }
        return operationsGenericInputs;
    }

    public static void setDefaultValuesOfOperation(GenericSoapInputField genericInput, HashMap<String, String> defaultValues) {
        List<GenericSoapInputField> childGenericInputs = genericInput.getChildElements().get(0).getChildElements();
        Set<String> defaultValuesFiledNames = defaultValues.keySet();
        for (GenericSoapInputField childGenericInput : childGenericInputs) {
            String fieldName = childGenericInput.getFieldName();
            if (defaultValuesFiledNames.contains(fieldName)) {
                childGenericInput.setValue(defaultValues.get(fieldName));
            }
        }
    }

    public static void setSuggestedValues(HashMap<String, List<String>> fieldSuggestedValues, GenericSoapInputField genericSoapInputField) {
        List<GenericSoapInputField> genericSoapInputFields = genericSoapInputField.getChildElements().get(0).getChildElements();
        for (int i = 0; i < genericSoapInputFields.size(); i++) {
            if (fieldSuggestedValues.containsKey(genericSoapInputFields.get(i).getFieldName())) {
                genericSoapInputFields.get(i).setSuggestedValues(fieldSuggestedValues.get(genericSoapInputFields.get(i).getFieldName()));
            }
        }
    }

    public static void addChildElements(ComplexType parentComplexType, GenericSoapInputField parentGenericInput) {

        Sequence sequence = getSequence(parentComplexType);

        for (Element element : sequence.getElements()) {
            GenericSoapInputField child = new GenericSoapInputField();
            child.setXpath(parentGenericInput.getXpath() + "/" + element.getName());
            child.setIsMandatory(isElementMandatory(element));
            child.setIsList(isElementList(element));
            child.setFieldName(element.getName());
            parentGenericInput.addChild(child);
            if (isElementComplex(element)) {
                addChildElements(getComplexType(element), child);
            }
        }
    }

    public static ComplexType getComplexType(Element element) {
        ComplexType complexType;
        if (isAnonymous(element)) {
            complexType = (ComplexType) element.getEmbeddedType();
        } else {
            complexType = element.getSchema().getComplexType(element.getType().getLocalPart());
        }
        return complexType;
    }

    public static Sequence getSequence(ComplexType complexType) {
        Sequence sequence;
        if (isAnonymous(complexType)) {
            Derivation der = ((ComplexContent) complexType.getModel()).getDerivation();
            sequence = (Sequence) der.getModel();
        } else {
            sequence = complexType.getSequence();
        }
        return sequence;
    }

    public static boolean isElementSimple(Element element) {
        return element.getType() != null && element.getType().getPrefix().equals("xs");
    }

    public static boolean isElementComplex(Element element) {
        return !isElementSimple(element);
    }

    public static boolean isAnonymous(ComplexType complexType) {
        return complexType.getSequence() == null;
    }

    public static boolean isAnonymous(Element element) {
        return element.getEmbeddedType() != null && element.getType() == null;
    }

    public static Boolean isElementList(Element element) {
        return element.getMaxOccurs() != null && element.getMaxOccurs().equals("unbounded");
    }

    public static Boolean isElementMandatory(Element element) {
        return !element.getMinOccurs().equals("0");
    }
}


