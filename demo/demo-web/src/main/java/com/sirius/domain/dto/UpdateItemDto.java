package com.sirius.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateItemDto {
    private Long itemId; //一定要传

    private String itemName; //改啥传啥

    private String standard;

    private String specification;

    private String material;

    private String level;

    private String surface;

    private Double unitWeight;

    private String unit;
}

