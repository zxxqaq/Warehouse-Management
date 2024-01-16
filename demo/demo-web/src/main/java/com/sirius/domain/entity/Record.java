package com.sirius.domain.entity;

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
    private long recordId;
    private long userId;

    private long itemId;
    private long companyId;

    private RecordType type;

    private Date date;

    private long amount;

    private String unit;

    private double totalWeight;

    private String direction;

    private Integer delFlag;
}
