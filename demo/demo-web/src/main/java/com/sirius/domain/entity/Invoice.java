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
    private Long invoiceId;
    private Long companyId;
    private String invoiceNum;
    private String invoiceCode;
    private Long taxRate;
    private String comment;
    private Long money;
    private Date startDate;
    private Date endDate;

    private Integer delFlag;
}
