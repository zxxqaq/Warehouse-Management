package com.sirius.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateInvoiceDto {
    private Long invoiceId;
    private Long companyId;
    private String invoiceNum;
    private String invoiceCode;
    private Double taxRate;
    private String comment;
    private Double money;
    private Date startDate;
    private Date endDate;
}
