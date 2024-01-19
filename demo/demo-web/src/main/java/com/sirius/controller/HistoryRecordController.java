package com.sirius.controller;

import com.sirius.domain.ResponseResult;
import com.sirius.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/historyRecord")
public class HistoryRecordController {

    @Autowired
    private RecordService recordService;

    @GetMapping("/{companyId}")
    public ResponseResult getHistoryRecordByCompanyId(@PathVariable("companyId") long companyId){
        return recordService.getHistoryRecordByCompanyId(companyId);
    }

}
