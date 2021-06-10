package controllers.comments;


import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Comment;
import models.Employee;
import models.Report;
import models.validators.CommentValidator;
import utils.DBUtil;

/**
 * Servlet implementation class CommentsCreateServlet
 */
@WebServlet("/comments/create")
public class CommentsCreateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public CommentsCreateServlet() {
        super();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String _token = request.getParameter("_token");
        if(_token != null && _token.equals(request.getSession().getId())) {
            EntityManager em = DBUtil.createEntityManager();

            //Commentのインスタンス化
            Comment c = new Comment();
            //セッションスコープのreportをgetし代入
            Report report = (Report)request.getSession().getAttribute("report");

            //Employee_idにlogin_employeeのidを設定
            c.setEmployee((Employee)request.getSession().getAttribute("login_employee"));
            //Report_idを設定
            c.setReport(report);

            //登録日を設定
            //フォームに入力された日付を上書き
            Date comment_date = new Date(System.currentTimeMillis());
            String cd_str = request.getParameter("comment_date");
            if(cd_str != null && !cd_str.equals("")) {
                comment_date = Date.valueOf(request.getParameter("comment_date"));
            }
            c.setComment_date(comment_date);

            //内容を設定
            c.setContent(request.getParameter("content"));

            //作成日時と更新日時を設定
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            c.setCreated_at(currentTime);
            c.setUpdated_at(currentTime);

            //エラーチェック
            List<String> errors = CommentValidator.validate(c);
            if(errors.size() > 0) { //エラーがある場合
                em.close();

                request.setAttribute("_token", request.getSession().getId());
                request.setAttribute("comment", c);
                request.setAttribute("errors", errors);

                //new.jspに戻す
                RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/comments/new.jsp");
                rd.forward(request, response);
            } else {
                em.getTransaction().begin();
                em.persist(c);
                em.getTransaction().commit();
                em.close();
                request.getSession().setAttribute("flush", "登録が完了しました。");

                //詳細ページにリダイレクト
                response.sendRedirect(request.getContextPath() + "/reports/show?id=" + report.getId());
            }
        }
    }
}
