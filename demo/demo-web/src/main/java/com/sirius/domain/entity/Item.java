package com.sirius.domain.entity;

import lombok.Data;

@Data
public class Item {
    private String itemName;

    private String standard;

    private String specification;

    private String material;

    private String level;

    private String surface;

    private double unitWeight;

    private Integer delFlag;
}
