package models;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Table(name = "files")
@NamedQueries({
    @NamedQuery(
            //日報ごとのファイルを取得
            name = "getReportsFiles",
            query = "SELECT f FROM File AS f WHERE f.report = :report ORDER BY f.id ASC"
            ),
    @NamedQuery(
            name = "deleteFile",
            query = "DELETE FROM File AS f WHERE f.id = :id"
            )
})
@Entity
public class File {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "report_id", nullable = false)
    private Report report;

    @Column(name = "fileName", nullable = false)
    private String fileName;

    @Column(name = "fileOriginalName", nullable = false)
    private String fileOriginalName;

    @Column(name = "created_at", nullable = false)
    private Timestamp created_at;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileOriginalName() {
        return fileOriginalName;
    }

    public void setFileOriginalName(String fileOriginalName) {
        this.fileOriginalName = fileOriginalName;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

}