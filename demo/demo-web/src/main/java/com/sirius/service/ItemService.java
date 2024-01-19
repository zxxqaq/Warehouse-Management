package com.sirius.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sirius.domain.entity.Item;


/**
 * (Item)表服务接口
 *
 * @author makejava
 * @since 2024-01-18 10:42:08
 */
public interface ItemService extends IService<Item> {

    boolean isExist(Item item);
}
