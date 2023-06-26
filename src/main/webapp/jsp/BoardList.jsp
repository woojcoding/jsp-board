<%@ page import="java.util.List" %>
<%@ page import="com.study.model.CategoryDao" %>
<%@ page import="com.study.model.CategoryBean" %><%--
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
    <link rel="stylesheet" href="/css/board.css?after" type="text/css">
    <title>게시글 보기</title>
</head>
<body>
    <h1>자유게시판 - 목록</h1>
    <table class="search">
        <tr>
            <td>등록일</td>
            <td><input type="date" name="startDate" value="yyyy-mm-dd"> ~ <input type="date" name="endDate" value="yyyy-mm-dd"></td>
            <td>
                <%
                    CategoryDao categoryDao = new CategoryDao();

                    List<CategoryBean> allCategories = categoryDao.getAllCategories();
                %>
                <select name="category">
                    <option value="ALL">전체 카테고리</option>
                    <%
                        for (CategoryBean categoryBean : allCategories) {
                    %>
                    <option value="<%=categoryBean.getName()%>"><%=categoryBean.getName()%></option>
                    <%
                        }
                    %>
                </select>
            </td>
            <td> <input type="text" id="searchBox" placeholder="검색어를 입력해 주세요.(제목 + 작성자 + 내용)"></td>
            <td><input type="submit" value="검색"></td>
        </tr>
    </table>
    <br>총 000건
    <br>
    <table class="boardList">
        <tr>
            <td>카테고리</td>
            <td>제목</td>
            <td>작성자</td>
            <td>조회수</td>
            <td>등록 일시</td>
            <td>수정 일시</td>
        </tr>
    </table>
</body>
</html>
