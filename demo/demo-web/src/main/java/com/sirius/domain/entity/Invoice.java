package com.sirius.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Invoice {
    @TableId
    private long invoiceId;
    private long companyId;
    private String invoiceNum;
    private String invoiceCode;
    private long taxRate;
    private String comment;
    private long money;
    private Date startDate;
    private Date endDate;

    private Integer delFlag;
}
