package com.mkyong.sims;

import java.util.ArrayList;
import java.util.HashMap;
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

    private static String addItemButton = "<div class=\"info\"><input type=\"button\" style=\"padding:0px;margin:0px;font-size=1.2em;\" onclick=\"javascript:addItem(%s);\" value=\"Add Item\" /></div>\n" +
            "<input type=\"hidden\" id=\"nbOfItemRMCD\" name=\"nbOfItemRMCD\" value=\"0\" />\n" +
            "<div id=\"listItemRMCD\"></div>";

    private static HashMap<String, GenericSoapInputField> listItems = new HashMap<String, GenericSoapInputField>();
    private static OperationsContainer operationsContainer = OperationsContainer.getInstance();
    private static HashMap<String, String> listItemsHTMLs = new HashMap<String, String>();

    public static String generateHTMLFromGenericInput(GenericSoapInputField genericInput) {
        operationsContainer.setCurrentElementXpathList(new ArrayList<String>());
        String htmlForm = " <div class=\"dropdown\"><a href=\"#\" class=\"dropdown_header\">basic data</a>\n" +
                "\n" +
                "        <div class=\"dropdown_content\">\n" +
                "            <fieldset>";
        List<String> complexTypesHTMLs = new ArrayList<String>();
        List<GenericSoapInputField> genericInputList = genericInput.getChildElements().get(0).getChildElements();
        for (GenericSoapInputField tempGenericInput : genericInputList) {
            if (tempGenericInput.getChildElements() == null) {
                htmlForm += generateHTMLFieldForSimpleInput(tempGenericInput) + "\n";
            } else {
                String complexTypeHTMLForm = " <div class=\"dropdown\"><a href=\"#\" class=\"dropdown_header\">" +
                        tempGenericInput.getFieldName() + "</a>\n" + "\n" +
                        "        <div class=\"dropdown_content\">\n <fieldset>\n";
                String tempComplexHTML = generateHTMLFieldForComplexInput(tempGenericInput, complexTypeHTMLForm, 0);
                if (tempGenericInput.isList()) {
                    listItemsHTMLs.put(tempGenericInput.getXpath(), tempComplexHTML);
                    tempComplexHTML += String.format(addItemButton, tempGenericInput.getXpath());
                    listItems.put(tempGenericInput.getXpath(), tempGenericInput);
                }
                tempComplexHTML += "</fieldset></div></div>";
                complexTypesHTMLs.add(tempComplexHTML);
            }
        }
        htmlForm += "</fieldset></div></div>";
        for (String complexTypeHTML : complexTypesHTMLs) {
            htmlForm += complexTypeHTML;
        }

        for (String key : listItemsHTMLs.keySet()) {
            System.out.println("key ===============" + key);
            System.out.println("listHTMLs ===========" + listItemsHTMLs.get(key));
        }

//        System.out.println("Xpath list =========== " + operationsContainer.getCurrentElementXpathList());
        return htmlForm;
    }

    public static String generateHTMLFieldForComplexInput(GenericSoapInputField genericSoapInputField, String htmlForm, int counter) {
        List<GenericSoapInputField> genericSoapInputFields = genericSoapInputField.getChildElements();
        for (GenericSoapInputField tempGenericInput : genericSoapInputFields) {
            if (tempGenericInput.getChildElements()!= null) {
                htmlForm += "<fieldset><legend>" + tempGenericInput.getFieldName() + "</legend>";
                for (GenericSoapInputField tempGenericInnerInput : tempGenericInput.getChildElements()) {
                    if (tempGenericInnerInput.getChildElements() != null) {
                        for (GenericSoapInputField temp : tempGenericInnerInput.getChildElements()) {
                            htmlForm += generateHTMLFieldForSimpleInput(temp);
                        }
                    }
                    if (tempGenericInnerInput.isList()) {
                        listItemsHTMLs.put(tempGenericInnerInput.getXpath(), htmlForm);
                        htmlForm += String.format(addItemButton, tempGenericInnerInput.getXpath());
                        listItems.put(tempGenericInnerInput.getXpath(), tempGenericInnerInput);
                    }
                    htmlForm += generateHTMLFieldForSimpleInput(tempGenericInnerInput) + "\n";
                }
                if (tempGenericInput.isList()) {
                    listItemsHTMLs.put(tempGenericInput.getXpath(), htmlForm);
                    htmlForm += String.format(addItemButton, tempGenericInput.getXpath());
                    listItems.put(tempGenericInput.getXpath(), tempGenericInput);
                }
                htmlForm += "</fieldset>";
//                generateHTMLFieldForComplexInput(genericSoapInputFields, htmlForm, counter);
            } else {
                if (tempGenericInput.isList()) {
                    listItemsHTMLs.put(tempGenericInput.getXpath(), htmlForm);
                    htmlForm += String.format(addItemButton, tempGenericInput.getXpath());
                    listItems.put(tempGenericInput.getXpath(), tempGenericInput);
                }
                htmlForm += generateHTMLFieldForSimpleInput(tempGenericInput) + "\n";
            }
        }
        return htmlForm;
    }

    public static String generateHTMLFieldForSimpleInput(GenericSoapInputField genericInput) {
        operationsContainer.addXpathIntoList(genericInput.getXpath());
        List<String> suggestedValues = genericInput.getSuggestedValues();
        String selectorOptions = "";
        if (suggestedValues != null) {
            for (String selectorValue : suggestedValues)
                selectorOptions += "<option value=\"" + selectorValue + "\"> " +
                        selectorValue + "</option>";
        }
        String selectorHTML = String.format(selectorTemplate, genericInput.getFieldName(), genericInput.getXpath(), selectorOptions);
        String fieldForm = String.format(fieldTemplate, genericInput.getFieldName(), genericInput.getFieldName(),
                genericInput.getXpath(), genericInput.getXpath(), genericInput.getValue());
        if (selectorOptions.length() > 1) {
            fieldForm = "<p>" + fieldForm + "<img src=\"/images/arrow-left-black.png\" class=\"combo_img\" /> " +
                    selectorHTML + "</p>";
        } else {
            fieldForm = "<p>" + fieldForm + "</p>";
        }
        if (genericInput.isMandatory()) {
            fieldForm = fieldForm.replace("<p>", "<p class=\"mandatory\">");
        }
        return fieldForm;
    }
}
