package com.sirius.service.impl;

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

}

