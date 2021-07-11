<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:if test="${errors != null}">
    <div id="flush_error">
        入力内容にエラーがあります。<br />
        <c:forEach var="error" items="${errors}">
            ・<c:out value="${error}" /><br />
        </c:forEach>
    </div>
</c:if>
<div class="report_form">

    <label for="name">氏名</label><br />
    <c:out value="${sessionScope.login_employee.name}" />
    <br /><br />

    <label for="report_date">日付</label><br />
    <input class="form_box" type="date" name="report_date" value="<fmt:formatDate value='${report.report_date}' pattern='yyyy-MM-dd' />" />
    <br /><br />

    <label for="title">タイトル</label><br />
    <input class="form_box" type="text" name="title" value="${report.title}" placeholder="タイトルを入力してください。" />
    <br /><br />

    <label for="content">内容</label><br />
    <textarea class="form_box" name="content" rows="10" cols="50" placeholder="内容を入力してください。">${report.content}</textarea>
    <br /><br />

    <label for="file">添付ファイル </label><br />
    <c:if test="${files != null && files.size() > 0}">
        <label id="file_text">※削除する場合はチェックを入れて更新して下さい。</label><br />
    </c:if>
    <c:forEach var="file" items="${files}" varStatus="status">
        <div class="files">
            <img class="icons" src="${pageContext.request.contextPath}/icons/jpg.png" onclick="imgwin('<c:out value="${file.fileName}" />')" alt="icon"><br />
            <c:out value="${file.fileOriginalName}" /><br />
            <input id="delete_checkbox" type="checkbox" name="deleteFile_id" value="${file.id}">

            <%-- 画像表示スクリプト --%>
            <script type="text/javascript">
            function imgwin(img) {
                window.open("${pageContext.request.contextPath}/images/"+img, "imgwindow", "width=860, height=580");
                }
            </script>
        </div>
    </c:forEach>
    <br />

    <input class="file_option" type="file" name="upfile" accept=".jpg, .jpeg, .png, .pdf" multiple /><br />

    <input type="hidden" name="_token" value="${_token}" />
    <button class="form_button" type="submit">
        <c:choose>
            <c:when test="${report.title == null}">
            投　　稿
            </c:when>
            <c:otherwise>
            更　　新
            </c:otherwise>
        </c:choose>
    </button>
</div>