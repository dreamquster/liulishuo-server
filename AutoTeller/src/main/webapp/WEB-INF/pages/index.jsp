<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2015/11/21 0021
  Time: 15:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Welcome to AutoTeller</title>
</head>
<body>
    <p>POST /user/add<br/>
        user_id=1&coins=10<br/>
        为ID为1的用户添加10金币<br/>
        <br/>
        POST /transaction/transfer<br/>
        from_user_id=1&to_user_id=2&coins=500<br/>
        从用户1向用户2转入500金币<br/>
        如果用户1账户不足,或者账户不存在<br/>
        <i>failed to transfer coins from 1 to 2 with error: e.getMessage();</i><br/>
        <br/>
        GET /coins/user/1<br/>
        返回用户1的金币数<br/>
        如果用户1不存在<br/>
        <i>Illegal Argument:The user 1 is not exist</i>
    </p>
</body>
</html>
