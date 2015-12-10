<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Hello World</title>
</head>
<body>
	<s:form action="login" method="post">
		<s:text name=">>>LOGIN<<<"/>
		<s:textfield key="mail" label="Email"/><br>
		<s:password key="password" label="Password"/><br>
		<s:submit method="execute"/>
	</s:form>
	<s:form action="regist" method="POST">
		<s:text name=">>>REGIST<<<"/>
		<s:textfield key="username" label="Username"/><br>
		<s:textfield key="mail" label="Email"/><br>
		<s:password key="password" label="Password"/><br>
		<s:submit method="execute"/>
	</s:form>
</body>
</html>