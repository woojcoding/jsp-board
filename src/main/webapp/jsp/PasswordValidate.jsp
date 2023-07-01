<%@ page import="com.study.model.BoardDao" %>
<%@ page import="com.study.model.BoardBean" %><%--
  Created by IntelliJ IDEA.
  User: 82103
  Date: 2023-07-01
  Time: PM 2:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <style>
    table {
      margin-left: auto;
      margin-right: auto;
    }

    table tr.password th {
      background-color: #f2f2f2;
    }

    table tr.password th,
    table tr.password td {
      border-top: 1px dotted black;
      border-bottom: 1px dotted black;
    }

    .buttons {
      text-align: center;
    }

  </style>
    <title>비밀번호 확인</title>
</head>
<body>
  <%
    String boardId= request.getParameter("boardId");

    BoardDao boardDao = new BoardDao();

    BoardBean boardBean = boardDao.getOneBoard(boardId);

    String dbPassword = boardBean.getPassword();
  %>
  <script>
    function validatePassword() {
      var password = document.getElementById("password").value;

      if (password == '<%=dbPassword%>') {
        location.href = "BoardDeleteProc.jsp?boardId=<%=boardId%>&password=" + encodeURIComponent(password);
      } else {
        alert("올바른 비밀번호를 입력해주세요.");
      }
    }
  </script>
  <table>
    <tr class="password">
      <th>비밀번호</th>
      <td><input type="password" id="password" name="password" placeholder="비밀번호를 입력해 주세요."></td>
    </tr>
    <tr>
      <td class="buttons" colspan="2">
        <button onclick="location.href='BoardInfo.jsp?boardId=<%=boardId%>'">취소</button>
        <button onclick="validatePassword();">확인</button>
      </td>
    </tr>
  </table>
</body>
</html>
