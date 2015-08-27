<%@ page import="com.mkyong.bo.impl.Data" %>
<%@ page import="javax.xml.soap.SOAPConnectionFactory" %>
<%@ page import="javax.xml.soap.SOAPConnection" %>
<%@ page import="javax.xml.soap.SOAPMessage" %>
<%@ page import="com.mkyong.bo.impl.WSDLUtil" %>
<%@ page import="com.mkyong.bo.impl.GenericInput" %>
<%@ page import="java.util.List" %>
<%@ page import="com.mkyong.bo.impl.HtmlGenerator" %>
<%@ page import="java.util.HashMap" %>
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
    <title></title>
</head>
<body>

<%
    HtmlGenerator htmlGenerator = HtmlGenerator.getInstance();
    SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
    SOAPConnection soapConnection = soapConnectionFactory.createConnection();
    String url = "http://localhost:8081/hello";
    SOAPMessage soapRequestBody = WSDLUtil.createRequestTemplate("http://localhost:8081/hello?wsdl", null, null);
    htmlGenerator.setSoapRequest(soapRequestBody);
    List<GenericInput> genericInputs = WSDLUtil.getSpecifiedClassFields(soapRequestBody, "updateData");
    htmlGenerator.setGenericInputs(genericInputs);
    htmlGenerator.initializeTitle();
    HashMap<String, String> htmlFormMap = htmlGenerator.generateEntireHTMLForms();
%>

<form id="kiwForm" action="/anyLocation" method="post">

    <%=WSDLUtil.returnStringFromHashMap(htmlFormMap) %>
    <input type="submit" name="submit" id="submit" value="submit"/>

</form>

</body>
</html>