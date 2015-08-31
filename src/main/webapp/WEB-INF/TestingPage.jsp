<%@ page import="javax.xml.soap.SOAPMessage" %>
<%@ page import="com.mkyong.bo.impl.WSDLUtil" %>
<%@ page import="com.mkyong.bo.impl.GenericInput" %>
<%@ page import="java.util.List" %>
<%@ page import="com.mkyong.bo.impl.HtmlGenerator" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>
<%--
  Created by IntelliJ IDEA.
  User: tigrank
  Date: 8/25/2015
  Time: 6:36 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Client page</title>
    <script>
        function showSoapRequest() {
            document.getElementById("soapBody").display = "block";
        }
    </script>
</head>

<body>

<%
    HashMap<String, String> defaultValues = new HashMap<String, String>();
    defaultValues.put("amount", "50");
    defaultValues.put("captureDay", "0");
    defaultValues.put("cardCSCValue", "100");
    defaultValues.put("cardExpiryDate", "201604");
    defaultValues.put("cardNumber", "4907000000000600");
    defaultValues.put("currencyCode", "978");
    defaultValues.put("customerId", "customerId1");
    defaultValues.put("customerIpAddress", "127.0.0.1");
    defaultValues.put("merchantId", "023101122334455");
    defaultValues.put("orderChannel", "INTERNET");
    defaultValues.put("orderId", "123");
    defaultValues.put("returnContext", "mon context de retour");
    defaultValues.put("transactionReference", "SIM201508289239");
    defaultValues.put("transactionOrigin", "SIPS-SIMS");
    defaultValues.put("interfaceVersion", "IR_WS_2.9");
    HtmlGenerator htmlGenerator = new HtmlGenerator();
    SOAPMessage soapRequestBody = WSDLUtil.createRequestTemplate("file:///C:/Users/tigrank/Desktop/PagesWSDL/page.xml", "cardOrder");
    htmlGenerator.setSoapRequest(soapRequestBody);
    List<GenericInput> genericInputs = WSDLUtil.getSpecifiedClassFields(soapRequestBody, "cardOrder", "file:///C:/Users/tigrank/Desktop/PagesWSDL/page.xml");
    htmlGenerator.setGenericInputs(genericInputs);
    htmlGenerator.initializeTitle();
    htmlGenerator.setValuesOfGenericInput(defaultValues);
    Map<String, String> htmlFormMap = htmlGenerator.generateEntireHTMLForms();
%>

<form id="kiwForm" action="/anyLocation" method="post">

    <%=WSDLUtil.returnStringFromHashMap(htmlFormMap) %>
    <input type="submit" name="submit" id="submit" value="submit"/>
    <button onclick="showSoapRequest()">Show Soap</button>
</form>

<div id="soapBody" style="display: none">
    <textarea name="soapRequestBody" id="soapRequestBody" cols="50" rows="5">
    </textarea>

</div>

</body>
</html>