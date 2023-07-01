<%@ page import="com.study.model.BoardDao" %>
<%@ page import="com.study.model.BoardBean" %>
<%@ page import="com.study.model.CategoryDao" %><%--
  Created by IntelliJ IDEA.
  User: 82103
  Date: 2023-07-01
  Time: AM 10:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <style>
    /* 테이블 스타일 */
    table {
      width: 100%;
      border-collapse: collapse;
      border-spacing: 0;
    }

    /* 테이블 헤더 스타일 */
    table th {
      background-color: #f2f2f2;
      text-align: left;
      padding: 10px;
      border-bottom: 1px dotted #ccc;
    }

    /* 테이블 데이터 스타일 */
    table td {
      padding: 10px;
      border-bottom: 1px dotted #ccc;
    }

    /* 첫 번째 열 스타일 */
    table td:first-child,
    table th:first-child {
      background-color: #f5f5f5;
    }

    /* 마지막 행 스타일 */
    table tr:last-child td {
      background-color: white;
      border-bottom: none;
    }

    .text {
      width: 100%;
    }

    .button-container {
      display: flex;
      justify-content: space-between;
    }

    .button-wrapper {
      flex: 1;
    }

    .button {
      display: inline-block;
      padding: 8px 16px;
      background-color: #f5f5f5;
      border: none;
      color: #333;
      text-align: center;
      text-decoration: none;
      font-size: 14px;
      cursor: pointer;
      border-radius: 4px;
    }
  </style>
  <title>게시판 수정</title>
</head>
<body>
  <%
    request.setCharacterEncoding("utf-8");

    String boardId = request.getParameter("boardId");

    BoardDao boardDao = new BoardDao();

    BoardBean boardBean = boardDao.getBoardInfo(boardId);

    CategoryDao categoryDao = new CategoryDao();

    String categoryName = categoryDao.getCategoryName(boardBean.getCategoryId());
  %>
  <script>
    function validateForm() {

      var writer = document.forms["myForm"]["writer"].value;
      var password = document.forms["myForm"]["password"].value;
      var title = document.forms["myForm"]["title"].value;
      var content = document.forms["myForm"]["content"].value;

      // 작성자 필수, 글자 수 검증
      if (writer.length < 3 || writer.length >= 5) {
        alert("작성자는 3글자 이상, 5글자 미만으로 입력해주세요.");
        return false;
      }

      // 비밀번호 검증
      if (password !== "<%= boardBean.getPassword() %>") {
        alert("올바른 비밀번호를 입력해주세요.");
        return false;
      }

      // 제목 필수, 글자 수 검증
      if (title.length < 4 || title.length >= 100) {
        alert("제목은 4글자 이상, 100글자 미만으로 입력해주세요.");
        return false;
      }

      // 내용 필수, 글자 수 검증
      if (content.length < 4 || content.length >= 2000) {
        alert("내용은 4글자 이상, 2000글자 미만으로 입력해주세요.");
        return false;
      }
    }
  </script>
  <h2>게시판 - 수정</h2>
  기본정보
  <form name="myForm" action="BoardUpdateProc.jsp" method="post">
  <table>
    <tr>
      <th>카테고리</th>
      <td><%=categoryName%></td>
    </tr>
    <tr>
      <th>등록 일시</th>
      <td><%=boardBean.getCreatedAt()%></td>
    </tr>
    <tr>
      <th>수정 일시</th>
      <td><%=boardBean.getModifiedAt() != null ? boardBean.getModifiedAt() : "-"%></td>
    </tr>
    <tr>
      <th>조회수</th>
      <td><%=boardBean.getViews()%></td>
    </tr>
    <tr>
      <th>작성자</th>
      <td><input type="text" name="writer" value="<%=boardBean.getWriter()%>"></td>
    </tr>
    <tr>
      <th>비밀번호</th>
      <td><input type="password" name="password" placeholder="비밀번호"></td>
    </tr>
    <tr>
      <th>제목</th>
      <td><textarea class="text" rows="1" cols="100" name="title"><%=boardBean.getTitle()%></textarea></td>
    </tr>
    <tr>
      <th>내용</th>
      <td><textarea class="text" rows="10" cols="60" name="content"><%=boardBean.getContent()%></textarea></td>
    </tr>
    <tr>
      <th>파일 첨부</th>
      <td>
        첨부파일 다운로드 삭제
        <br>
        <input type="file">
      </td>
    </tr>
    <tr>
      <td colspan="2" class="button-container">
        <div class="button-wrapper start">
          <a href="BoardInfo.jsp?boardId=<%=boardId%>" class="button">취소</a>
        </div>
        <div class="button-wrapper end">
          <input type="hidden" name="boardId" value="<%=boardId%>">
          <button onclick="return validateForm();" class="button">저장</button>
        </div>
      </td>
    </tr>
  </table>
  </form>
</body>
</html>
