package controllers.comments;

import java.io.IOException;
import java.sql.Date;

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
 * Servlet implementation class CommentsNewServlet
 */
@WebServlet("/comments/new")
public class CommentsNewServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public CommentsNewServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();
        request.setAttribute("_token", request.getSession().getId());

        //Reportオブジェクトを取得
        Report r = em.find(Report.class, Integer.parseInt(request.getParameter("report_id")));
        //Commentのインスタンス化
        Comment c = new Comment();
        //登録日に本日の日付を設定
        c.setComment_date(new Date(System.currentTimeMillis()));
        //Report_idを設定
        c.setReport(r);

        request.setAttribute("comment", c);

        //new.jspに送る
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/comments/new.jsp");
        rd.forward(request, response);
    }

}
