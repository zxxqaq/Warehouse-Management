package com.sirius.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sirius.domain.ResponseResult;
import com.sirius.domain.dto.InitializeItemDto;
import com.sirius.domain.entity.Item;
import com.sirius.domain.entity.Record;
import com.sirius.domain.entity.User;
import com.sirius.domain.vo.HistoryRecordVo;
import com.sirius.enums.AppHttpCodeEnum;
import com.sirius.enums.RecordType;
import com.sirius.mapper.ItemMapper;
import com.sirius.mapper.RecordMapper;
import com.sirius.service.ItemService;
import com.sirius.service.RecordService;
import com.sirius.service.UserService;
import com.sirius.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Override
    public ResponseResult initializeItem(InitializeItemDto initializeItemDto) {

        Item item = BeanCopyUtils.beanCopy(initializeItemDto, Item.class);
        if(itemService.isExist(item)){
            return ResponseResult.errorResult(AppHttpCodeEnum.ITEM_EXIST);
        }

        itemMapper.insert(item);
        Record record = BeanCopyUtils.beanCopy(initializeItemDto, Record.class);
        record.setItemId(item.getItemId());
        //TODO 用户ID设置
        record.setUserId(1L);
        record.setType(RecordType.Initialization);
        this.save(record);

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getHistoryRecordByCompanyId(Long companyId) {
        LambdaQueryWrapper<Record> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Record::getCompanyId, companyId);
        List<Record> list = this.list(queryWrapper);
        List<HistoryRecordVo> voList = this.getHistoryRecordVoList(list);
        return ResponseResult.okResult(voList);
    }

    @Override
    public ResponseResult getHistoryRecordByItemId(Long companyId, Long itemId) {
        LambdaQueryWrapper<Record> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Record::getCompanyId, companyId);
        queryWrapper.eq(Record::getItemId, itemId);
        List<Record> list = this.list(queryWrapper);
        List<HistoryRecordVo> voList = this.getHistoryRecordVoList(list);
        return ResponseResult.okResult(voList);
    }

    private List<HistoryRecordVo> getHistoryRecordVoList(List<Record> list) {
        return list.stream().map(record -> {
            HistoryRecordVo vo = BeanCopyUtils.beanCopy(record, HistoryRecordVo.class);
            Item item = itemService.getById(record.getItemId());
            User user = userService.getById(record.getUserId());
            this.setVoItem(vo, item);
            this.setVoUser(vo, user);
            return vo;
        }).collect(Collectors.toList());
    }

    private void setVoUser(HistoryRecordVo vo, User user) {
        vo.setUserName(user.getUserName());
    }

    private void setVoItem(HistoryRecordVo historyRecordVo, Item item){
        historyRecordVo.setItemName(item.getItemName());
        historyRecordVo.setLevel(item.getLevel());
        historyRecordVo.setMaterial(item.getMaterial());
        historyRecordVo.setSpecification(item.getSpecification());
        historyRecordVo.setStandard(item.getStandard());
        historyRecordVo.setSpecification(item.getSpecification());
        historyRecordVo.setSurface(item.getSurface());
        historyRecordVo.setUnitWeight(item.getUnitWeight());
    }
}

