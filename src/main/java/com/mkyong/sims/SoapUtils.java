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

    public static HashMap<String, GenericInputList> getOperationFields(String url) {
        HashMap<String, GenericInputList> operationsGenericInputs = new HashMap<String, GenericInputList>();
        WSDLParser parser = new WSDLParser();
        Definitions definitions = parser.parse(url);
        for (PortType portType : definitions.getPortTypes()) {
            for (Operation operation : portType.getOperations()) {
                GenericInputList genericInputLists = new GenericInputList();
                genericInputLists.setClassName(operation.getName() + "Input");
                genericInputLists.setFieldName(operation.getName());
                for (Schema schema : definitions.getSchemas()) {
                    List<String> schemaComplexTypes = SoapUtils.getSchemasComplexTypes(schema);
                    ComplexType inputComplexType = schema.getComplexType(schema.getComplexType(operation.getName()).getSequence().getElements().get(0).getType().getLocalPart());
                    Derivation der = ((ComplexContent) inputComplexType.getModel()).getDerivation();
                    Sequence sequence = (Sequence) der.getModel();
                    for (com.predic8.schema.Element element : sequence.getElements()) {
                        GenericInputList genericInput = new GenericInputList(element.getName(), element.getName(), true,
                                isElementMandatory(element), "value");
                        if (element.getType() == null) {
                            modifyGenericInputIfDataIsAnonymous(genericInput, element);
                            genericInputLists.addGenericInputIntoInnerList(genericInput);
                        } else {
                            if (schemaComplexTypes.contains(element.getType().getLocalPart())) {
                                genericInputLists.addGenericInputIntoInnerList(getInnerComplexTypes(schema, element, schemaComplexTypes, genericInput));
                            } else {
                                genericInput.setIsList(false);
                                //genericInput = modifyGenericInputIfDataIsAnonymous(genericInput, element);
                                genericInputLists.addGenericInputIntoInnerList(genericInput);
                            }
                        }
                    }
                    operationsGenericInputs.put(operation.getName(), genericInputLists);
                }
            }
        }
        for(String operationName: operationsGenericInputs.keySet()) {
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

    public static GenericInputList getInnerComplexTypes(Schema schema, com.predic8.schema.Element element, List<String> schemaComplexTypes,
                                                        GenericInputList genericInput) {
        String xPath = genericInput.getxPath();
        ComplexType inputComplexType = schema.getComplexType(element.getType().getLocalPart());
        Sequence sequence = null;
        try {
            Derivation der = ((ComplexContent) inputComplexType.getModel()).getDerivation();
            sequence = (Sequence) der.getModel();
        } catch (ClassCastException e) {
            sequence = inputComplexType.getSequence();
        }
        for (com.predic8.schema.Element sequenceElement : sequence.getElements()) {
            if (sequenceElement.getType() == null) {
                GenericInputList arrayGenericInput = new GenericInputList(xPath + "/" + sequenceElement.getName(), sequenceElement.getName(), true, false, "listValue");
                modifyGenericInputIfDataIsAnonymous(arrayGenericInput, sequenceElement);
                genericInput.addGenericInputIntoInnerList(arrayGenericInput);
            } else {
                if (schemaComplexTypes.contains(sequenceElement.getType().getLocalPart())) {
                    GenericInputList genericInputInner = new GenericInputList(xPath + "/" + sequenceElement.getName(), sequenceElement.getName(), false, false, "complexValue");
                    /*GenericInputList genericInputInner1 = modifyGenericInputIfDataIsAnonymous(new GenericInputList(xPath + "/" + sequenceElement.getName(), sequenceElement.getName(), false, false, "complexValue"), sequenceElement);*/
                    genericInput.addGenericInputIntoInnerList(genericInputInner);
                    getInnerComplexTypes(schema, sequenceElement, schemaComplexTypes, genericInputInner);
                }
                int genericInputChildesCount = genericInput.getChildGenericInputs().size();
                GenericInputList genericInputInner = new GenericInputList(xPath + "/" + sequenceElement.getName(), sequenceElement.getName(), false, false, "simpleValue");
                if (genericInputChildesCount > 0) {
                    GenericInputList lastAddedElement = genericInput.getChildGenericInputs().get(genericInput.getChildGenericInputs().size() - 1);
                    if (!lastAddedElement.getxPath().equals(genericInputInner.getxPath())) {
                        modifyGenericInputIfDataIsAnonymous(genericInputInner, sequenceElement);
                        genericInput.addGenericInputIntoInnerList(genericInputInner);
                    }
                } else {
                    //genericInputInner = modifyGenericInputIfDataIsAnonymous(genericInputInner,sequenceElement);
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

    public static void setDefaultValuesOfOperation(GenericInputList genericInput, HashMap <String, String> defaultValues) {
        List<GenericInputList> childGenericInputs = genericInput.getChildGenericInputs();
        Set<String> defaultValuesFiledNames = defaultValues.keySet();
        for(GenericInputList childGenericInput: childGenericInputs) {
            String fieldName = childGenericInput.getFieldName();
            if(defaultValuesFiledNames.contains(fieldName)) {
                childGenericInput.setValue(defaultValues.get(fieldName));
            }
        }
    }

    public static void modifyGenericInputIfDataIsAnonymous(GenericInputList genericInput, com.predic8.schema.Element element) {
        System.out.println("In modify function \n field name===========" + genericInput.getFieldName());
        if(genericInput.getFieldName().equals("riskManagementCustomDataList")) {
            System.out.println("========in modify function===========");
        }
        if(genericInput.isList()) {
            ComplexType complexType =(ComplexType)element.getEmbeddedType();
            if(complexType.getSequence().getElements().size() == 1) {
                Sequence sequence = complexType.getSequence();
                Element element1 = sequence.getElements().get(0);
                GenericInputList genericInput1 = new GenericInputList(genericInput.getxPath() + "/" + element1.getName(),
                        element1.getName(), true, isElementMandatory(element1), "modifiedValue");
                genericInput.addGenericInputIntoInnerList(genericInput1);
            } else {
                System.out.println(" the complex type is null ");
            }
        }
    }

}
