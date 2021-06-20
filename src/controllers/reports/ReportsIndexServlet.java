package controllers.reports;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class ReportsIndexServlet
 */
@WebServlet("/reports/index")
public class ReportsIndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsIndexServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();

        //変数宣言
        List<Report> reports;
        long reports_count;

        //パラメーターを取得
        String name = request.getParameter("name");
        String rd_str = request.getParameter("date");
        String title = request.getParameter("title");

        //ページを取得、初期値は1
        int page;
        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch(Exception e) {
            page = 1;
        }

        /*
         * 検索処理
         */
        if(rd_str != null && !rd_str.equals("")) {
            //キーワードに日付が入っている場合

            //日付を文字列からDate型に変換
            Date report_date = Date.valueOf(rd_str);

            reports = em.createNamedQuery("getSearchReports", Report.class)
                                    .setParameter("name", "%" + name + "%")
                                    .setParameter("report_date", report_date)
                                    .setParameter("title", "%" + title + "%")
                                    .setFirstResult(15 * (page -1))
                                    .setMaxResults(15)
                                    .getResultList();

            reports_count = (long)em.createNamedQuery("getSearchReportsCount", Long.class)
                                    .setParameter("name", "%" + name + "%")
                                    .setParameter("report_date", report_date)
                                    .setParameter("title", "%" + title + "%")
                                    .getSingleResult();

            request.setAttribute("name", name);
            request.setAttribute("date", rd_str);
            request.setAttribute("title", title);

        } else if(name != null && !name.equals("") || title!= null && !title.equals("")) {
            //キーワードに日付が入っていない場合
            reports = em.createNamedQuery("getSearchReportsNodate", Report.class)
                                    .setParameter("name", "%" + name + "%")
                                    .setParameter("title", "%" + title + "%")
                                    .setFirstResult(15 * (page -1))
                                    .setMaxResults(15)
                                    .getResultList();

            reports_count = (long)em.createNamedQuery("getSearchReportsNodateCount", Long.class)
                                    .setParameter("name", "%" + name + "%")
                                    .setParameter("title", "%" + title + "%")
                                    .getSingleResult();

            request.setAttribute("name", name);
            request.setAttribute("title", title);

        } else {
            //キーワードがnullの時は日報全件を表示
            reports = em.createNamedQuery("getAllReports", Report.class)
                                    .setFirstResult(15 * (page - 1))
                                    .setMaxResults(15)
                                    .getResultList();

            reports_count  = (long)em.createNamedQuery("getReportsCount", Long.class)
                                    .getSingleResult();
        }

        em.close();

        request.setAttribute("reports", reports);
        request.setAttribute("reports_count", reports_count);
        request.setAttribute("page", page);
        if(request.getSession().getAttribute("flush") != null) {
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            request.getSession().removeAttribute("flush");
        }

        //JSPに送る
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/index.jsp");
        rd.forward(request, response);
    }

}
