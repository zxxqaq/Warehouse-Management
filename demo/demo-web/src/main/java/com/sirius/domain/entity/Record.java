package com.sirius.domain.entity;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sirius.enums.RecordType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;

@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
public class Record {
    @TableId
    private Long recordId;
    private Long userId;

    private Long itemId;
    private Long companyId;

    @EnumValue
    private RecordType type;

    private Date date;

    private Long amount;

    private Double totalWeight;

    private String direction;

    private Integer delFlag;

    private Double unitPrice;
    private Boolean isCheck;
}
