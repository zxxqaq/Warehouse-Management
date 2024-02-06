package com.sirius.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InputItemDto {

    //Item
    private Long itemId;

    //Record

    private Date date;

    private Long companyId;

    private Double totalWeight;

    private Long amount;

    private Double unitPrice;
}
