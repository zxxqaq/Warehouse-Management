package com.sirius.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    private long itemId;

    private String itemName;

    private String standard;

    private String specification;

    private String material;

    private String level;

    private String surface;

    private double unitWeight;

    private Integer delFlag;
}
