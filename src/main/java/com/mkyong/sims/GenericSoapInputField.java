package com.mkyong.sims;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tigrank on 8/31/2015.
 */
public class GenericSoapInputField {

    private String fieldName;
    private boolean isMandatory;
    private String value;
    private String xpath;
    private boolean isList;
    private List<String> suggestedValues;
    private List<GenericSoapInputField> childElements;

    public GenericSoapInputField(){
        value = "";
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public boolean isMandatory() {
        return isMandatory;
    }

    public void setIsMandatory(boolean isMandatory) {
        this.isMandatory = isMandatory;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    public boolean isList() {
        return isList;
    }

    public void setIsList(boolean isList) {
        this.isList = isList;
    }

    public List<String> getSuggestedValues() {
        return suggestedValues;
    }

    public void setSuggestedValues(List<String> suggestedValues) {
        this.suggestedValues = suggestedValues;
    }

    public List<GenericSoapInputField> getChildElements() {
        return childElements;
    }

    public void setChildElements(List<GenericSoapInputField> childElements) {
        this.childElements = childElements;
    }

    public GenericSoapInputField addChild(GenericSoapInputField childElement) {
        if (childElements == null) {
            childElements = new ArrayList<GenericSoapInputField>();
        }
        childElements.add(childElement);
        return this;
    }

    @Override
    public String toString() {
        return "{" +
                "\"fieldName\":\"" + fieldName + "\", " +
                "\"suggestedValues\":\"" + suggestedValues + "\"," +
                "\"isMandatory\":\"" + isMandatory + "\"," +
                "\"value\":\"" + value + "\"," +
                "\"xPath\":\"" + xpath + "\"," +
                "\"isList\":\"" + isList + "\"," +
                "\"childGenericInputs\": " + childElements
                + "}\n";
    }

}