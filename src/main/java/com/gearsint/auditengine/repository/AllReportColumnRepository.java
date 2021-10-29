package com.gearsint.auditengine.repository;

import com.gearsint.auditengine.entity.AllReportColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllReportColumnRepository extends JpaRepository<AllReportColumn, Integer>
        , RevisionRepository<AllReportColumn, Integer, Long> {
}