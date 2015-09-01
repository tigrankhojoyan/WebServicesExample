package com.mkyong.sims;

import com.predic8.schema.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tigrank on 8/31/2015.
 */
public class GenericInputList {

    private String className;
    private String fieldName;
    private boolean isMandatory;
    private String value;
    private String xPath;
    private boolean isList;
    private List<String> suggestedValues;
    private List<GenericInputList> childGenericInputs;

    public GenericInputList() {
        childGenericInputs = new ArrayList<GenericInputList>();
        suggestedValues = new ArrayList<String>();
    }

    public GenericInputList(String xPath, String fieldName, Boolean isList,
                            boolean isMandatory, String value) {
        this.xPath = xPath;
        this.fieldName = fieldName;
        this.isMandatory = isMandatory;
        this.value = value;
        this.isList = isList;
        childGenericInputs = new ArrayList<GenericInputList>();
        suggestedValues = new ArrayList<String>();
    }

    public GenericInputList(String className, String fieldName, boolean isMandatory,
                            String value, String xPath, Boolean isList) {
        this.xPath = xPath;
        this.fieldName = fieldName;
        this.isMandatory = isMandatory;
        this.value = value;
        this.isList = isList;
        childGenericInputs = new ArrayList<GenericInputList>();
    }

    public void addGenericInputIntoInnerList(GenericInputList genericInput) {
        childGenericInputs.add(genericInput);
    }



    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
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

    public List<GenericInputList> getChildGenericInputs() {
        return childGenericInputs;
    }

    public void setChildGenericInputs(List<GenericInputList> childGenericInputs) {
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
                "\"className\":\"" + className +
                "\", \"fieldName\":\"" + fieldName +
                "\", \"suggestedValues\":\"" + suggestedValues +
                "\",  \"isMandatory\":\"" + isMandatory +
                "\", \"value\":\"" + value + "\", \"xPath\":\"" +
                xPath + "\", \"childGenericInputs\":" + childGenericInputs +
                ", \"isList\":\"" + isList + "\"" + '}';
    }

}