package com.sirius.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyListVo {
    private long companyId;
    private String companyName;
    private String taxNum;
}
