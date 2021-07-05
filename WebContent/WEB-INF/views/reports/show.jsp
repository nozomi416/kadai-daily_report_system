<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
        <c:if test="${flush != null}">
            <div id="flush_success">
                <c:out value="${flush}" />
            </div>
        </c:if>
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
                            <th>添付ファイル</th>
                            <td>
                                <c:forEach var="file" items="${files}" varStatus="status">
                                    <a href="/Applications/Eclipse_4.6.3.app/Contents/workspace/daily_report_system/WebContent/images/<c:out value="${file.fileName}" />">
                                        <img src="${pageContext.request.contextPath}/icons/jpg.png" alt="icon" width="50" height="50"><br />
                                        <c:out value="${file.fileOriginalName}" /><br /></a>
                                </c:forEach>
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

                <p class="index_a"><a href="<c:url value="/reports/index" />">一覧に戻る</a></p>

                <c:if test="${sessionScope.login_employee.id == report.employee.id}">
                    <p><a href="<c:url value="/reports/edit?id=${report.id}" />">この日報を編集する</a></p>
                </c:if>
                <c:if test="${sessionScope.login_employee.id != report.employee.id}">
                    <p><a href="<c:url value="/comments/new?report_id=${report.id}" />">この日報にコメントする</a></p>
                </c:if>

                <h3 id="comment_header">コメント</h3>
                <hr>
                <div>
                    <c:forEach var="comment" items="${comments}" varStatus="status">
                        <p id="comment_name">${comments.size() - status.index}.<span class="comment_employee_name"><c:out value="${comment.employee.name}" /></span></p>
                        <pre class="comment_content"><c:out value="${comment.content}" /></pre>
                        <p class="comment_date">投稿日：<fmt:formatDate value="${comment.comment_date}" pattern="yyyy-MM-dd" /></p>
                        <c:if test="${sessionScope.login_employee.id == comment.employee.id}">
                            <p id="comment_edit"><a href="<c:url value="/comments/edit?id=${comment.id}" />">このコメントを編集する</a></p>
                        </c:if>
                        <hr id="separationline">

                        <c:set value="${num - 1}" var="num" />
                    </c:forEach>
                </div>
                <c:if test="${comments.size() == 0}">
                    <p>コメントはありません。</p>
                </c:if>
            </c:when>
            <c:otherwise>
                <h2>お探しのデータは見つかりませんでした。</h2>
                <p><a href="<c:url value="/reports/index" />">一覧に戻る</a></p>
            </c:otherwise>
        </c:choose>
    </c:param>
</c:import>