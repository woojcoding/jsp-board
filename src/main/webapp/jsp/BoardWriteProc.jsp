<%--
  Created by IntelliJ IDEA.
  User: 82103
  Date: 2023-06-30
  Time: AM 9:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.study.model.BoardBean" %>
<%@ page import="com.study.model.BoardDao" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%
    request.setCharacterEncoding("utf-8");

    BoardBean boardBean = new BoardBean();

    String writer = request.getParameter("writer");

    String password = request.getParameter("password");

    String password2 = request.getParameter("password2");

    String title = request.getParameter("title");

    String content = request.getParameter("content");

    String categoryIdParam = request.getParameter("categoryId");

    // 작성자 필수, 글자 수 검증
    if (writer == null || writer.length() < 3 || writer.length() >= 5) {
        System.out.println("작성자는 3글자 이상, 5글자 미만으로 입력해주세요.");

        return;
    }

    // 비밀번호 필수, 글자 수, 패턴 검증
    if (password == null || password.length() < 4 || password.length() >= 16 || !password.matches("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]+$")) {
        System.out.println("비밀번호는 4글자 이상, 16글자 미만의 영문, 숫자, 특수문자 조합으로 입력해주세요.");

        return;
    }

    // 비밀번호 확인 일치 검증
    if (!password.equals(password2)) {
        System.out.println("비밀번호와 비밀번호 확인이 일치하지 않습니다.");

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

    // 카테고리 필수 선택 검증
    long categoryId = 0L;
    if (categoryIdParam == null || categoryIdParam.equals("ALL")) {
        System.out.println("카테고리를 선택해주세요.");

        return;
    } else {
        try {
            categoryId = Long.parseLong(categoryIdParam);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    boardBean.setCategoryId(categoryId);
    boardBean.setWriter(writer);
    boardBean.setPassword(password);
    boardBean.setTitle(title);
    boardBean.setContent(content);

    String fileParameter = request.getParameter("file");

    if (fileParameter != null && !fileParameter.isEmpty()) {
        boardBean.setAttached(true);
    } else {
        boardBean.setAttached(false);
    }

    BoardDao boardDao = new BoardDao();

    boardDao.insertBoard(boardBean);

    response.sendRedirect("BoardList.jsp");
%>
</body>
</html>
