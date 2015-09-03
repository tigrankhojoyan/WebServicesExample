package com.mkyong.sims;

import com.predic8.schema.*;
import com.predic8.wsdl.Definitions;
import com.predic8.wsdl.Operation;
import com.predic8.wsdl.PortType;
import com.predic8.wsdl.WSDLParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public abstract class SoapUtils {

    public static HashMap<String, GenericSoapInputField> getOperationFields(String url) {
        HashMap<String, GenericSoapInputField> operationsGenericInputs = new HashMap<String, GenericSoapInputField>();
        WSDLParser parser = new WSDLParser();
        Definitions definitions = parser.parse(url);
        for (PortType portType : definitions.getPortTypes()) {
            for (Operation operation : portType.getOperations()) {
                GenericSoapInputField operationInput = new GenericSoapInputField();
                operationInput.setFieldName(operation.getName());
                for (Schema schema : definitions.getSchemas()) {
                    ComplexType parentComplexType = schema.getComplexType(schema.getComplexType(operation.getName()).getSequence().getElements().get(0).getType().getLocalPart());
                    GenericSoapInputField parentInput = new GenericSoapInputField();
                    parentInput.setXpath(parentComplexType.getName());
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
        List<GenericSoapInputField> childGenericInputs = genericInput.getChildElements();
        Set<String> defaultValuesFiledNames = defaultValues.keySet();
        for (GenericSoapInputField childGenericInput : childGenericInputs) {
            String fieldName = childGenericInput.getFieldName();
            if (defaultValuesFiledNames.contains(fieldName)) {
                childGenericInput.setValue(defaultValues.get(fieldName));
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
