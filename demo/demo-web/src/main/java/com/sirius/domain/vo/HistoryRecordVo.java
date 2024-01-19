package com.sirius.domain.vo;

import com.sirius.enums.RecordType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryRecordVo {
    private Long recordId;

    private String itemName;

    private String standard;

    private String specification;

    private String material;

    private String level;

    private String surface;

    private Double unitWeight;

    private RecordType type;

    private Date date;

    private Long amount;

    private String unit;

    private Double totalWeight;

    private String direction;

    private String userName;

    private double unitPrice;

}
