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
import models.Report;
import models.validators.CommentValidator;
import utils.DBUtil;

/**
 * Servlet implementation class CommentsUpdateServlet
 */
@WebServlet("/comments/update")
public class CommentsUpdateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public CommentsUpdateServlet() {
        super();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String _token = request.getParameter("_token");
        if(_token != null && _token.equals(request.getSession().getId())) {
            EntityManager em = DBUtil.createEntityManager();

            //セッションスコープのcomment_idに該当するコメントを抽出しcに代入
            Comment c = em.find(Comment.class, (Integer)(request.getSession().getAttribute("comment_id")));

            //登録日を設定
            c.setComment_date(Date.valueOf(request.getParameter("comment_date")));
            //内容を設定
            c.setContent(request.getParameter("content"));
            //更新日時を設定
            c.setUpdated_at(new Timestamp(System.currentTimeMillis()));

            //エラーチェック
            List<String> errors = CommentValidator.validate(c);
            if(errors.size() > 0) {  //エラーがある場合
                em.close();

                request.setAttribute("_token", request.getSession().getId());
                request.setAttribute("comment", c);
                request.setAttribute("errors", errors);

                //edit.jspに戻す
                RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/comments/edit.jsp");
                rd.forward(request, response);
            } else {
                em.getTransaction().begin();
                em.getTransaction().commit();
                em.close();
                //後ほどflush追加

                //リダイレクトで使用するため、セッションスコープのreportをget
                Report report = (Report)request.getSession().getAttribute("report");

                //詳細ページにリダイレクト
                response.sendRedirect(request.getContextPath() + "/reports/show?id=" + report.getId());
            }
        }
    }
}
