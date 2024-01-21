package com.sirius.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddInvoiceDto {
    private Long companyId;

    private String invoiceNum;
    private String invoiceCode;

    private Double taxRate;
    private String comment;
    private Date startDate;
    private Date endDate;
}
