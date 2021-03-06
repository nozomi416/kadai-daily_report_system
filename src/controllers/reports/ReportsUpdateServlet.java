package controllers.reports;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import models.File;
import models.Report;
import models.validators.ReportValidator;
import utils.DBUtil;

/**
 * Servlet implementation class ReportsUpdateServlet
 */
@WebServlet("/reports/update")
@MultipartConfig(location="/Users/temp")
public class ReportsUpdateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsUpdateServlet() {
        super();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String _token = request.getParameter("_token");
        if(_token != null && _token.equals(request.getSession().getId())) {
            EntityManager em = DBUtil.createEntityManager();

            //ファイル削除の処理
            em.getTransaction().begin();
            if(request.getParameterValues("deleteFile_id") != null) {
                String[] deleteFiles = request.getParameterValues("deleteFile_id");
                for(String deleteFile_id: deleteFiles) {
                    em.createNamedQuery("deleteFile")
                        .setParameter("id", Integer.parseInt(deleteFile_id))
                        .executeUpdate();
                }
            }
            em.getTransaction().commit();

            Report r = em.find(Report.class, (Integer)(request.getSession().getAttribute("report_id")));

            r.setReport_date(Date.valueOf(request.getParameter("report_date")));
            r.setTitle(request.getParameter("title"));
            r.setContent(request.getParameter("content"));
            r.setUpdated_at(new Timestamp(System.currentTimeMillis()));

            //エラー確認
            List<String> errors = ReportValidator.validate(r);
            if(errors.size() > 0) { //エラーがあったら
                em.close();

                request.setAttribute("_token", request.getSession().getId());
                request.setAttribute("report", r);
                request.setAttribute("errors", errors);

                //edit.jspに戻す
                RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/edit.jsp");
                rd.forward(request, response);
            } else { //正常な場合
                em.getTransaction().begin();
                em.getTransaction().commit();

                //ファイルがある場合のみファイルの処理を行う
                File f;
                Collection<Part> parts = request.getParts();
                for(Part part: parts) {
                    if(part.getName().equals("upfile") && part.getSize() > 0) {
                        //ファイル名を調整するためファイルの名前と拡張子を分ける
                        int i = part.getSubmittedFileName().indexOf(".");
                        String file_name = part.getSubmittedFileName().substring(0, i);
                        String ext = part.getSubmittedFileName().substring(i);

                        //ファイル名に時刻を追記
                        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
                        String currentTimestampToString = new SimpleDateFormat("yyyyMMddHHmmss").format(currentTimestamp);

                        //下記ファイル名でアップロード
                        part.write("/Applications/Eclipse_4.6.3.app/Contents/workspace/daily_report_system/WebContent/images/"
                                    + file_name + "_" + currentTimestampToString + ext);

                        //ファイル情報を格納
                        f = new File();

                        f.setReport(r);
                        f.setFileName(file_name + "_" + currentTimestampToString + ext);
                        f.setFileOriginalName(part.getSubmittedFileName());
                        f.setCreated_at(currentTimestamp);

                        em.getTransaction().begin();
                        em.persist(f);
                        em.getTransaction().commit();
                    }
                }

                em.close();
                request.getSession().setAttribute("flush", "更新が完了しました。");

                request.getSession().removeAttribute("report_id");

                //indexへリダイレクト
                response.sendRedirect(request.getContextPath() + "/reports/index");
            }
        }
    }

}
