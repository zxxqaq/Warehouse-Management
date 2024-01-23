package com.sirius.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sirius.domain.ResponseResult;
import com.sirius.domain.dto.InitializeItemDto;
import com.sirius.domain.dto.InputItemDto;
import com.sirius.domain.dto.OutputItemDto;
import com.sirius.domain.dto.UpdateRecordDto;
import com.sirius.domain.entity.Record;


/**
 * (Record)表服务接口
 *
 * @author makejava
 * @since 2024-01-18 10:47:07
 */
public interface RecordService extends IService<Record> {

    ResponseResult initializeItem(InitializeItemDto initializeItemDto);

    ResponseResult getHistoryRecordByCompanyId(Long companyId);

    ResponseResult getHistoryRecordByItemId(Long companyId, Long itemId);

    ResponseResult getItemList(Long companyId);

    ResponseResult inputItem(InputItemDto inputItemDto);

    ResponseResult outputItem(OutputItemDto outputItemDto);

    ResponseResult deleteRecord(Long recordId);

    ResponseResult updateRecord(UpdateRecordDto updateRecordDto);
}
