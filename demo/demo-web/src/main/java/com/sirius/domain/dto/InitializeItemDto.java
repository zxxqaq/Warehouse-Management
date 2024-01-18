package com.sirius.domain.dto;

import com.sirius.enums.RecordType;

import java.util.Date;

public class InitializeItemDto {


    //Item
    private String itemName;

    private String standard;

    private String specification;

    private String material;

    private String level;

    private String surface;

    private double unitWeight;



    //Record

    private Date date;

    private long companyId;

    private long amount;

    private String unit;

    private Integer delFlag;

}
