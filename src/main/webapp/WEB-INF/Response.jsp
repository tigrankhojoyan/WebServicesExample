<%@ page import="com.mkyong.bo.impl.HtmlGenerator" %>
<%@ page import="javax.xml.soap.SOAPMessage" %>
<%@ page import="java.io.ByteArrayOutputStream" %>
<%@ page import="java.io.OutputStream" %>
<%@ page import="javax.xml.soap.SOAPBody" %>
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
    <title></title>
</head>
<body>
<%
  HtmlGenerator htmlGenerator = HtmlGenerator.getInstance();
  SOAPMessage responseBody = htmlGenerator.getSoapResponse();
  ByteArrayOutputStream stream = new ByteArrayOutputStream();
  responseBody.writeTo(stream);
  String stringResponse = new String(stream.toByteArray(), "utf-8");
  //String stringResponse = new String(os.toByteArray(),"UTF-8");
  /*String stringResponse = responseBody.writeTo(outputStream);*/
%>
<h1> Example </h1> <br>
<%=stringResponse %>
</body>
</html>
