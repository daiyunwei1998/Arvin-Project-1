package com.stylish.stylish.controller;

import com.stylish.stylish.errors.ErrorResponse;
import com.stylish.stylish.service.ReportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/api/1.0/report/payments")
    public ResponseEntity<?> getReport() {
        return reportService.reportOrders();
    }

    @GetMapping("/api/2.0/report/payments")
    public ResponseEntity<?> getReportMQ(@RequestBody Map<String, String> requestBody) {
        if (requestBody.containsKey("email")) {
            String email = (String) requestBody.get("email");
            return reportService.publishOrderProcessTask(email);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("no email provided"));
        }
    }
}
