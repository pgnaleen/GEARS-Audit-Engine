package com.gearsint.auditengine.controller;

import com.gearsint.auditengine.service.AuditQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin(exposedHeaders = "Access-Control-Allow-Origin")
@RequestMapping("/query-audit-logs")
public class AuditQueryController {
    @Autowired
    AuditQueryService auditService;

    @GetMapping("/audit-logs/{tableId}")
    public @ResponseBody
    String queryAuditLogsForTable(@PathVariable String tableId){

        return auditService.getAuditLogs(tableId, null, null);
    }

    @GetMapping("/audit-logs/{tableId}/{id}")
    public @ResponseBody
    String queryAuditLogsForId(@PathVariable String tableId, @PathVariable Integer id){

        return auditService.getAuditLogs(tableId, id, null);
    }

    @GetMapping("/audit-logs/{tableId}/{id}/{timestamp}")
    public @ResponseBody
    String queryAuditLogsForId(@PathVariable String tableId
            , @PathVariable Integer id, @PathVariable String timestamp){

        return auditService.getAuditLogs(tableId, id, timestamp);
    }
}
