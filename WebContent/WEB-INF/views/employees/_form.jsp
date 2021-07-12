<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${errors != null}">
    <div id="flush_error">
        入力内容にエラーがあります。<br />
        <c:forEach var="error" items="${errors}">
            ・<c:out value="${error}" /><br />
        </c:forEach>
    </div>
</c:if>
<div class="employee_form">
    <label for="code">社員番号</label><br />
    <input class="form_box" type="text" name="code" value="${employee.code}" placeholder="社員番号を入力してください。" />
    <br /><br />

    <label for="name">氏名</label><br />
    <input class="form_box" type="text" name="name" value="${employee.name}" placeholder="氏名を入力してください。" />
    <br /><br />

    <label for="password">パスワード</label><br />
    <input class="form_box" type="password" name="password"  placeholder="パスワードを入力してください。" />
    <br /><br />

    <label for="admin_flag">権限</label><br />
    <select name="admin_flag">
        <option value="0"<c:if test="${employee.admin_flag == 0}"> selected</c:if>>一般</option>
        <option value="1"<c:if test="${employee.admin_flag == 1}"> selected</c:if>>管理者</option>
    </select>
    <br /><br />

    <input type="hidden" name="_token" value="${_token}" />
    <button class="form_button" type="submit">登　　録</button>
</div>