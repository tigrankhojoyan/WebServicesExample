package com.mkyong.bo.impl;

import javax.xml.soap.SOAPMessage;
import java.util.*;

/**
 * Created by tigrank on 8/25/2015.
 */
public class HtmlGenerator {


    private  List<GenericInput> genericInputs = null;
    private  List<String> titles = new ArrayList<String>();
    private  SOAPMessage soapRequest = null;
    private  SOAPMessage soapResponse = null;

    public HtmlGenerator() {

    }

    public  void initializeTitle() {
        for (GenericInput genericInput : genericInputs) {
            if (!titles.contains(genericInput.getHtmlTitle())) {
                titles.add(genericInput.getHtmlTitle());
            }
        }
    }

    /*public HtmlGenerator(List<GenericInput> genericInputs) {
        this.genericInputs = genericInputs;
        for(GenericInput genericInput: genericInputs) {
            if(!titles.contains(genericInput.getHtmlTitle())) {
                titles.add(genericInput.getHtmlTitle());
            }
        }
    }*/

    public  String generateHTMLForGivenGenericList(String htmlTitle) {
        String htmlStringWithoutSelector = " <p>\n" +
                "%s                    <input name=\"%s\"\n" +
                "                           id=\"%s\" type=\"text\"\n" +
                "                           value=\"%s\"/>\n" +
                "                </p>";
        String htmlFormFields = "";
        for (GenericInput genericInput : genericInputs) {
            if (genericInput.getHtmlTitle().equals(htmlTitle)) {
                String htmlBody = String.format(htmlStringWithoutSelector, genericInput.getFieldName(), genericInput.getxPath(),
                        genericInput.getxPath(), genericInput.getValue());
                htmlFormFields += htmlBody;
            }
        }
        return htmlFormFields;
    }

    public  Map<String, String> generateEntireHTMLForms() {
        Map<String, String> formBodyByTitles = new HashMap<String, String>();
        List<String> drownTitles = new ArrayList<String>();
        for (String title : titles) {
           /* if(! drownTitles.contains(title))  {
                drownTitles.add(title);*/
            //formBodyByTitles.put("The" + title, "<h1>" + title + "</h1>");
            //}
            formBodyByTitles.put(title, generateHTMLForGivenGenericList(title));
        }
 /*       System.out.println("formBody=========\n");
        WSDLUtil.printMap(formBodyByTitles);
        System.out.println("kkkkkkkkkkkkkk");*/
        //System.out.println("formBodyTitle========" + formBodyByTitles);
        return formBodyByTitles;
    }

    public void setValuesOfGenericInput(HashMap<String, String> valuesList) {
        for (GenericInput genericInput : genericInputs) {
            String fieldName = genericInput.getFieldName().substring(4, genericInput.getFieldName().length());
            if (valuesList.containsKey(fieldName)) {
                genericInput.setValue(valuesList.get(fieldName));
            }
        }
    }

    public List<GenericInput> getGenericInputs() {
        return genericInputs;
    }

    public void setGenericInputs(List<GenericInput> genericInputs) {
        this.genericInputs = genericInputs;
    }

    public List<String> getTitles() {
        return titles;
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }

    public SOAPMessage getSoapRequest() {
        return soapRequest;
    }

    public void setSoapRequest(SOAPMessage soapRequest) {
        this.soapRequest = soapRequest;
    }

    public SOAPMessage getSoapResponse() {
        return soapResponse;
    }

    public void setSoapResponse(SOAPMessage soapResponse) {
        this.soapResponse = soapResponse;
    }
}
