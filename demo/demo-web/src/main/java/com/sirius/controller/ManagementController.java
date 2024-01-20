package com.sirius.controller;

import com.sirius.domain.ResponseResult;
import com.sirius.domain.dto.InitializeItemDto;
import com.sirius.domain.dto.InputItemDto;
import com.sirius.domain.dto.OutputItemDto;
import com.sirius.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/management")
public class ManagementController {

    @Autowired
    private RecordService recordService;

    @PostMapping("/initializeItem")
    public ResponseResult initializeItem(@RequestBody InitializeItemDto initializeItemDto) {
        return recordService.initializeItem(initializeItemDto);
    }

    @GetMapping("/{companyId}")
    public ResponseResult getItemList(@PathVariable("companyId") Long companyId) {
        return recordService.getItemList(companyId);
    }


    @PostMapping("/inputItem")
    public ResponseResult inputItem(@RequestBody InputItemDto inputItemDto) {
        return recordService.inputItem(inputItemDto);
    }

    @PostMapping("/outputItem")
    public ResponseResult outputItem(@RequestBody OutputItemDto outputItemDto) {
        return recordService.outputItem(outputItemDto);
    }
}
