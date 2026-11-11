package com.home.expenses_control.controller;

import com.home.expenses_control.dto.response.SummaryResponseDto;
import com.home.expenses_control.service.SummaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/summary")
@Slf4j
public class SummaryController {

    private final SummaryService summaryService;

    public SummaryController(SummaryService summaryService) {
        this.summaryService = summaryService;
    }

    @GetMapping
    public ResponseEntity<SummaryResponseDto> getTransactionsSummary(
        @RequestParam(defaultValue = "1") Integer page,
        @RequestParam(defaultValue = "10") Integer pageSize,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to){
        log.info("Get transactions summary: getTransactionsSummary(), page = {}, pageSize = {}, startDate = {}, endDate = {}", page, pageSize, from, to);

        return ResponseEntity.ok().body(summaryService.getTransactionsSummary(page, pageSize, from, to));
    }
}
