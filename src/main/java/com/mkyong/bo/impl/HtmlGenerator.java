package com.mkyong.bo.impl;

import javax.xml.soap.SOAPMessage;
import java.util.*;

/**
 * Created by tigrank on 8/25/2015.
 */
public class HtmlGenerator {

    private static HtmlGenerator instance = null;
    private static List<GenericInput> genericInputs = null;
    private static List<String> titles = new ArrayList<String>();
    private static SOAPMessage soapRequest = null;
    private static SOAPMessage soapResponse = null;

    private HtmlGenerator() {

    }

    public static HtmlGenerator getInstance() {
        if(null == instance) {
            instance = new HtmlGenerator();
        }
        return instance;
    }

    public static void initializeTitle() {
        for(GenericInput genericInput: genericInputs) {
            if(!titles.contains(genericInput.getHtmlTitle())) {
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

    public static String generateHTMLForGivenGenericList(String htmlTitle) {
        String htmlStringWithoutSelector = " <p>\n" +
                "%s                    <input name=\"%s.%s\"\n" +
                "                           id=\"%s.%s\" type=\"text\"\n" +
                "                           value=\"%s\"/>\n" +
                "                </p>";
        String htmlFormFields = "";
        for(GenericInput genericInput:genericInputs) {
            if(genericInput.getHtmlTitle().equals(htmlTitle)) {
                String htmlBody =  String.format(htmlStringWithoutSelector, genericInput.getFieldName(), genericInput.getClassName(), genericInput.getFieldName(),
                        genericInput.getClassName(), genericInput.getFieldName(), genericInput.getValue());
                htmlFormFields += htmlBody;
            }
        }
        return htmlFormFields;
    }

    public static Map<String, String> generateEntireHTMLForms() {
        Map<String, String> formBodyByTitles = new HashMap<String, String>();
        List<String> drownTitles = new ArrayList<String>();
        for(String title : titles) {
           /* if(! drownTitles.contains(title))  {
                drownTitles.add(title);*/
                //formBodyByTitles.put("The" + title, "<h1>" + title + "</h1>");
            //}
            formBodyByTitles.put(title, generateHTMLForGivenGenericList(title));
        }
 /*       System.out.println("formBody=========\n");
        WSDLUtil.printMap(formBodyByTitles);
        System.out.println("kkkkkkkkkkkkkk");*/
        System.out.println("formBodyTitle========" + formBodyByTitles);
        return formBodyByTitles;
    }

    public static List<GenericInput> getGenericInputs() {
        return genericInputs;
    }

    public static void setGenericInputs(List<GenericInput> genericInputs) {
        instance.genericInputs = genericInputs;
    }

    public static List<String> getTitles() {
        return titles;
    }

    public static void setTitles(List<String> titles) {
        instance.titles = titles;
    }

    public static SOAPMessage getSoapRequest() {
        return soapRequest;
    }

    public static void setSoapRequest(SOAPMessage soapRequest) {
        instance.soapRequest = soapRequest;
    }

    public static SOAPMessage getSoapResponse() {
        return soapResponse;
    }

    public static void setSoapResponse(SOAPMessage soapResponse) {
        instance.soapResponse = soapResponse;
    }
}
