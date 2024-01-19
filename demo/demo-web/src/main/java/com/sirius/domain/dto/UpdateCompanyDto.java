package com.sirius.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCompanyDto {
    private Long companyId;
    private String companyName;
    private String taxNum;
}
