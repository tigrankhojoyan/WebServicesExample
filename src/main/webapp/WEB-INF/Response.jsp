<%@ page import="com.mkyong.bo.impl.HtmlGenerator" %>
<%@ page import="javax.xml.soap.SOAPMessage" %>
<%@ page import="java.io.ByteArrayOutputStream" %>
<%@ page import="java.io.OutputStream" %>
<%@ page import="javax.xml.soap.SOAPBody" %>
<%@ page import="com.mkyong.sims.OperationsContainer" %>
<%@ page import="com.mkyong.sims.GenericSoapInputField" %>
<%@ page import="com.mkyong.sims.SimsHTMLGenerator" %>
<%@ page import="java.util.List" %>
<%--
  Created by IntelliJ IDEA.
  User: tigrank
  Date: 8/26/2015
  Time: 11:45 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Test</title>
</head>
<%
    OperationsContainer.initializeOperationContainer();

    GenericSoapInputField genericInput = OperationsContainer.getOperationsGenericInputs().get("cardOrder");

        /*SoapUtils.setDefaultValuesOfOperation(genericInput, cardOrderDefaultValues);
        SoapUtils.setSuggestedValues(cardOrderSuggestedValues, genericInput);*/
    SimsHTMLGenerator htmlGenerator = new SimsHTMLGenerator();
    String formBody = htmlGenerator.generateHTMLFromGenericInput(genericInput);
    System.out.println("form body================" + formBody);

    List<String> listXpaths = htmlGenerator.getListsXpaths();
    String stringListXpaths = "[";
    for (String xPath : listXpaths) {
        stringListXpaths += "\"" + xPath + "\",";
    }
    stringListXpaths = stringListXpaths.substring(0, stringListXpaths.length() - 1) + "]";
%>
<script>

    function test(xPath) {
        //xpath:/cardOrder/input/riskManagementCustomDataList/riskManagementCustomData
        var listInput = <%=stringListXpaths%>;
        var dataFields = [];
        for (i = 0; i < listInput.length; i++) {
            //alert(listInput[i]);
            if ((listInput[i].indexOf(xPath) > -1) && (listInput[i].length > xPath.length)) {
                dataFields.push(listInput[i])
            }
        }
        if (dataFields.length == 0) {
            addItem(xPath);
        } else {
            var fieldSet = constructFieldSetWithLegend(xPath);
            for(i = 0; i < dataFields.length; i++) {
                fieldSet.appendChild(constructField[dataFields[i]]);
            }
            alert(fieldSet.toString);
            var insertingDiv = getElementInsertDiv(xPath);
            //insertingDiv.appendChild(fieldSet);
        }
        /*alert(listInput);
         alert(listInput[2]);*/
        alert(dataFields)
    }

    function getElementInsertDiv(xPath) {
        var index = xPath.lastIndexOf("/");
        var divId = "listItem" + xPath.slice(0, index);
        var elementInsertingDiv = document.getElementById(divId);
        return elementInsertingDiv;
    }

    function constructFieldSetWithLegend(xPath) {
        var legendName = xPath.substring(xPath.lastIndexOf("/"), xPath.length) +
                getElementInsertDiv(xPath).childElementCount;
        var fieldSet = document.createElement("FIELDSET");
        var legend = document.createElement("legend");
        legend.innerHTML = legendName;
        fieldSet.appendChild(fieldSet);
        return fieldSet;
    }

    function constructField(xPath) {
        var index = xPath.lastIndexOf("/");
        var insertingDiv = getElementInsertDiv(xPath);
        var newId = xPath + "[" + (insertingDiv.childElementCount + 1) + "]";
        var parentDiv = insertingDiv.parentNode.parentNode.parentNode;
        parentDiv.setAttribute("class", "");
        parentDiv.setAttribute("style", "");
        var p = document.createElement("p");
        var label = document.createElement("label");
        var input = document.createElement("input");
        label.for = xPath.slice(index + 1) + (insertingDiv.childElementCount + 1);
        label.innerHTML = xPath.slice(index + 1) + (insertingDiv.childElementCount + 1);
        input.setAttribute("id", newId);
        input.setAttribute("name", newId);
        input.setAttribute("value", "");
        p.appendChild(label);
        p.appendChild(input);
        return p;
    }

    function addItem(elementXpath) {
        var index = elementXpath.lastIndexOf("/");
        var divId = "listItem" + elementXpath.slice(0, index);
        var addDiv = document.getElementById(divId);
        var newId = elementXpath + "[" + (addDiv.childElementCount + 1) + "]";
        var parentDiv = document.getElementById(divId).parentNode.parentNode.parentNode;
        parentDiv.setAttribute("class", "");
        parentDiv.setAttribute("style", "");
        var p = document.createElement("p");
        var label = document.createElement("label");
        var input = document.createElement("input");
        label.for = elementXpath.slice(index + 1) + (addDiv.childElementCount + 1);
        label.innerHTML = elementXpath.slice(index + 1) + (addDiv.childElementCount + 1);
        input.setAttribute("id", newId);
        input.setAttribute("name", newId);
        input.setAttribute("value", "");
        p.appendChild(label);
        p.appendChild(input);
        addDiv.appendChild(p);
    }

</script>
<body>

<h1> Example </h1> <br>
<input type="button" value="test"
       onclick="javascript:test('xpath:/cardOrder/input/riskManagementCustomDataList/riskManagementCustomData');"/>

</body>
</html>
