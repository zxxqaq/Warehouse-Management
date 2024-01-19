package com.sirius.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Company {
    @TableId
    private Long companyId;
    private String companyName;
    private String taxNum;

    private Integer delFlag;
}
