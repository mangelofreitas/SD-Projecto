<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@	taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<head>
<title>Hello World</title>
</head>
<body>
	<c:choose>
		<c:when test="${session.tipo == login}">
			<h3>Login com sucesso!</h3>
			<br>
			<p>${user.getUser().getUsername()}</p>
		</c:when>
		<c:when test="${session.tipo == regist}">
			<h3>Registo com sucesso!</h3>
			<br>
			<p>${user.getUser().getUsername()}</p>
		</c:when>
	</c:choose>
	<p>Hello World, ${user.getUser().getUsername()}</p>
</body>
</html>