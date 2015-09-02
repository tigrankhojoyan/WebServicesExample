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
                GenericSoapInputField genericInputLists = new GenericSoapInputField();
                genericInputLists.setFieldName(operation.getName());
                for (Schema schema : definitions.getSchemas()) {
                    List<String> schemaComplexTypes = SoapUtils.getSchemasComplexTypes(schema);
                    ComplexType inputComplexType = schema.getComplexType(schema.getComplexType(operation.getName()).getSequence().getElements().get(0).getType().getLocalPart());
                    Derivation der = ((ComplexContent) inputComplexType.getModel()).getDerivation();
                    Sequence sequence = (Sequence) der.getModel();
                    for (Element element : sequence.getElements()) {
                        GenericSoapInputField genericInput = new GenericSoapInputField(element.getName(), element.getName(), isElementList(element),
                                isElementMandatory(element), "");
                        if (element.getType() == null) {
                            modifyGenericInputIfDataIsAnonymous(genericInput, element, schema);
                            genericInputLists.addGenericInputIntoInnerList(genericInput);
                        } else {
                            if (schemaComplexTypes.contains(element.getType().getLocalPart())) {
                                genericInputLists.addGenericInputIntoInnerList(getInnerComplexTypes(schema, element, schemaComplexTypes, genericInput));
                            } else {
                                genericInput.setIsList(false);
                                genericInputLists.addGenericInputIntoInnerList(genericInput);
                            }
                        }
                    }
                    operationsGenericInputs.put(operation.getName(), genericInputLists);
                }
            }
        }
        for (String operationName : operationsGenericInputs.keySet()) {
            System.out.println("000000000\noperation=======" + operationName + "\n operation generic input=======" +
                    operationsGenericInputs.get(operationName) + "\n 00000000");
        }
        return operationsGenericInputs;
    }

    public static Boolean isElementMandatory(com.predic8.schema.Element element) {
        if (element.getMinOccurs().equals("0"))
            return false;
        return true;
    }

    public static GenericSoapInputField getInnerComplexTypes(Schema schema, com.predic8.schema.Element element, List<String> schemaComplexTypes,
                                                             GenericSoapInputField genericInput) {
        String xPath = genericInput.getxPath();
        ComplexType inputComplexType = schema.getComplexType(element.getType().getLocalPart());
        Sequence sequence = null;
        try {
            Derivation der = ((ComplexContent) inputComplexType.getModel()).getDerivation();
            sequence = (Sequence) der.getModel();
        } catch (ClassCastException e) {
            sequence = inputComplexType.getSequence();
        }
        for (Element sequenceElement : sequence.getElements()) {
            if (sequenceElement.getType() == null) {
                GenericSoapInputField arrayGenericInput = new GenericSoapInputField(xPath + "/" + sequenceElement.getName(), sequenceElement.getName(), isElementList(element), false, "");
                modifyGenericInputIfDataIsAnonymous(arrayGenericInput, sequenceElement, schema);
                genericInput.addGenericInputIntoInnerList(arrayGenericInput);
            } else {
                if (schemaComplexTypes.contains(sequenceElement.getType().getLocalPart())) {
                    GenericSoapInputField genericInputInner = new GenericSoapInputField(xPath + "/" + sequenceElement.getName(), sequenceElement.getName(), isElementList(element), false, "complexValue");
                    genericInput.addGenericInputIntoInnerList(genericInputInner);
                    getInnerComplexTypes(schema, sequenceElement, schemaComplexTypes, genericInputInner);
                }
                int genericInputChildesCount = genericInput.getChildGenericInputs().size();
                GenericSoapInputField genericInputInner = new GenericSoapInputField(xPath + "/" + sequenceElement.getName(), sequenceElement.getName(), isElementList(element), false, "simpleValue");
                if (genericInputChildesCount > 0) {
                    GenericSoapInputField lastAddedElement = genericInput.getChildGenericInputs().get(genericInput.getChildGenericInputs().size() - 1);
                    if (!lastAddedElement.getxPath().equals(genericInputInner.getxPath())) {
                        modifyGenericInputIfDataIsAnonymous(genericInputInner, sequenceElement, schema);
                        genericInput.addGenericInputIntoInnerList(genericInputInner);
                    }
                } else {
                    genericInput.addGenericInputIntoInnerList(genericInputInner);
                }
            }
        }
        return genericInput;
    }

    public static List<String> getSchemasComplexTypes(Schema schema) {
        List<String> complexTypeNames = new ArrayList<String>();
        for (ComplexType ct : schema.getComplexTypes()) {
            complexTypeNames.add(ct.getName());
        }
        return complexTypeNames;
    }

    public static void setDefaultValuesOfOperation(GenericSoapInputField genericInput, HashMap<String, String> defaultValues) {
        List<GenericSoapInputField> childGenericInputs = genericInput.getChildGenericInputs();
        Set<String> defaultValuesFiledNames = defaultValues.keySet();
        for (GenericSoapInputField childGenericInput : childGenericInputs) {
            String fieldName = childGenericInput.getFieldName();
            if (defaultValuesFiledNames.contains(fieldName)) {
                childGenericInput.setValue(defaultValues.get(fieldName));
            }
        }
    }

    public static void modifyGenericInputIfDataIsAnonymous(GenericSoapInputField genericInput,
                                                           Element element, Schema schema) {
        if (element.getEmbeddedType() != null) {
            ComplexType complexType = (ComplexType) element.getEmbeddedType();
            if (complexType.getSequence().getElements().size() == 1) {
                Sequence sequence = complexType.getSequence();
                Element element1 = sequence.getElements().get(0);
                ComplexType inputComplexType = null;
                try {
                    inputComplexType = schema.getComplexType(element1.getType().getLocalPart());
                    sequence = inputComplexType.getSequence();
                    GenericSoapInputField innerGenericInput = new GenericSoapInputField(genericInput.getxPath() + "/" + element1.getName(),
                            element1.getName(), isElementList(element1), isElementMandatory(element1), "");
                    for (Element elem : sequence.getElements()) {
                        GenericSoapInputField genericInput1 = new GenericSoapInputField(genericInput.getxPath() + "/" + element1.getName() +
                                "/" + elem.getName(), elem.getName(), isElementList(elem), isElementMandatory(elem), "");
                        innerGenericInput.addGenericInputIntoInnerList(genericInput1);
                    }
                    genericInput.addGenericInputIntoInnerList(innerGenericInput);
                } catch (Exception e) {
                    GenericSoapInputField genericInput1 = new GenericSoapInputField(genericInput.getxPath() + "/" + element1.getName(),
                            element1.getName(), isElementList(element1), isElementMandatory(element1), "");
                    genericInput.addGenericInputIntoInnerList(genericInput1);
                }
            }
        }
    }

    public static Boolean isElementList(Element element) {
        if (element.getType() == null) {
            return true;
        }
        return false;
    }

}
