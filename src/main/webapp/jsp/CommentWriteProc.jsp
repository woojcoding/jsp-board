<%@ page import="com.study.model.BoardDao" %>
<%@ page import="com.study.model.BoardBean" %>
<%@ page import="com.study.model.CommentDao" %>
<%@ page import="com.study.model.CommentBean" %><%--
  Created by IntelliJ IDEA.
  User: 82103
  Date: 2023-06-30
  Time: PM 6:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
  <%
      request.setCharacterEncoding("utf-8");

      String comment = request.getParameter("comment");

      String boardId = request.getParameter("boardId");

      CommentBean commentBean = new CommentBean();
      commentBean.setContent(comment);
      commentBean.setBoardId(Long.parseLong(boardId));

      CommentDao commentDao = new CommentDao();
      commentDao.insertComment(commentBean);

      response.sendRedirect("BoardInfo.jsp?boardId=" + boardId);
  %>
</body>
</html>
