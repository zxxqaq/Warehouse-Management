package com.sirius.controller;

import com.sirius.domain.ResponseResult;
import com.sirius.domain.dto.UpdateRecordDto;
import com.sirius.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/historyRecord")
public class HistoryRecordController {

    @Autowired
    private RecordService recordService;

    @GetMapping("/companyId/{companyId}")
    public ResponseResult getHistoryRecordByCompanyId(@PathVariable("companyId") Long companyId) {
        return recordService.getHistoryRecordByCompanyId(companyId);
    }

    @GetMapping("/{companyId}/{itemId}")
    public ResponseResult getHistoryRecordByItemIdAndCompanyId(@PathVariable("companyId") Long companyId,
                                                               @PathVariable("itemId") Long itemId) {
        return recordService.getHistoryRecordByItemIdAndCompanyId(companyId, itemId);
    }

    @GetMapping("/itemId/{itemId}")
    public ResponseResult getHistoryRecordByItemId(@PathVariable("itemId") Long itemId) {
        return recordService.getHistoryRecordByItemId(itemId);
    }

    @DeleteMapping("/{recordId}")
    public ResponseResult deleteRecord(@PathVariable("recordId") Long recordId) {
        return recordService.deleteRecord(recordId);
    }

    @PutMapping("/updateRecord")
    public ResponseResult updateRecord(@RequestBody UpdateRecordDto updateRecordDto) {
        return recordService.updateRecord(updateRecordDto);
    }
}
