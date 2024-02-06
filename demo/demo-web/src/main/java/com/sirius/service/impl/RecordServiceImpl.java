package com.sirius.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sirius.domain.ContainItem;
import com.sirius.domain.ResponseResult;
import com.sirius.domain.dto.InitializeItemDto;
import com.sirius.domain.dto.InputItemDto;
import com.sirius.domain.dto.OutputItemDto;
import com.sirius.domain.dto.UpdateRecordDto;
import com.sirius.domain.entity.Company;
import com.sirius.domain.entity.Item;
import com.sirius.domain.entity.Record;
import com.sirius.domain.entity.User;
import com.sirius.domain.vo.HistoryRecordVo;
import com.sirius.domain.vo.ItemVo;
import com.sirius.enums.AppHttpCodeEnum;
import com.sirius.enums.RecordType;
import com.sirius.mapper.ItemMapper;
import com.sirius.mapper.RecordMapper;
import com.sirius.service.CompanyService;
import com.sirius.service.ItemService;
import com.sirius.service.RecordService;
import com.sirius.service.UserService;
import com.sirius.utils.BeanCopyUtils;
import com.sirius.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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

    @Autowired
    private CompanyService companyService;

    @Override
    public ResponseResult initializeItem(InitializeItemDto initializeItemDto) {

        Item item = BeanCopyUtils.beanCopy(initializeItemDto, Item.class);
        Long itemId = itemService.isExist(item);
        if (itemId == -1) {
            itemMapper.insert(item);
        } else if (containItem(itemId, initializeItemDto.getCompanyId())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.ITEM_EXIST);
        } else {
            item.setItemId(itemId);
        }
        Record record = BeanCopyUtils.beanCopy(initializeItemDto, Record.class);
        record.setItemId(item.getItemId());
        record.setUserId(SecurityUtils.getUserId());
        record.setType(RecordType.Initialization);
        this.save(record);

        return ResponseResult.okResult();
    }

    private boolean containItem(Long itemId, Long companyId) {
        LambdaQueryWrapper<Record> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Record::getItemId, itemId);
        queryWrapper.eq(Record::getCompanyId, companyId);
        queryWrapper.eq(Record::getType, RecordType.Initialization);
        Record record = this.getOne(queryWrapper);
        return record != null;
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
    public ResponseResult getHistoryRecordByItemIdAndCompanyId(Long companyId, Long itemId) {
        LambdaQueryWrapper<Record> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Record::getCompanyId, companyId);
        queryWrapper.eq(Record::getItemId, itemId);
        List<Record> list = this.list(queryWrapper);
        List<HistoryRecordVo> voList = this.getHistoryRecordVoList(list);
        return ResponseResult.okResult(voList);
    }

    @Override
    public ResponseResult getItemList(Long companyId) {
        LambdaQueryWrapper<Record> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Record::getCompanyId, companyId);
        queryWrapper.eq(Record::getType, RecordType.Initialization);
        List<Record> list = this.list(queryWrapper);

        List<ItemVo> voList = this.getItemVoList(list);

        this.setStatistics(voList, companyId);

        return ResponseResult.okResult(voList);
    }

    @Override
    public ResponseResult inputItem(InputItemDto inputItemDto) {
        Record record = BeanCopyUtils.beanCopy(inputItemDto, Record.class);
        record.setType(RecordType.Input);
        record.setUserId(SecurityUtils.getUserId());
        Item item = itemMapper.selectById(inputItemDto.getItemId());
        if (inputItemDto.getAmount() == null){
            record.setAmount((long) (inputItemDto.getTotalWeight() / item.getUnitWeight()));
        } else {
            record.setAmount(inputItemDto.getAmount());
        }
        save(record);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult outputItem(OutputItemDto outputItemDto) {
        Record record = BeanCopyUtils.beanCopy(outputItemDto, Record.class);
        record.setType(RecordType.Output);
        record.setUserId(SecurityUtils.getUserId());
        save(record);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteRecord(Long recordId) {
        this.removeById(recordId);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult updateRecord(UpdateRecordDto updateRecordDto) {
        Record record = BeanCopyUtils.beanCopy(updateRecordDto, Record.class);
        record.setUserId(SecurityUtils.getUserId());
        if (this.updateById(record)) {
            return ResponseResult.okResult();
        } else {
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public ResponseResult getHistoryRecordByItemId(Long itemId) {
        LambdaQueryWrapper<Record> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Record::getItemId, itemId);
        List<Record> list = this.list(queryWrapper);
        List<HistoryRecordVo> voList = this.getHistoryRecordVoList(list);
        return ResponseResult.okResult(voList);
    }

    @Override
    public ResponseResult getTotalItemList() {
        LambdaQueryWrapper<Record> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Record::getType, RecordType.Initialization);
        List<Record> list = this.list(queryWrapper);

        List<ItemVo> voList = this.getItemVoList(list);

        this.setStatistics(voList);

        return ResponseResult.okResult(voList);
    }

    private void setStatistics(List<ItemVo> voList, Long companyId) {
        voList.forEach(itemVo -> {
            itemVo.setTotalCount(0L);
            itemVo.setInCount(0L);
            itemVo.setOutCount(0L);
            LambdaQueryWrapper<Record> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Record::getItemId, itemVo.getItemId());
            queryWrapper.eq(Record::getCompanyId, companyId);
            queryWrapper.eq(Record::getType, RecordType.Input);
            List<Record> list = this.list(queryWrapper);
            list.forEach(record -> {
                itemVo.setInCount(itemVo.getInCount() + record.getAmount());
            });

            queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Record::getItemId, itemVo.getItemId());
            queryWrapper.eq(Record::getCompanyId, companyId);
            queryWrapper.eq(Record::getType, RecordType.Output);
            list = this.list(queryWrapper);
            list.forEach(record -> {
                itemVo.setOutCount(itemVo.getOutCount() + record.getAmount());
            });

            itemVo.setTotalCount(itemVo.getInCount() - itemVo.getOutCount() + itemVo.getInitialCount());
        });
    }


    private void setStatistics(List<ItemVo> voList) {
        voList.forEach(itemVo -> {
            itemVo.setTotalCount(0L);
            itemVo.setInCount(0L);
            itemVo.setOutCount(0L);
            LambdaQueryWrapper<Record> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Record::getItemId, itemVo.getItemId());
            queryWrapper.eq(Record::getType, RecordType.Input);
            List<Record> list = this.list(queryWrapper);
            list.forEach(record -> {
                itemVo.setInCount(itemVo.getInCount() + record.getAmount());
            });

            queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Record::getItemId, itemVo.getItemId());
            queryWrapper.eq(Record::getType, RecordType.Output);
            list = this.list(queryWrapper);
            list.forEach(record -> {
                itemVo.setOutCount(itemVo.getOutCount() + record.getAmount());
            });

            itemVo.setTotalCount(itemVo.getInCount() - itemVo.getOutCount() + itemVo.getInitialCount());
        });
    }


    private List<ItemVo> getItemVoList(List<Record> list) {

        HashMap<Long, Record> map = new HashMap<>();

        for (Record record : list) {
            if (map.containsKey(record.getItemId())) {
                Record r = map.get(record.getItemId());
                r.setAmount(r.getAmount() + record.getAmount());
            } else {
                map.put(record.getItemId(), record);
            }
        }


        return map.values().stream().map(record -> {
            ItemVo itemVo = new ItemVo();
            Item item = itemService.getById(record.getItemId());
            itemVo.setItemId(item.getItemId());
            this.setVoItem(itemVo, item);
            itemVo.setInitialCount(record.getAmount());
            return itemVo;
        }).collect(Collectors.toList());
    }


    private List<HistoryRecordVo> getHistoryRecordVoList(List<Record> list) {
        return list.stream().map(record -> {
            HistoryRecordVo vo = BeanCopyUtils.beanCopy(record, HistoryRecordVo.class);
            Item item = itemService.getById(record.getItemId());
            User user = userService.getById(record.getUserId());
            Company company = companyService.getById(record.getCompanyId());
            this.setVoCompany(vo, company);
            this.setVoItem(vo, item);
            this.setVoUser(vo, user);
            return vo;
        }).collect(Collectors.toList());
    }

    private void setVoCompany(HistoryRecordVo vo, Company company) {
        vo.setCompanyName(company.getCompanyName());
    }

    private void setVoUser(HistoryRecordVo vo, User user) {
        vo.setUserName(user.getUserName());
    }

    private void setVoItem(ContainItem itemVo, Item item) {
        itemVo.setItemName(item.getItemName());
        itemVo.setLevel(item.getLevel());
        itemVo.setMaterial(item.getMaterial());
        itemVo.setSpecification(item.getSpecification());
        itemVo.setStandard(item.getStandard());
        itemVo.setSpecification(item.getSpecification());
        itemVo.setSurface(item.getSurface());
        itemVo.setUnitWeight(item.getUnitWeight());
        itemVo.setUnit(item.getUnit());
    }
}

