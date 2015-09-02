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
    private String xPath;
    private boolean isList;
    private List<String> suggestedValues;
    private List<GenericSoapInputField> childGenericInputs;

    public GenericSoapInputField() {
        childGenericInputs = new ArrayList<GenericSoapInputField>();
        suggestedValues = new ArrayList<String>();
    }

    public GenericSoapInputField(String xPath, String fieldName, Boolean isList,
                                 boolean isMandatory, String value) {
        this.xPath = xPath;
        this.fieldName = fieldName;
        this.isMandatory = isMandatory;
        this.value = value;
        this.isList = isList;
        childGenericInputs = new ArrayList<GenericSoapInputField>();
        suggestedValues = new ArrayList<String>();
    }

    public GenericSoapInputField(String className, String fieldName, boolean isMandatory,
                                 String value, String xPath, Boolean isList) {
        this.xPath = xPath;
        this.fieldName = fieldName;
        this.isMandatory = isMandatory;
        this.value = value;
        this.isList = isList;
        childGenericInputs = new ArrayList<GenericSoapInputField>();
    }

    public void addGenericInputIntoInnerList(GenericSoapInputField genericInput) {
        childGenericInputs.add(genericInput);
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

    public String getxPath() {
        return xPath;
    }

    public void setxPath(String xPath) {
        this.xPath = xPath;
    }

    public List<String> getSuggestedValues() {
        return suggestedValues;
    }

    public void setSuggestedValues(List<String> suggestedValues) {
        this.suggestedValues = suggestedValues;
    }

    public List<GenericSoapInputField> getChildGenericInputs() {
        return childGenericInputs;
    }

    public void setChildGenericInputs(List<GenericSoapInputField> childGenericInputs) {
        this.childGenericInputs = childGenericInputs;
    }

    public boolean isList() {
        return isList;
    }

    public void setIsList(boolean isList) {
        this.isList = isList;
    }

    @Override
    public String toString() {
        return "{" +
                "\"fieldName\":\"" + fieldName +
                "\", \"suggestedValues\":\"" + suggestedValues +
                "\",  \"isMandatory\":\"" + isMandatory +
                "\", \"value\":\"" + value + "\", \"xPath\":\"" +
                xPath + "\", \"childGenericInputs\":" + childGenericInputs +
                ", \"isList\":\"" + isList + "\"" + '}';
    }

}