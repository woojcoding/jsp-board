<%@ page import="com.study.model.BoardDao" %>
<%@ page import="com.study.model.BoardBean" %><%--
  Created by IntelliJ IDEA.
  User: 82103
  Date: 2023-07-01
  Time: AM 10:52
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
        
        String boardId = request.getParameter("boardId");

        String writer = request.getParameter("writer");

        String password = request.getParameter("password");

        String title = request.getParameter("title");

        String content = request.getParameter("content");

        BoardBean boardBean = new BoardBean();
        boardBean.setBoardId(Long.parseLong(boardId));
        boardBean.setPassword(password);

        BoardDao boardDao = new BoardDao();
        boolean isValidated = boardDao.validatePassword(boardBean);

        // 작성자 필수, 글자 수 검증
        if (writer == null || writer.length() < 3 || writer.length() >= 5) {
            System.out.println("작성자는 3글자 이상, 5글자 미만으로 입력해주세요.");

            return;
        }

        // 비밀번호 확인 일치 검증
        if (!isValidated) {
            System.out.println("비밀번호가 일치하지 않습니다.");

            return;
        }

        // 제목 필수, 글자 수 검증
        if (title == null || title.length() < 4 || title.length() >= 100) {
            System.out.println("제목은 4글자 이상, 100글자 미만으로 입력해주세요.");

            return;
        }

        // 내용 필수, 글자 수 검증
        if (content == null || content.length() < 4 || content.length() >= 2000) {
            System.out.println("내용은 4글자 이상, 2000글자 미만으로 입력해주세요.");

            return;
        }

        boardBean.setWriter(writer);
        boardBean.setTitle(title);
        boardBean.setContent(content);

        boardDao.updateBoard(boardBean);

        response.sendRedirect("BoardInfo.jsp?boardId=" + boardId);
    %>
</body>
</html>
