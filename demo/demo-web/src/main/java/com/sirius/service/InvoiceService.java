package com.sirius.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sirius.domain.ResponseResult;
import com.sirius.domain.dto.AddInvoiceDto;
import com.sirius.domain.dto.UpdateInvoiceDto;
import com.sirius.domain.entity.Invoice;


/**
 * (Invoice)表服务接口
 *
 * @author makejava
 * @since 2024-01-21 11:31:24
 */
public interface InvoiceService extends IService<Invoice> {

    ResponseResult addInvoice(AddInvoiceDto addInvoiceDto);

    ResponseResult updateInvoice(UpdateInvoiceDto updateInvoiceDto);

    ResponseResult deleteInvoice(Long invoiceId);

    ResponseResult invoiceList();
}
