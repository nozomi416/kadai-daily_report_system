package controllers.reports;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Comment;
import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class ReportsShowServlet
 */
@WebServlet("/reports/show")
public class ReportsShowServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsShowServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();

        //URLのidから該当する日報を抽出してrに代入
        Report r = em.find(Report.class, Integer.parseInt(request.getParameter("id")));
        //抽出した日報に対するコメントをリストcommentsに代入
        List<Comment> comments = em.createNamedQuery("getReportsComments", Comment.class)
                                    .setParameter("report", r)
                                    .getResultList();

        em.close();

        //リクエストスコープにset
        request.setAttribute("report", r);
        request.setAttribute("comments", comments);
        request.setAttribute("_token", request.getSession().getId());
        //セッションスコープにflushメッセージがあればリクエストスコープに置き換えて、
        //セッションスコープからは削除
        if(request.getSession().getAttribute("flush") != null) {
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            request.getSession().removeAttribute("flush");
        }

        //show.jspに送る
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/show.jsp");
        rd.forward(request, response);
    }

}
