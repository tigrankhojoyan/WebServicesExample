<%--
  Created by IntelliJ IDEA.
  User: tigrank
  Date: 9/2/2015
  Time: 5:57 PM
  To change this template use File | Settings | File Templates.
--%>
<%@page import="net.atos.sips.sim.sipsofficeconnectv2.soap.GenericSoapInputField" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/base_page/header_and_menu.jsp"/>
<jsp:include page="/base_page/sharedCode.jsp"/>


<%@ page import="net.atos.sips.sim.sipsofficeconnectv2.soap.HTMLGenerator" %>
<%@ page import="net.atos.sips.sim.sipsofficeconnectv2.soap.OperationsContainer" %>
<%@ page import="net.atos.sips.sim.sipsofficeconnectv2.soap.SoapUtils" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ResourceBundle" %>

<%
  ResourceBundle bundle = ResourceBundle.getBundle("resource-bindings");
  String url = bundle.getString("net.atos.sips.cn.contract.office.checkout.v2.CheckoutService");

  String serviceBaseUrl = "";
  ResourceBundle resourceBundle = ResourceBundle.getBundle("m2m");
  if (resourceBundle != null) {
    serviceBaseUrl = resourceBundle.getString("socV2.base_url");
  }

  String serviceUrl = serviceBaseUrl + "checkout?wsdl";
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

  GenericSoapInputField genericInput = OperationsContainer.getOperationsGenericInputs().get("cardOrder");

  SoapUtils.setDefaultValuesOfOperation(genericInput, cardOrderDefaultValues);
  SoapUtils.setSuggestedValues(cardOrderSuggestedValues, genericInput);
  HTMLGenerator htmlGenerator = new HTMLGenerator();
  String formBody = htmlGenerator.generateHTMLFromGenericInput(genericInput);
%>
<script language='javascript'>
  function init() {
    assignCurrencyCode("978");
    assignField('cardExpiryDate', generate_expiry_date_YYYYMM());
    assignField('transactionReference', genererTransactionReference());
  }

  function addItem(elementXpath) {
    /* var xPaths =  <%=htmlGenerator.getListsXpaths()%>;
     alert(xPaths);*/
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

<h1>Sips Office SOAP V2 : CardOrder</h1>

<div class="info">
  WebService URL :
  <%=url%>
</div>
<hr/>
<form id="pwiForm" action="/GenericSoapRequestBuilder" method="post" class="form">
  <%= formBody%>
  <input type="hidden" id="serviceUrl" name="serviceUrl" value="<%=serviceUrl %>"/>
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

<script type="text/javascript">
  init();
</script>
<jsp:include page="/base_page/footer.jsp"/>
