package com.sirius.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sirius.domain.ResponseResult;
import com.sirius.domain.dto.InitializeItemDto;
import com.sirius.domain.entity.Item;
import com.sirius.domain.entity.Record;
import com.sirius.enums.RecordType;
import com.sirius.mapper.ItemMapper;
import com.sirius.mapper.RecordMapper;
import com.sirius.service.ItemService;
import com.sirius.service.RecordService;
import com.sirius.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * (Record)表服务实现类
 *
 * @author makejava
 * @since 2024-01-18 10:47:07
 */
@Service("recordService")
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record> implements RecordService {

    @Autowired
    private ItemMapper itemMapper;

    @Override
    public ResponseResult initializeItem(InitializeItemDto initializeItemDto) {

        Item item = BeanCopyUtils.beanCopy(initializeItemDto, Item.class);
        //TODO 查询是否存在该零件
        itemMapper.insert(item);
        Record record = BeanCopyUtils.beanCopy(initializeItemDto, Record.class);
        record.setItemId(item.getItemId());
        //TODO 用户ID设置
        record.setUserId(1);
        record.setType(RecordType.Initialization);
        this.save(record);

        return ResponseResult.okResult();
    }
}

