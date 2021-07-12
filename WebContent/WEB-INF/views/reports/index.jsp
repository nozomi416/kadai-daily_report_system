<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
        <div class="report_index">
            <c:if test="${flush != null}">
                <div id="flush_success">
                    <c:out value="${flush}"></c:out>
                </div>
            </c:if>
            <h2>日報 一覧</h2>

            <p class="p_report_new"><a  class="a_report_new" href="<c:url value='/reports/new' />">新規日報の登録 　></a></p>

            <!-- 検索フォーム -->
            <form method="GET" action ="<c:url value='/reports/index' />">
                <div class="search_field">
                    <h3 class="search_header">検 索</h3>
                    <div class="search_wrapper">
                        <div class="search_column">
                            <label>氏名 ※部分一致可</label><br />
                            <input type="text" name="name" id="search_name" class="search_box" value="${name}" placeholder="氏名を入力してください" />
                        </div>
                        <div class="search_column">
                            <label>日付</label><br />
                            <input type="date" name="date" id="search_date" class="search_box" value="${date}" />
                        </div>
                        <div class="search_column">
                            <label>タイトル ※部分一致可</label><br />
                            <input type="text" name="title" id="search_title" class="search_box" value="${title}" placeholder="タイトルを入力してください" />
                        </div>
                    </div>
                    <div class="button_wrapper">
                        <button type="button" class="search_reset_button" onclick="clearText();">条件をクリア</button>
                        <button type="submit" class="search_submit_button">検　索</button>

                        <!-- 検索条件クリアの機能 -->
                        <script>
                        function clearText() {
                            var search_name = document.getElementById('search_name');
                            search_name.value = '';
                            var search_date = document.getElementById('search_date');
                            search_date.value = '';
                            var search_title = document.getElementById('search_title');
                            search_title.value = '';
                            }
                        </script>
                    </div>
                </div>
            </form>

            <!-- 日報表示 -->
            <table id="report_list">
                <tbody>
                    <tr>
                        <th class="report_name">氏名</th>
                        <th class="report_date">日付</th>
                        <th class="report_title">タイトル</th>
                        <th class="report_action">操作</th>
                    </tr>
                    <c:forEach var="report" items="${reports}" varStatus="status">
                        <tr class="row${status.count % 2}">
                            <td class="report_name"><c:out value="${report.employee.name}" /></td>
                            <td class="report_date"><fmt:formatDate value="${report.report_date}" pattern="yyyy-MM-dd" /></td>
                            <td class="report_title">${report.title}</td>
                            <td class="report_action"><a href="<c:url value='/reports/show?id=${report.id}' />">詳細を見る</a></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <!-- ページネーション -->
            <div id="pagination">
                (全 ${reports_count} 件)<br />
                <c:forEach var="i" begin="1" end="${((reports_count - 1) / 15) + 1}" step="1">
                    <c:choose>
                        <c:when test="${i == page}">
                            <c:out value="${i}" />&nbsp;
                        </c:when>
                        <c:otherwise>
                            <a href="<c:url value='/reports/index?page=${i}&name=${name}&date=${date}&title=${title}' />"><c:out value="${i}" /></a>&nbsp;
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </div>
        </div>
    </c:param>
</c:import>