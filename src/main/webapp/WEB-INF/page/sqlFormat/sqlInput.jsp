<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>用户管理</title>
    <%@include file="/WEB-INF/page/common.jsp" %>
    <script type="text/javascript">

    </script>
</head>

<body>

<form name="form1" action="${ctx}/sqlFormat/formatSql" method='post'>
    <textarea id="edtInputWord" name="sqlInputStr" rows="38" style="background-color:#68b1ff;color:#0e0e0e;width:45%;"><c:if test="${not empty sqlInputStr}">${sqlInputStr}</c:if><c:if test="${empty sqlInputStr}"> 请输入要格式化的SQL片段</c:if></textarea>
    <input type="submit" value="submit" style="background-color:#ff314c;color:#fbfbfb;width: 60px;height: 30px;">
    <textarea rows="38" style="background-color:#68b1ff;color:#0e0e0e;width:45%;">${sqlFormated}</textarea>
</form>

</body>
</html>
