package com.gearsint.auditengine.entity;

import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "all_report_columns")
@Entity
@Audited
public class AllReportColumn {
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "report_table_id")
    private Integer report_table_id;

    @Column(name = "column_name", length = 100)
    private String columnName;

    @Column(name = "column_display_name", length = 100)
    private String columnDisplayName;

    @Column(name = "column_data_type", length = 20)
    private String columnDataType;

    @Column(name = "column_default_filter", length = 200)
    private String columnDefaultFilter;

    public String getColumnDefaultFilter() {
        return columnDefaultFilter;
    }

    public void setColumnDefaultFilter(String columnDefaultFilter) {
        this.columnDefaultFilter = columnDefaultFilter;
    }

    public String getColumnDataType() {
        return columnDataType;
    }

    public void setColumnDataType(String columnDataType) {
        this.columnDataType = columnDataType;
    }

    public String getColumnDisplayName() {
        return columnDisplayName;
    }

    public void setColumnDisplayName(String columnDisplayName) {
        this.columnDisplayName = columnDisplayName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Integer getReport_table_id() {
        return report_table_id;
    }

    public void setReport_table_id(Integer reportTableId) {
        this.report_table_id = reportTableId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}