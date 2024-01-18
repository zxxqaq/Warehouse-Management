package com.sirius.controller;

import com.sirius.domain.ResponseResult;
import com.sirius.domain.dto.InitializeItemDto;
import com.sirius.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/management")
public class ManagementController {

    @Autowired
    private RecordService recordService;

    @PostMapping("/initializeItem")
    public ResponseResult initializeItem(@RequestBody InitializeItemDto initializeItemDto) {
        return recordService.initializeItem(initializeItemDto);
    }
}
