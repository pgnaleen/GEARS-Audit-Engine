package com.gearsint.auditengine.repository;

import com.gearsint.auditengine.entity.ReportJoin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;

public interface ReportJoinRepository extends JpaRepository<ReportJoin, Integer>
        , RevisionRepository<ReportJoin, Integer, Long> {
}