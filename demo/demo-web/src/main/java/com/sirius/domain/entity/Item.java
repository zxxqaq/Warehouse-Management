package com.sirius.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    @TableId(type = IdType.AUTO)
    private Long itemId;

    private String itemName;

    private String standard;

    private String specification;

    private String material;

    private String level;

    private String surface;

    private Double unitWeight;

    private Integer delFlag;
}
