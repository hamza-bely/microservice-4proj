package com.traficandme.report_service_traficandme.reports.controller;


import com.traficandme.report_service_traficandme.common.ApiResponse;
import com.traficandme.report_service_traficandme.reports.dto.CreateReportRequest;
import com.traficandme.report_service_traficandme.reports.dto.StatusReport;
import com.traficandme.report_service_traficandme.reports.dto.TypeReport;
import com.traficandme.report_service_traficandme.reports.entity.Report;
import com.traficandme.report_service_traficandme.reports.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@RestController
@RequestMapping("/api/reports/")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("create")
    public ResponseEntity<ApiResponse<Report>> create(@RequestBody CreateReportRequest request) {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = principal.toString();
            if(username == null){
                throw new IllegalArgumentException("User undefined");
            }

            Report report = reportService.createReport(request,username);
            return ResponseEntity.ok(new ApiResponse<>("Report created successfully", report));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(e.getMessage(), null));
        }
    }

    @GetMapping("get-all")
    public ResponseEntity<ApiResponse<List<Report>>> getAll() {
        try {
            List<Report> reports = reportService.getAllReports();
            return ResponseEntity.ok(new ApiResponse<>("Reports fetched successfully", reports));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(e.getMessage(), null));
        }
    }
    @GetMapping("get-all-by-user")
    public ResponseEntity<ApiResponse<List<Report>>> getAllByUser() {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = principal.toString();
            //UserResponse userConnected = userService.getUserByEmail(((UserDetails) principal).getUsername());
            if(username == null){
                throw new IllegalArgumentException("User undefined");
            }

            List<Report> reports = reportService.getAllReportsByUser(username);
            return ResponseEntity.ok(new ApiResponse<>("Reports fetched successfully", reports));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(e.getMessage(), null));
        }
    }

    @PostMapping("{id}/like")
    public ResponseEntity<ApiResponse<Report>> like(@PathVariable String id) {

        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = principal.toString();
            //UserResponse userConnected = userService.getUserByEmail(((UserDetails) principal).getUsername());
            if(username == null){
                throw new IllegalArgumentException("User undefined");
            }

            Report updated = reportService.likeReport(id, username);
            return ResponseEntity.ok(new ApiResponse<>("Report liked", updated));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(e.getMessage(), null));
        }
    }

    @PostMapping("{id}/dislike")
    public ResponseEntity<ApiResponse<Report>> dislike(@PathVariable String id) {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = principal.toString();
            //UserResponse userConnected = userService.getUserByEmail(((UserDetails) principal).getUsername());
            if(username == null){
                throw new IllegalArgumentException("User undefined");
            }

            Report updated = reportService.dislikeReport(id, username);
            return ResponseEntity.ok(new ApiResponse<>("Report disliked", updated));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(e.getMessage(), null));
        }
    }


    @PatchMapping("{id}/update-status")
    public ResponseEntity<ApiResponse<Report>> changeStatus(
            @PathVariable String id,
            @RequestBody StatusReport status) {
        try {
            Report updated = reportService.changeStatus(id, status);
            return ResponseEntity.ok(new ApiResponse<>("Status changed", updated));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(e.getMessage(), null));
        }
    }

    @PatchMapping("{id}/update-type")
    public ResponseEntity<ApiResponse<Report>> changeStatus(
            @PathVariable String id,
            @RequestBody TypeReport status) {
        try {
            Report updated = reportService.changeType(id, status);
            return ResponseEntity.ok(new ApiResponse<>("Type changed", updated));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(e.getMessage(), null));
        }
    }


    @DeleteMapping("{id}/delete-definitive")
    public ResponseEntity<ApiResponse<Void>> permanentlyDelete(@PathVariable String id) {
        try {
            reportService.deleteReport(id);
            return ResponseEntity.ok(new ApiResponse<>("Report deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(e.getMessage(), null));
        }
    }


}
