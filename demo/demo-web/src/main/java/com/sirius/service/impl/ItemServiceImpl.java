package com.sirius.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sirius.domain.entity.Item;
import com.sirius.mapper.ItemMapper;
import com.sirius.service.ItemService;
import org.springframework.stereotype.Service;

/**
 * (Item)表服务实现类
 *
 * @author makejava
 * @since 2024-01-18 10:42:08
 */
@Service("itemService")
public class ItemServiceImpl extends ServiceImpl<ItemMapper, Item> implements ItemService {

    @Override
    public Long isExist(Item item) {

        LambdaQueryWrapper<Item> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Item::getItemName, item.getItemName());
        queryWrapper.eq(Item::getStandard, item.getStandard());
        queryWrapper.eq(Item::getMaterial, item.getMaterial());
        queryWrapper.eq(Item::getLevel, item.getLevel());
        queryWrapper.eq(Item::getSurface, item.getSurface());
        queryWrapper.eq(Item::getSpecification, item.getSpecification());

        Item item1 = this.getOne(queryWrapper);

        if(item1 == null){
            return -1L;
        }

        return item1.getItemId();
    }
}

