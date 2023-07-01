<%@ page import="java.util.List" %>
<%@ page import="com.study.model.CategoryDao" %>
<%@ page import="com.study.model.CategoryBean" %>
<%@ page import="com.study.model.BoardBean" %>
<%@ page import="com.study.model.BoardDao" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %><%--
  Created by IntelliJ IDEA.
  User: 82103
  Date: 2023-06-26
  Time: AM 10:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="/css/board-list.css?after" type="text/css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css" />
    <title>게시글 보기</title>
</head>
<body>
    <h1>자유게시판 - 목록</h1>
    <%
        int pageSize = 10;

        int total = 0;

        String pageNum = request.getParameter("pageNum");

        if (pageNum == null) {
            pageNum = "1";
        }

        int currentPage = Integer.parseInt(pageNum);

        BoardDao boardDao = new BoardDao();

        int startRow = (currentPage - 1) * pageSize + 1;

        int endRow = currentPage * pageSize;

        String startDate = request.getParameter("startDate");

        String endDate = request.getParameter("endDate");

        String categoryId = request.getParameter("category");

        String keyword = request.getParameter("keyword");

        total = boardDao.getBoardCount(startDate, endDate, categoryId, keyword);
    %>
    <form action="BoardList.jsp?&startDate=<%=startDate%>&endDate=<%=endDate%>&category=<%=categoryId%>&keyword=<%=keyword%>" method="get">
    <table class="search">
        <tr>
            <td>등록일</td>
            <td><input type="date" name="startDate" value="yyyy-mm-dd"> ~ <input type="date" name="endDate" value="yyyy-mm-dd"></td>
            <td>
                <%
                    CategoryDao categoryDao = new CategoryDao();

                    List<CategoryBean> allCategories = categoryDao.getAllCategories();

                    Map<Long, String> categories = new HashMap<>();
                %>
                <select name="category">
                    <option value="ALL">전체 카테고리</option>
                    <%
                        for (CategoryBean categoryBean : allCategories) {

                            categories.put(categoryBean.getCategoryId(), categoryBean.getName());
                    %>
                    <option value="<%=categoryBean.getCategoryId()%>" name="categoryId"><%=categoryBean.getName()%></option>
                    <%
                        }
                    %>
                </select>
            </td>
            <td> <input type="text" id="searchBox" name="keyword" placeholder="검색어를 입력해 주세요.(제목 + 작성자 + 내용)"></td>
            <td><button type="submit">검색</button></td>
        </tr>
    </table>
    </form>
    <br>총 <%=total%>건
    <table class="boardList">
        <tr>
            <td>카테고리</td>
            <td>첨부</td>
            <td>제목</td>
            <td>작성자</td>
            <td>조회수</td>
            <td>등록 일시</td>
            <td>수정 일시</td>
        </tr>
        <%
            List<BoardBean> boards = boardDao.getBoards(startDate, endDate, categoryId, keyword, startRow, endRow);

            for (BoardBean board : boards) {
                String attachmentStatus = board.isAttached() ? "<i class='fas fa-paperclip'></i>" : "";

                String shortenedTitle = board.getTitle().length() > 80 ? board.getTitle().substring(0, 80) + "..." : board.getTitle();
        %>
        <tr>
            <td><%=categories.get(board.getCategoryId())%></td>
            <td><%=attachmentStatus%></td>
            <td><a href="BoardInfo.jsp?boardId=<%=board.getBoardId()%>" style="text-decoration:none;"><%=shortenedTitle%></a></td>
            <td><%=board.getWriter()%></td>
            <td><%=board.getViews()%></td>
            <td><%=board.getCreatedAt()%></td>
            <td><%=board.getModifiedAt() != null ? board.getModifiedAt() : "-"%></td>
        </tr>
        <%
            }
        %>
    </table>
    <p id="paging">
        <%
            if (total > 0) {
                int pageCount = total / pageSize + (total % pageSize == 0 ? 0 : 1);

                int startPage = 1;

                if (currentPage % 10 != 0) {
                    startPage = (currentPage / 10) * 10 + 1;
                } else {
                    startPage = ((currentPage / 10) - 1) * 10 + 1;
                }

                int pageBlock = 10;

                int endPage = startPage + pageBlock - 1;

                if (endPage > pageCount) {
                    endPage = pageCount;
                }
        %>
        <a href="BoardList.jsp?pageNum=1&startDate=<%=startDate != null ? startDate : ""%>&endDate=<%=endDate != null ? endDate : ""%>&category=<%=categoryId != null ? categoryId : ""%>&keyword=<%=keyword != null ? keyword : ""%>"><<</a>
        <% if (startPage > 10) { %>
        <a href="BoardList.jsp?pageNum=<%=startPage - 10%>&startDate=<%=startDate != null ? startDate : ""%>&endDate=<%=endDate != null ? endDate : ""%>&category=<%=categoryId != null ? categoryId : ""%>&keyword=<%=keyword != null ? keyword : ""%>"><</a>
        <% } %>
        <% for (int i = startPage; i <= endPage; i++) { %>
        <a href="BoardList.jsp?pageNum=<%=i%>&startDate=<%=startDate != null ? startDate : ""%>&endDate=<%=endDate != null ? endDate : ""%>&category=<%=categoryId != null ? categoryId : ""%>&keyword=<%=keyword != null ? keyword : ""%>"><%=i%> </a>
        <% } %>
        <% if (endPage < pageCount) { %>
        <a href="BoardList.jsp?pageNum=<%=startPage + 10%>&startDate=<%=startDate != null ? startDate : ""%>&endDate=<%=endDate != null ? endDate : ""%>&category=<%=categoryId != null ? categoryId : ""%>&keyword=<%=keyword != null ? keyword : ""%>">></a>
        <% } %>
        <a href="BoardList.jsp?pageNum=<%=endPage%>&startDate=<%=startDate != null ? startDate : ""%>&endDate=<%=endDate != null ? endDate : ""%>&category=<%=categoryId != null ? categoryId : ""%>&keyword=<%=keyword != null ? keyword : ""%>">>></a>
        <% } %>
    </p>
    <button id="post" onclick="location.href='BoardWriteForm.jsp'">등록</button>
</body>
</html>
