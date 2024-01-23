package com.sirius.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRecordDto {
    private Long recordId;

    private Date date;
    private Double unitPrice;
    private Double totalWeight;
    private Long amount;
    private String direction;
    private Boolean isCheck;
}
