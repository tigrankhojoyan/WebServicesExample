package com.mkyong.sims;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tigrank on 9/1/2015.
 */
public final class SimsHTMLGenerator {

    private static String fieldTemplate = " <label for=\"%s\">%s</label>\n" + //fieldName, fieldName
            "                    <input name=\"%s\"\n" + //xPath
            "                           id=\"%s\" type=\"text\" value=\"%s\"/>"; // xPAth, default value

    private static String selectorTemplate = "<select name=\"%s\" onclick=\"assignField('%s', this.value);\" class=\"combo_list\">\n" + //fieldName, xPath
            "\t\t\t\t\t\t%s\n" + //selectors options in HTML format
            "\t\t\t\t\t</select>";


    public static String generateHTMLFromGenericInput(GenericInputList genericInput) {
        String htmlForm = " <div class=\"dropdown\"><a href=\"#\" class=\"dropdown_header\">basic data</a>\n" +
                "\n" +
                "        <div class=\"dropdown_content\">\n" +
                "            <fieldset>";
        List<String> complexTypesHTMLs = new ArrayList<String>();
        List<GenericInputList> genericInputList = genericInput.getChildGenericInputs();
        int complexTypesIndex = 0;
        for (GenericInputList tempGenericInput : genericInputList) {
            if (tempGenericInput.getChildGenericInputs().size() == 0) {
                htmlForm += generateHTMLFieldForSimpleInput(genericInput);
            } else {
                List<GenericInputList> complexGenericInputs = tempGenericInput.getChildGenericInputs();
                for(GenericInputList complexGenericInput: complexGenericInputs) {
                    String complexTypeHTMLForm = " <div class=\"dropdown\"><a href=\"#\" class=\"dropdown_header\">" +
                            tempGenericInput.getFieldName() + "</a>\n" + "\n" +
                            "        <div class=\"dropdown_content\">\n";
                    String tempComplexHTML = generateHTMLFieldForComplexInput(complexGenericInput, complexTypeHTMLForm, 0);
                    complexTypesHTMLs.add(tempComplexHTML);
                }
            }
        }
        for(String complexTypeHTML: complexTypesHTMLs) {
            htmlForm += complexTypeHTML;
        }
        htmlForm += "</fieldset>";
        return htmlForm;
    }

    public static String generateHTMLFieldForComplexInput(GenericInputList genericInput, String htmlForm, int counter) {
        List<GenericInputList> genericInputList = genericInput.getChildGenericInputs();

        for(GenericInputList tempGenericInput: genericInputList) {
            if(tempGenericInput.getChildGenericInputs().size() > 0) {
                counter++;
                htmlForm += "<fieldset><legend>" + tempGenericInput.getFieldName() + "</legend>";
                generateHTMLFieldForComplexInput(tempGenericInput, htmlForm, counter);
            }
            if(counter > 1) {
                counter = 0;
                htmlForm +="<fieldset>";
            }
            htmlForm += generateHTMLFieldForSimpleInput(tempGenericInput);
        }
        return htmlForm;
    }

    public static String generateHTMLFieldForSimpleInput(GenericInputList genericInput) {
        List<String> suggestedValues = genericInput.getSuggestedValues();
        String selectorOptions = "";
        if (suggestedValues.size() > 0) {
            for (String selectorValue : suggestedValues)
                selectorOptions += "<option value=\"" + selectorValue + "\"> " +
                        selectorValue + "</option>";
        }
        String selectorHTML = String.format(selectorTemplate, genericInput.getFieldName(), genericInput.getxPath(), selectorOptions);
        String fieldForm = String.format(fieldTemplate, genericInput.getFieldName(), genericInput.getFieldName(),
                genericInput.getxPath(), genericInput.getxPath(), genericInput.getValue());
        if(selectorOptions.length() > 1) {
            fieldForm = "<p>"  + fieldForm + "<img src=\"/images/arrow-left-black.png\" class=\"combo_img\" /> " +
                    selectorHTML + "<p/>";
        } else {
            fieldForm = "<p>"  + fieldForm + "</p>";
        }
        return fieldForm;
    }
}
