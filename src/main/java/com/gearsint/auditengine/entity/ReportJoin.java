package com.gearsint.auditengine.entity;

import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "report_joins")
@Entity
@Audited
public class ReportJoin {
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "report_table_id")
    private Integer reportTableId;

    @Column(name = "join_table_id")
    private Integer joinTableId;

    @Column(name = "report_table_column", length = 50)
    private String reportTableColumn;

    @Column(name = "join_table_column", length = 50)
    private String joinTableColumn;

    @Column(name = "join_str", length = 200)
    private String joinStr;

    public String getJoinStr() {
        return joinStr;
    }

    public void setJoinStr(String joinStr) {
        this.joinStr = joinStr;
    }

    public String getJoinTableColumn() {
        return joinTableColumn;
    }

    public void setJoinTableColumn(String joinTableColumn) {
        this.joinTableColumn = joinTableColumn;
    }

    public String getReportTableColumn() {
        return reportTableColumn;
    }

    public void setReportTableColumn(String reportTableColumn) {
        this.reportTableColumn = reportTableColumn;
    }

    public Integer getJoinTableId() {
        return joinTableId;
    }

    public void setJoinTableId(Integer joinTableId) {
        this.joinTableId = joinTableId;
    }

    public Integer getReportTableId() {
        return reportTableId;
    }

    public void setReportTableId(Integer reportTableId) {
        this.reportTableId = reportTableId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}