<%@ page import="com.study.model.BoardDao" %>
<%@ page import="com.study.model.BoardBean" %><%--
  Created by IntelliJ IDEA.
  User: 82103
  Date: 2023-07-01
  Time: PM 2:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <%
        String boardId = request.getParameter("boardId");

        String inputPassword = request.getParameter("password");

        BoardBean boardBean = new BoardBean();
        boardBean.setBoardId(Long.parseLong(boardId));
        boardBean.setPassword(inputPassword);

        BoardDao boardDao = new BoardDao();
        boolean isValidated = boardDao.validatePassword(boardBean);

        // 비밀번호 확인 일치 검증
        if (!isValidated) {
        System.out.println("비밀번호가 일치하지 않습니다.");

        return;
        }

        boardDao.deleteBoard(boardId);

        response.sendRedirect("BoardList.jsp");
    %>
</body>
</html>
