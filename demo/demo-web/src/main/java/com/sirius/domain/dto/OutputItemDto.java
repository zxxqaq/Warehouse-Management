package com.sirius.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OutputItemDto {
    //Item
    private Long itemId;

    //Record
    private Date date;

    private Long amount;

    private String direction;
}
