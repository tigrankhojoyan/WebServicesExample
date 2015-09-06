<%--
  Created by IntelliJ IDEA.
  User: tigrank
  Date: 9/2/2015
  Time: 5:57 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="com.mkyong.sims.OperationsContainer" %>
<%@ page import="com.mkyong.sims.GenericSoapInputField" %>
<%@ page import="com.mkyong.sims.SoapUtils" %>
<%@ page import="com.mkyong.sims.SimsHTMLGenerator" %>

<%

  String serviceTitle = "Checkout Credit Order";
  String responsePage = "/engine/simsResponse.jsp";
  String nameSpacePrefix = "urn";
  String nameSpace = "urn:sips:cn:contract:office:cashmanagement:v2";
  String wsdlPortTypeName = "cnv2CheckoutServiceImplPort";
  String wsdlOperationName = "cardOrder";
  String wsdlBindingName = "CheckoutServiceImplServiceSoapBinding";


  //Sets default values of cardOrder
  HashMap<String, String> cardOrderDefaultValues = new HashMap<String, String>();
  cardOrderDefaultValues.put("amount", "1000");
  cardOrderDefaultValues.put("captureDay", "0");
  cardOrderDefaultValues.put("cardCSCValue", "100");
  cardOrderDefaultValues.put("cardExpiryDate", "201604");
  cardOrderDefaultValues.put("cardNumber", "4907000000000600");
  cardOrderDefaultValues.put("currencyCode", "978");
  cardOrderDefaultValues.put("customerId", "customerId1");
  cardOrderDefaultValues.put("customerIpAddress", "127.0.0.1");
  cardOrderDefaultValues.put("merchantId", "222090000100016");
  cardOrderDefaultValues.put("orderChannel", "INTERNET");
  cardOrderDefaultValues.put("orderId", "123");
  cardOrderDefaultValues.put("returnContext", "mon context de retour");
  cardOrderDefaultValues.put("transactionReference", "SIM2015092185324");
  cardOrderDefaultValues.put("transactionOrigin", "SIPS-SIMS");
  cardOrderDefaultValues.put("interfaceVersion", "IR_WS_2.9");
  cardOrderDefaultValues.put("returnContext", "mon context de retour");

  //Sets suggested values of cardOrder
  HashMap<String, List<String>> cardOrderSuggestedValues = new HashMap<String, List<String>>();

  List<String> captureModeSuggestedValues = Arrays.asList("AUTHOR_CAPTURE", "VALIDATION", "IMMEDIATE", "BAD_VALUE");
  cardOrderSuggestedValues.put("captureMode", captureModeSuggestedValues);
  List<String> currencyCodeSuggestedValues = Arrays.asList("032", "036", "116", "124", "203", "208", "348", "356", "392", "410", "428", "440", "484", "544", "587", "643", "702", "710", "752", "756", "806", "840", "901", "949", "978", "985", "986", "BAD_VALUE", "NULL");
  cardOrderSuggestedValues.put("currencyCode", currencyCodeSuggestedValues);
  List<String> orderChannelSuggestedValues = Arrays.asList("INTERNET", "FAX", "IVR", "MAIL_ORDER", "MOTO", "TELEPHONE_ORDER", "PROXI_SEMIATTENDED", "BAD_VALUE");
  cardOrderSuggestedValues.put("orderChannel", orderChannelSuggestedValues);
  List<String> panTypeSuggestedValues = Arrays.asList("PAN", "TOKEN_PAN", "BAD_VALUE");
  cardOrderSuggestedValues.put("panType", panTypeSuggestedValues);
  List<String> interfaceVersionSuggestedValues = Arrays.asList("IR_WS_2.0", "IR_WS_2.1", "IR_WS_2.2", "IR_WS_2.3", "IR_WS_2.4", "IR_WS_2.5", "IR_WS_2.6", "IR_WS_2.7", "IR_WS_2.8", "IR_WS_2.9", "BAD_VALUE");
  cardOrderSuggestedValues.put("interfaceVersion", interfaceVersionSuggestedValues);
  List<String> paymentPatternSuggestedValues = Arrays.asList("INSTALMENT", "RECURRING_1", "RECURRING_N", "ONE_SHOT", "BAD_VALUE");
  cardOrderSuggestedValues.put("paymentPattern", paymentPatternSuggestedValues);

  OperationsContainer.initializeOperationContainer();
  GenericSoapInputField genericInput = OperationsContainer.getOperationsGenericInputs().get("cardOrder");

  SoapUtils.setDefaultValuesOfOperation(genericInput, cardOrderDefaultValues);
  SoapUtils.setSuggestedValues(cardOrderSuggestedValues, genericInput);
  SimsHTMLGenerator htmlGenerator = new SimsHTMLGenerator();
  String formBody = htmlGenerator.generateHTMLFromGenericInput(genericInput);
  System.out.println(formBody);

  List<String> listXpaths = htmlGenerator.getListsXpaths();
  String stringListXpaths = "[";
  for (String xPath : listXpaths) {
    stringListXpaths += "\"" + xPath + "\",";
  }
  stringListXpaths = stringListXpaths.substring(0, stringListXpaths.length() - 1) + "]";

