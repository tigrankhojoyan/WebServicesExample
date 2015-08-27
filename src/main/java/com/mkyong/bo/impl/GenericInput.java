package com.mkyong.bo.impl;

/**
 * Created by tigrank on 8/25/2015.
 */
public class GenericInput {

    private String className;
    private String fieldName;
    private String htmlTitle;//
    private boolean isMandatory;
    private String value;
    private String xPath;

    public GenericInput() {

    }

    public GenericInput(String className, String fieldName, String htmlTitle,
                        boolean isMandatory, String value) {
        this.className = className;
        this.fieldName = fieldName;
        this.htmlTitle = htmlTitle;
        this.isMandatory = isMandatory;
        this.value = value;
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

    public String getHtmlTitle() {
        return htmlTitle;
    }

    public void setHtmlTitle(String htmlTitle) {
        this.htmlTitle = htmlTitle;
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

    @Override
    public String toString() {
        return "{" +
                "\"className\":\"" + className +
                "\", \"fieldName\":\"" + fieldName +
                "\", \"htmlTitle\":\"" + htmlTitle +
                "\", \"isMandatory\":\"" + isMandatory +
                "\", \"value\":\"" + value + "\", \"xPath\":\"" +
                xPath + "\"" + '}';
    }
}
