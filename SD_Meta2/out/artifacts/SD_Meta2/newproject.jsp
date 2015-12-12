<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <title>New Project</title>
    <script>
        function add(divName)
        {
            var newdiv = document.createElement('div');
            newdiv.innerHTML ="<input id='productType' type='text' name='ProductType'>";
            document.getElementById(divName).appendChild(newdiv);
        }
    </script>
</head>
<body>
<h1>>>>New Project<<<</h1>
<form action="create" method="post">
    <p>Name:</p>
    <input id="name" type="text" name="Name"><br>
    <p>Description:</p>
    <input id="description" type="text" name="Description"><br>
    <p>Date Limit:</p>
    <input id="dateLimit" type="date" name="DateLimit"><br>
    <p>Requested Value:</p>
    <input id="requestedValue" type="number" name="RequestedValue"><br>
    <div id="ptype">
        <p>Product Type(s):</p>
        <input id="productType" type="text" name="ProductType">
        <input type="button" value="+" onclick="add('ptype');">
    </div><br>
    <input type="submit">
</form>

<s:form action="profile">
    <s:submit value="Back"/>
</s:form>
</body>
</html>
