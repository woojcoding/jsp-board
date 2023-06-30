<%@ page import="com.study.model.CategoryDao" %>
<%@ page import="com.study.model.CategoryBean" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %><%--
  Created by IntelliJ IDEA.
  User: 82103
  Date: 2023-06-27
  Time: AM 9:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>게시판-등록</title>

</head>
<body>
<script>
    function validateForm() {
        var category = document.forms["myForm"]["categoryId"].value;
        var writer = document.forms["myForm"]["writer"].value;
        var password = document.forms["myForm"]["password"].value;
        var password2 = document.forms["myForm"]["password2"].value;
        var title = document.forms["myForm"]["title"].value;
        var content = document.forms["myForm"]["content"].value;

        // 카테고리 필수 선택 검증
        if (category === "ALL") {
            alert("카테고리를 선택해주세요.");
            return false;
        }

        // 작성자 필수, 글자 수 검증
        if (writer.length < 3 || writer.length >= 5) {
            alert("작성자는 3글자 이상, 5글자 미만으로 입력해주세요.");
            return false;
        }

        // 비밀번호 필수, 글자 수, 패턴 검증
        if (password.length < 4 || password.length >= 16 || !password.match(/^(?=.*[a-zA-Z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]+$/)) {
            alert("비밀번호는 4글자 이상, 16글자 미만의 영문, 숫자, 특수문자 조합으로 입력해주세요.");
            return false;
        }

        // 비밀번호 확인 일치 검증
        if (password !== password2) {
            alert("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
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
게시판 - 등록
    <form name="myForm" action="BoardWriteProc.jsp" method="post" onsubmit="return validateForm()">
    <table>
        <tr>
            <td>카테고리</td>
            <td>
            <%
                CategoryDao categoryDao = new CategoryDao();

                List<CategoryBean> allCategories = categoryDao.getAllCategories();

                Map<Long, String> categories = new HashMap<>();
            %>
            <select name="categoryId">
                <option value="ALL">카테고리 선택</option>
                <%
                    for (CategoryBean categoryBean : allCategories) {

                        categories.put(categoryBean.getCategoryId(), categoryBean.getName());
                %>
                <option value="<%=categoryBean.getCategoryId()%>"><%=categoryBean.getName()%></option>
                <%
                    }
                %>
            </select>
            </td>
        </tr>
        <tr>
            <td>작성자</td>
            <td><input type="text" name="writer" id="writer"></td>
        </tr>
        <tr>
            <td>비밀번호</td>
            <td><input type="password" placeholder="비밀번호" name="password"></td>
            <td><input type="password" placeholder="비밀번호 확인" name="password2"></td>
        </tr>
        <tr>
            <td>제목</td>
            <td><input type="text" name="title"></td>
        </tr>
        <tr>
            <td>내용</td>
            <td><textarea rows="10" cols="60" name="content"></textarea></td>
        </tr>
        <tr>
            <td>파일첨부</td>
            <td>
                <input type="file" name="file1">
                <br>
                <input type="file" name="file2">
                <br>
                <input type="file" name="file3">
            </td>
        </tr>
        <tr>
            <td><button onclick="location.href='BoardList.jsp'">취소</button></td>
            <td><input type="submit" value="저장"></td>
        </tr>
    </table>
    </form>
</body>
</html>
