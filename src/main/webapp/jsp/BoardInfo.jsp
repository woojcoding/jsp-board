<%@ page import="com.study.model.BoardDao" %>
<%@ page import="com.study.model.BoardBean" %>
<%@ page import="com.study.model.CategoryDao" %>
<%@ page import="com.study.model.CommentDao" %>
<%@ page import="com.study.model.CommentBean" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <style>
    table {
      width: 100%;
      background-color: #fff;
      border-collapse: collapse;
      margin-bottom: 20px;
    }

    table td {
      padding: 10px;
    }

    table tr:first-child {
      font-weight: bold;
    }

    table tr:last-child td {
      border-top: none;
    }

    .title {
      border-bottom: 2px solid #000;
      font-weight: bold;
    }

    .content {
      border: 1px solid #ccc;
      padding: 10px;
      margin-top: 20px;
    }

    .rightAlign {
      text-align: right;
    }

    .comment {
      border-bottom: 1px dashed #ccc;
      background: #f2f2f2;
    }

    .commentEnd {
      background: #f2f2f2;
    }

    .buttons {
      text-align: center;
    }
  </style>
  <title>게시판 보기</title>
</head>
<body>
<h2>게시판 - 보기<h2>
    <%
    String boardId = request.getParameter("num");

    BoardDao boardDao = new BoardDao();

    BoardBean boardBean = boardDao.getOneBoard(boardId);

    CategoryDao categoryDao = new CategoryDao();

    String categoryName = categoryDao.getCategoryName(boardBean.getCategoryId());

  %>
  <table>
    <tr>
      <td><%=boardBean.getWriter()%></td>
      <td class="rightAlign">등록일시 <%=boardBean.getCreatedAt()%></td>
      <td class="rightAlign">수정일시 <%=boardBean.getModifiedAt() != null ? boardBean.getModifiedAt() : "-"%></td>
    </tr>
    <tr class="title">
      <td colspan="3">[<%=categoryName%>] <%=boardBean.getTitle()%></td>
      <td class="rightAlign">조회수: <%=boardBean.getViews()%></td>
    </tr>
    <tr class="content-row">
      <td colspan="4" class="content"><%=boardBean.getContent()%></td>
    </tr>
    <tr>
      <td colspan="4">
        첨부파일
        <br>
        첨부파일
        <br>
        첨부파일
        <br>
      </td>
    </tr>

      <%
        CommentDao commentDao = new CommentDao();

        List<CommentBean> comments = commentDao.getComments(boardId);

        for (CommentBean commentBean : comments) {
      %>
    <tr class="comment">
      <td colspan="4">
        <%=commentBean.getCreatedAt()%>
        <br>
        <%=commentBean.getContent()%>
      </td>
    </tr>
      <%
        }
      %>
    <form action="CommentWriteProc.jsp?boardId=<%=boardId%>" method="post">
    <tr class="commentEnd">
      <td colspan="3"><textarea rows="2" cols="250" name="comment" placeholder="댓글을 입력해주세요."></textarea></td>
      <td><button type="submit">등록</button> </td>
    </tr>
    </form>
    <tr>
      <td class="buttons" colspan="4">
        <button onclick="location.href='BoardList.jsp'">목록</button>
        <button onclick="location.href='BoardList.jsp'">수정</button>
        <button onclick="location.href='BoardList.jsp'">삭제</button>
      </td>
    </tr>
  </table>
</body>
</html>
