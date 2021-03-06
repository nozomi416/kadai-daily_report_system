package controllers.comments;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Comment;
import models.Employee;
import utils.DBUtil;

/**
 * Servlet implementation class CommentsEditServlet
 */
@WebServlet("/comments/edit")
public class CommentsEditServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public CommentsEditServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();

        //URLのidから該当するコメントを抽出してcに代入
        Comment c = em.find(Comment.class, Integer.parseInt(request.getParameter("id")));

        em.close();

        Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");
        //ログインユーザーとコメント者が同じだったら
        if(c != null && login_employee.getId() == c.getEmployee().getId()) {
            request.setAttribute("comment", c);
            request.setAttribute("_token", request.getSession().getId());
        }

        //edit.jspに送る
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/comments/edit.jsp");
        rd.forward(request, response);
    }
}
