<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
        <c:choose>
            <c:when test="${report != null}">
                <h2>日報 詳細ページ</h2>

                <table>
                    <tbody>
                        <tr>
                            <th>氏名</th>
                            <td><c:out value="${report.employee.name}" /></td>
                        </tr>
                        <tr>
                            <th>日付</th>
                            <td><fmt:formatDate value="${report.report_date}" pattern="yyyy-MM-dd" /></td>
                        </tr>
                        <tr>
                            <th>内容</th>
                            <td>
                                <pre><c:out value="${report.content}" /></pre>
                            </td>
                        </tr>
                        <tr>
                            <th>登録日時</th>
                            <td>
                                <fmt:formatDate value="${report.created_at}" pattern="yyyy-MM-dd HH:mm:ss" />
                            </td>
                        </tr>
                        <tr>
                            <th>更新日時</th>
                            <td>
                                <fmt:formatDate value="${report.updated_at}" pattern="yyyy-MM-dd HH:mm:ss" />
                            </td>
                        </tr>
                    </tbody>
                </table>

                <c:if test="${sessionScope.login_employee.id == report.employee.id}">
                    <p><a href="<c:url value="/reports/edit?id=${report.id}" />">この日報を編集する</a></p>
                </c:if>
                <c:if test="${sessionScope.login_employee.id != report.employee.id}">
                    <p><a href="<c:url value="/comments/new" />">この日報にコメントする</a></p>
                </c:if>

                <p><a href="<c:url value="/reports/index" />">一覧に戻る</a></p>

                <h3>・コメント</h3>
                <table>
                    <tbody>
                        <c:forEach var="comment" items="${comments}">
                            <tr>
                                <td><c:out value="${comment.employee.name}" /></td>
                                <td><fmt:formatDate value="${comment.comment_date}" pattern="yyyy-MM-dd" /></td>
                            </tr>
                            <tr>
                                <td>
                                    <pre><c:out value="${comment.content}" /></pre>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>

                <c:if test="${sessionScope.login_employee.id == comment.employee.id}">
                    <p><a href="<c:url value="/comments/edit?id=${comment.id}" />">このコメントを編集する</a></p>
                </c:if>
            </c:when>
            <c:otherwise>
                <h2>お探しのデータは見つかりませんでした。</h2>
                <p><a href="<c:url value="/reports/index" />">一覧に戻る</a></p>
            </c:otherwise>
        </c:choose>
    </c:param>
</c:import>