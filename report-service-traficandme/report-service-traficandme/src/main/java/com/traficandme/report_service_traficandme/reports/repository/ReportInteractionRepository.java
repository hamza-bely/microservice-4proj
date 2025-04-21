package com.traficandme.report_service_traficandme.reports.repository;

import com.traficandme.report_service_traficandme.reports.entity.ReportInteraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReportInteractionRepository extends JpaRepository<ReportInteraction, Long> {
    boolean existsByReportIdAndUserEmailAndAction(String reportId, String userEmail, String action);
    Optional<ReportInteraction> findByReportIdAndUserEmail(String reportId, String userEmail);

}