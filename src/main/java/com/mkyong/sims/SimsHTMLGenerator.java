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

        private static String addItemButton = "<div class=\"info\"><input type=\"button\" style=\"padding:0px;margin:0px;font-size=1.2em;\" " +
                "onclick=\"javascript:addItem('%s')\" value=\"Add Item\" /></div>\n" +
                "<div class=\"dropdown_content\" id=\"listItem%s\"></div>";

        private static List<String> listsXpaths = new ArrayList<String>();

        public static String generateHTMLFromGenericInput(GenericSoapInputField genericInput) {
            String htmlForm = " <div class=\"dropdown\"><a href=\"#\" class=\"dropdown_header\">basic data</a>\n" +
                    "\n" +
                    "        <div class=\"dropdown_content\">\n" +
                    "            <fieldset>";
            List<String> complexTypesHTMLs = new ArrayList<String>();
           /* System.out.println("___________________________________________");
            System.out.println("genericInput============" + genericInput);
            System.out.println("___________________________________________");*/
            List<GenericSoapInputField> genericInputList = genericInput.getChildElements().get(0).getChildElements();
            for (GenericSoapInputField tempGenericInput : genericInputList) {
                if (tempGenericInput.getChildElements() == null) {
                    htmlForm += generateHTMLFieldForSimpleInput(tempGenericInput) + "\n";
                } else {
                    String complexTypeHTMLForm = " <div class=\"dropdown init_hidden\"><a href=\"#\" class=\"dropdown_header\">" +
                            tempGenericInput.getFieldName() + "</a>\n" + "\n" +
                            "        <div class=\"dropdown_content\">\n <fieldset>\n";
                    String tempComplexHTML = generateHTMLFieldForComplexInput(tempGenericInput, complexTypeHTMLForm, 0);
                    if (tempGenericInput.isList()) {
                        getXpathsOfListGenericInput(tempGenericInput);
                        listsXpaths.add(tempGenericInput.getXpath());
                        tempComplexHTML += String.format(addItemButton, tempGenericInput.getXpath(), tempGenericInput.getXpath());
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
            List<GenericSoapInputField> genericSoapInputFields = genericSoapInputField.getChildElements();
            for (GenericSoapInputField tempGenericInput : genericSoapInputFields) {
                if (tempGenericInput.isList()) {
                    listsXpaths.add(tempGenericInput.getXpath());
                    getXpathsOfListGenericInput(tempGenericInput);
                    htmlForm += String.format(addItemButton, tempGenericInput.getXpath(), tempGenericInput.getXpath());
                } else {
                    if (tempGenericInput.getChildElements() != null) {
                        htmlForm += "<fieldset><legend>" + tempGenericInput.getFieldName() + "</legend>";
                        for (GenericSoapInputField tempGenericInnerInput : tempGenericInput.getChildElements()) {
                            if (tempGenericInnerInput.isList()) {
                                listsXpaths.add(tempGenericInnerInput.getXpath());
                                getXpathsOfListGenericInput(tempGenericInnerInput);
                                htmlForm += String.format(addItemButton, tempGenericInnerInput.getXpath(), tempGenericInput.getXpath());
                            } else {
                                if (tempGenericInnerInput.getChildElements() != null) {
                                    for (GenericSoapInputField temp : tempGenericInnerInput.getChildElements()) {
                                        htmlForm += generateHTMLFieldForSimpleInput(temp);
                                    }
                                } else {
                                    htmlForm += generateHTMLFieldForSimpleInput(tempGenericInnerInput) + "\n";
                                }
                            }
                        }
                        htmlForm += "</fieldset>";
                    } else {
                        htmlForm += generateHTMLFieldForSimpleInput(tempGenericInput) + "\n";
                    }
                }
            }
            return htmlForm;
        }

        public static String generateHTMLFieldForSimpleInput(GenericSoapInputField genericInput) {
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

        public static List<String> getListsXpaths() {
            return listsXpaths;
        }

        public static void setListsXpaths(List<String> listsXpaths) {
            SimsHTMLGenerator.listsXpaths = listsXpaths;
        }

        public static void getXpathsOfListGenericInput(GenericSoapInputField genericSoapInputFields) {
            if(genericSoapInputFields.getChildElements() != null) {
                for (GenericSoapInputField genericSoapInputField : genericSoapInputFields.getChildElements()) {
                    if (genericSoapInputField.getChildElements() == null) {
                        listsXpaths.add(genericSoapInputField.getXpath());
                    } else {
                        for (GenericSoapInputField genericSoapInputField1 : genericSoapInputField.getChildElements()) {
                            listsXpaths.add(genericSoapInputField1.getXpath());
                        }
                    }
                }
            }
        }

}