%>
<script language='javascript'>

  function addItem(xPath) {
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
      addSimpleItem(xPath);
    } else {
      var fieldSet = constructFieldSetWithLegend(xPath);
      for(i = 0; i < dataFields.length; i++) {
        fieldSet.appendChild(constructField(dataFields[i]));
      }
      var insertingDiv = getElementInsertDiv(xPath);
      insertingDiv.appendChild(fieldSet);
    }
    /*alert(listInput);
     alert(listInput[2]);*/
    alert(dataFields)
  }

  function getElementInsertDiv(xPath) {
    var index = xPath.lastIndexOf("/");
    var divId = "listItem" + xPath;
    var elementInsertingDiv = document.getElementById(divId);
    return elementInsertingDiv;
  }

  function constructFieldSetWithLegend(xPath) {
    var legendName = xPath.substring(xPath.lastIndexOf("/"), xPath.length) +
            getElementInsertDiv(xPath).childElementCount;
    var fieldSet = document.createElement("FIELDSET");
    var legend = document.createElement("legend");
    legend.innerHTML = legendName;
    fieldSet.appendChild(legend);
    return fieldSet;
  }

  function constructField(xPath) {
    var index = xPath.lastIndexOf("/");
    alert(xPath.slice(0, index));
    var insertingDiv = getElementInsertDiv(xPath.slice(0, index));

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

  function addSimpleItem(xPath) {
    var index = xPath.lastIndexOf("/");
    var divId = "listItem" + xPath.slice(0, index);
    var addDiv = document.getElementById(divId);
    var newId = xPath + "[" + (addDiv.childElementCount + 1) + "]";
    var parentDiv = document.getElementById(divId).parentNode.parentNode.parentNode;
    parentDiv.setAttribute("class", "");
    parentDiv.setAttribute("style", "");
    var p = document.createElement("p");
    var label = document.createElement("label");
    var input = document.createElement("input");
    label.for = xPath.slice(index + 1) + (addDiv.childElementCount + 1);
    label.innerHTML = xPath.slice(index + 1) + (addDiv.childElementCount + 1);
    input.setAttribute("id", newId);
    input.setAttribute("name", newId);
    input.setAttribute("value", "");
    p.appendChild(label);
    p.appendChild(input);
    addDiv.appendChild(p);
  }

 /* function addItem(elementXpath) {
    /!* var xPaths =  <%=htmlGenerator.getListsXpaths()%>;
     alert(xPaths);*!/
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
*/
</script>

<h1>Sips Office SOAP V2 : CardOrder</h1>

<div class="info">
  WebService URL :
 Jimmy
</div>
<hr/>
<html>
<head>
  <title>CardOrder</title>
</head>
<body>
<form id="pwiForm" action="/GenericSoapRequestBuilder" method="post" class="form">
  <%= formBody%>
  <input type="hidden" id="serviceTitle" name="serviceTitle" value="<%=serviceTitle %>"/>
  <input type="hidden" id="responsePage" name="responsePage" value="<%=responsePage %>"/>
  <input type="hidden" id="nameSpacePrefix" name="nameSpacePrefix" value="<%=nameSpacePrefix %>"/>
  <input type="hidden" id="nameSpace" name="nameSpace" value="<%=nameSpace %>"/>
  <input type="hidden" id="wsdlPortTypeName" name="wsdlPortTypeName" value="<%=wsdlPortTypeName %>"/>
  <input type="hidden" id="wsdlOperationName" name="wsdlOperationName" value="<%=wsdlOperationName %>"/>
  <input type="hidden" id="wsdlBindingName" name="wsdlBindingName" value="<%=wsdlBindingName %>"/>
  <input type="submit" name="submit" id="submit" class="submit" value="submit"/>
  <input type="submit" name="submit" id="submit2" class="submit" value="preview XML"/>
  <input type="reset" name="reset" id="reset" class="reset" value="Reset Form"/>
  <input type="button" name="clear" id="clear" class="clear" value="Clear Form" onclick="clearForm('pwiForm')"/>
</form>

</body>
</html>