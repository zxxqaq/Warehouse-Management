package com.sirius.domain.vo;

import com.sirius.domain.ContainItem;
import com.sirius.enums.RecordType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemVo implements ContainItem {
    private Long itemId;
    private String itemName;

    private String standard;

    private String specification;

    private String material;

    private String level;

    private String surface;

    private Double unitWeight;

    private RecordType type;

    private Date date;

    private Long amount;

    private String unit;
    private String userName;

    private Integer inCount;
    private Integer outCount;

    private Integer initialCount;
    private Integer totalCount;

}
