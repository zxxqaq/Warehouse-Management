package com.sirius.domain.dto;

import com.sirius.enums.RecordType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InitializeItemDto {


    //Item
    private String itemName;

    private String standard;

    private String specification;

    private String material;

    private String level;

    private String surface;

    private double unitWeight;

    private String unit;



    //Record

    private Date date;

    private Long amount;

}
