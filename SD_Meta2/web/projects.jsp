<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>My Projects</title>
</head>
<body>
<c:forEach items="${session.projects}" var="project">
    <s:text name="Project ID:"/>
    <c:out value="${project.getProjectID()}"/><br>
    <s:text name="Admin:"/>
    <c:out value="${project.getUser().getUsername()}"/><br>
    <s:text name="Name:"/>
    <c:out value="${project.getProjectName()}"/><br>
    <s:text name="Description:"/>
    <c:out value="${project.getDescription()}"/><br>
    <s:text name="Date Limit:"/>
    <c:out value="${project.getDateLimit()}"/><br>
    <s:text name="Requested Value:"/>
    <c:out value="${project.getRequestedValue()}"/><br>
    <s:text name="Requested Value:"/>
    <c:out value="${project.getCurrentAmount()}"/><br>
    <s:text name="Rewards:"/><br>
    <c:forEach items="${project.getRewards()}" var="rewards">
        <s:text name="Description:"/>
        <c:out value="${rewards.getDescription()}"/><br>
        <s:text name="Value Of Reward"/>
        <c:out value="${rewards.getValueOfReward()}"/><br>
    </c:forEach>
    <s:text name="Product Types"/><br>
    <c:forEach items="${project.getProductTypes()}" var="productTypes">
        <s:text name="Type"/>
        <c:out value="${productTypes.getType()}"/><br>
        <s:text name="Vote"/>
        <c:out value="${productTypes.getVote()}"/><br>
    </c:forEach>
    <br>
</c:forEach>
<s:form action="profile">
    <s:submit value="Back"/>
</s:form>
</body>
</html>
