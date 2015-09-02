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

    private static String addItemButton = "<div class=\"info\"><input type=\"button\" style=\"padding:0px;margin:0px;font-size=1.2em;\" onclick=\"javascript:addItemRMCD();\" value=\"Add Item\" /></div>\n" +
            "<input type=\"hidden\" id=\"nbOfItemRMCD\" name=\"nbOfItemRMCD\" value=\"0\" /><!-- not used -->\n" +
            "<div id=\"listItemRMCD\"></div>";


    public static String generateHTMLFromGenericInput(GenericSoapInputField genericInput) {
        String htmlForm = " <div class=\"dropdown\"><a href=\"#\" class=\"dropdown_header\">basic data</a>\n" +
                "\n" +
                "        <div class=\"dropdown_content\">\n" +
                "            <fieldset>";
        List<String> complexTypesHTMLs = new ArrayList<String>();
        List<GenericSoapInputField> genericInputList = genericInput.getChildGenericInputs();
        for (GenericSoapInputField tempGenericInput : genericInputList) {
            if (tempGenericInput.getChildGenericInputs().size() == 0) {
                htmlForm += generateHTMLFieldForSimpleInput(tempGenericInput) + "\n";
            } else {
                String complexTypeHTMLForm = " <div class=\"dropdown\"><a href=\"#\" class=\"dropdown_header\">" +
                        tempGenericInput.getFieldName() + "</a>\n" + "\n" +
                        "        <div class=\"dropdown_content\">\n <fieldset>\n";
                String tempComplexHTML = generateHTMLFieldForComplexInput(tempGenericInput, complexTypeHTMLForm, 0);
                if(tempGenericInput.isList()) {
                    tempComplexHTML += addItemButton;
                }
                tempComplexHTML += "</fieldset></div></div>";
                complexTypesHTMLs.add(tempComplexHTML);
            }
        }
        htmlForm += "</fieldset></div></div>";
        for (String complexTypeHTML : complexTypesHTMLs) {
            htmlForm += complexTypeHTML;
        }

        return htmlForm;
    }

    public static String generateHTMLFieldForComplexInput(GenericSoapInputField genericSoapInputField, String htmlForm, int counter) {
        List<GenericSoapInputField> genericSoapInputFields = genericSoapInputField.getChildGenericInputs();
        System.out.println("the gen input list =======" + genericSoapInputFields.toString());
        for (GenericSoapInputField tempGenericInput : genericSoapInputFields) {
            if (tempGenericInput.getChildGenericInputs().size() > 0) {
                htmlForm += "<fieldset><legend>" + tempGenericInput.getFieldName() + "</legend>";
                for(GenericSoapInputField tempGenericInnerInput: tempGenericInput.getChildGenericInputs()) {
                    if(tempGenericInnerInput.getChildGenericInputs().size() > 0) {
                        for (GenericSoapInputField temp : tempGenericInnerInput.getChildGenericInputs()) {
                            htmlForm +=generateHTMLFieldForSimpleInput(temp);
                        }
                    }
                    if(tempGenericInnerInput.isList()) {
                        htmlForm += addItemButton;
                    }
                    htmlForm += generateHTMLFieldForSimpleInput(tempGenericInnerInput) + "\n";
                }
                if(tempGenericInput.isList()) {
                    htmlForm += addItemButton;
                }
                htmlForm += "</fieldset>";
//                generateHTMLFieldForComplexInput(genericSoapInputFields, htmlForm, counter);
            } else {
                if(tempGenericInput.isList()) {
                    htmlForm += addItemButton;
                }
                htmlForm += generateHTMLFieldForSimpleInput(tempGenericInput) + "\n";
            }
        }
        return htmlForm;
    }

    public static String generateHTMLFieldForSimpleInput(GenericSoapInputField genericInput) {
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
        if (selectorOptions.length() > 1) {
            fieldForm = "<p>" + fieldForm + "<img src=\"/images/arrow-left-black.png\" class=\"combo_img\" /> " +
                    selectorHTML + "</p>";
        } else {
            fieldForm = "<p>" + fieldForm + "</p>";
        }
        if(genericInput.isMandatory()) {
            fieldForm = fieldForm.replace("<p>", "<p class=\"mandatory\">");
        }
        return fieldForm;
    }

}
