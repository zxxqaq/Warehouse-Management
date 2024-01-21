package com.sirius.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sirius.domain.ResponseResult;
import com.sirius.domain.dto.AddInvoiceDto;
import com.sirius.domain.dto.UpdateInvoiceDto;
import com.sirius.domain.entity.Invoice;
import com.sirius.domain.entity.Record;
import com.sirius.enums.AppHttpCodeEnum;
import com.sirius.enums.RecordType;
import com.sirius.mapper.InvoiceMapper;
import com.sirius.service.InvoiceService;
import com.sirius.service.RecordService;
import com.sirius.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (Invoice)表服务实现类
 *
 * @author makejava
 * @since 2024-01-21 11:31:24
 */
@Service("invoiceService")
public class InvoiceServiceImpl extends ServiceImpl<InvoiceMapper, Invoice> implements InvoiceService {


    @Autowired
    private RecordService recordService;


    @Override
    public ResponseResult addInvoice(AddInvoiceDto addInvoiceDto) {
        Invoice invoice = BeanCopyUtils.beanCopy(addInvoiceDto, Invoice.class);

        //calculate money
        LambdaQueryWrapper<Record> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        lambdaQueryWrapper.eq(Record::getCompanyId, invoice.getCompanyId());

        lambdaQueryWrapper.eq(Record::getType, RecordType.Input);

        lambdaQueryWrapper.ge(Record::getDate, invoice.getStartDate());

        lambdaQueryWrapper.le(Record::getDate, invoice.getEndDate());

        List<Record> list = recordService.list(lambdaQueryWrapper);

        Double money = (double) 0;

        for (Record record : list) {
            money += record.getAmount() * record.getUnitPrice();
        }

        invoice.setMoney(money);

        //save invoice
        this.save(invoice);

        return ResponseResult.okResult(money);
    }

    @Override
    public ResponseResult updateInvoice(UpdateInvoiceDto updateInvoiceDto) {
        Invoice invoice = BeanCopyUtils.beanCopy(updateInvoiceDto, Invoice.class);
        if(this.updateById(invoice)){
            return ResponseResult.okResult();
        } else {
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public ResponseResult deleteInvoice(Long invoiceId) {
        this.removeById(invoiceId);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult invoiceList() {
        return null;
    }
}

