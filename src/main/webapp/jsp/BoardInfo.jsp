<%@ page import="com.study.model.BoardDao" %>
<%@ page import="com.study.model.BoardBean" %>
<%@ page import="com.study.model.CategoryDao" %>
<%@ page import="com.study.model.CommentDao" %>
<%@ page import="com.study.model.CommentBean" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <link rel="stylesheet" href="/css/board-info.css" type="text/css">
  <title>게시판 보기</title>
</head>
<body>
<h2>게시판 - 보기<h2>
    <%
    String boardId = request.getParameter("boardId");

    BoardDao boardDao = new BoardDao();

    boolean updateViews = true;

    BoardBean boardBean = boardDao.getOneBoard(boardId, updateViews);

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
      <td colspan="4" class="content"><%=boardBean.getContent().replaceAll("\n", "<br>")%></td>
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
        <%=commentBean.getContent().replaceAll("\n", "<br>")%>
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
        <button onclick="location.href='BoardUpdateForm.jsp?boardId=<%=boardId%>'">수정</button>
        <button onclick="location.href='PasswordValidate.jsp?boardId=<%=boardId%>'">삭제</button>
      </td>
    </tr>
  </table>
</body>
</html>
