package com.company.digisavingscore.controller;

import com.company.digisavingscore.dto.response.AuditEventResponse;
import com.company.digisavingscore.service.AuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Bharadi Harshitha
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/audit")
@RequiredArgsConstructor
public class AuditController {

    private final AuditService auditService;

    @GetMapping("/logs")
    public ResponseEntity<List<AuditEventResponse>> getAuditLogs(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) String eventType,
            @RequestParam(required = false) String severity,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        try {
            log.info("GET /admin/audit/logs - startDate: {}, endDate: {}, eventType: {}, severity: {}, page: {}, size: {}",
                    startDate, endDate, eventType, severity, page, size);

            List<AuditEventResponse> logs = auditService.getAuditLogs(
                    startDate, endDate, eventType, severity, page, size);

            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            log.error("Error fetching audit logs: ", e);
            throw e;
        }
    }

    @GetMapping("/logs/count")
    public ResponseEntity<Map<String, Long>> getAuditLogsCount(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) String eventType,
            @RequestParam(required = false) String severity) {

        try {
            log.info("GET /admin/audit/logs/count - startDate: {}, endDate: {}, eventType: {}, severity: {}",
                    startDate, endDate, eventType, severity);

            Long count = auditService.getAuditLogsCount(startDate, endDate, eventType, severity);

            Map<String, Long> response = new HashMap<>();
            response.put("count", count);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching audit logs count: ", e);
            throw e;
        }
    }

    @GetMapping("/event-types")
    public ResponseEntity<List<String>> getEventTypes() {
        try {
            log.info("GET /admin/audit/event-types");
            List<String> eventTypes = auditService.getEventTypes();
            return ResponseEntity.ok(eventTypes);
        } catch (Exception e) {
            log.error("Error fetching event types: ", e);
            throw e;
        }
    }
}