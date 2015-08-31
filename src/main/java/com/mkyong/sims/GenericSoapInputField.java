package com.mkyong.sims;

import java.util.List;

public class GenericSoapInputField {
    private String className;
    private String fieldName;
    private boolean isMandatory;
    private String value;
    private String xPath;
    private List<String> suggestedValues;
    private List<GenericSoapInputField> childFields;
    private String imagePath;

    public GenericSoapInputField() {

    }

    public GenericSoapInputField(String className, String fieldName,
                                 boolean isMandatory, String value, List<String> suggestedValues) {
        this.className = className;
        this.fieldName = fieldName;
        this.isMandatory = isMandatory;
        this.value = value;
        this.suggestedValues = suggestedValues;
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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return "{" +
                "\"className\":\"" + className +
                "\", \"fieldName\":\"" + fieldName +
                "\", \"suggestedValues\":\"" + suggestedValues +
                "\", \"isMandatory\":\"" + isMandatory +
                "\", \"value\":\"" + value + "\", \"xPath\":\"" +
                xPath + "\", \"imagePath\": \"" + imagePath + "\"" + '}';
    }

}
