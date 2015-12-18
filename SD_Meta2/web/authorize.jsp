<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@	taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>Authorize</title>
</head>
<body>
<script type="text/javascript">
    function goURL()
    {
        window.location.href = '${session.authorization}';
    }

    window.onload = goURL();
</script>
</body>
</html>
