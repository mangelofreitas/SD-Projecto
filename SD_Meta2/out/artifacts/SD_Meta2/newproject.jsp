<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <title>New Project</title>
    <script>
        function add(divName,idDiv,idInput,nameInput)
        {
            var newdiv = document.createElement('div');
            newdiv.id = idDiv;
            newdiv.innerHTML ="<input id="+idInput+" type='text' name="+nameInput+" required>";
            if(divName == 'rwrd')
            {
                newdiv.innerHTML += "<input id='valueReward' type='number' name='ValueReward' required>";
            }
            document.getElementById(divName).appendChild(newdiv);
        }
        function remove_div(divName,idDiv)
        {
            var div = document.getElementById(divName);
            var divToRemove = document.getElementById(divName).lastElementChild;
            var divRemove = document.getElementById(idDiv);
            if(divRemove!=null)
            {
                div.removeChild(divToRemove);
            }
        }
    </script>
</head>
<body>
<h1>>>>New Project<<<</h1>
<form action="create" method="post">
    <p>Name:</p>
    <input id="name" type="text" name="Name" required><br>
    <p>Description:</p>
    <input id="description" type="text" name="Description" required><br>
    <p>Date Limit:</p>
    <input id="dateLimit" type="date" name="DateLimit" required><br>
    <p>Requested Value:</p>
    <input id="requestedValue" type="number" name="RequestedValue" required><br>
    <div id="ptype">
        <p>Product Type(s):</p>
        <input id="productType" type="text" name="ProductType" required>
        <input type="button" value="+" onclick="add('ptype','ptypediv','productType','ProductType');">
        <input type="button" value="-" onclick="remove_div('ptype','ptypediv');">
    </div><br>
    <div id="rwrd">
        <p>Reward(s):</p>
        <input id="reward" type="text" name="Reward" required><input id="valueReward" type="number" name="ValueReward" required>
        <input type="button" value="+" onclick="add('rwrd','rwrddiv','reward','Reward');">
        <input type="button" value="-" onclick="remove_div('rwrd','rwrddiv');">
    </div><br>
    <input type="submit">
</form>

<s:form action="profile">
    <s:submit value="Back"/>
</s:form>
</body>
</html>
