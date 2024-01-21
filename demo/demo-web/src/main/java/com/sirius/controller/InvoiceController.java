package com.sirius.controller;

import com.sirius.domain.ResponseResult;
import com.sirius.domain.dto.AddInvoiceDto;
import com.sirius.domain.dto.UpdateInvoiceDto;
import com.sirius.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @PostMapping("/addInvoice")
    public ResponseResult addInvoice(@RequestBody AddInvoiceDto addInvoiceDto) {
        return invoiceService.addInvoice(addInvoiceDto);
    }

    @PutMapping("/updateInvoice")
    public ResponseResult updateInvoice(@RequestBody UpdateInvoiceDto updateInvoiceDto) {
        return invoiceService.updateInvoice(updateInvoiceDto);
    }

    @DeleteMapping("/{invoiceId}")
    public ResponseResult deleteInvoice(@PathVariable("invoiceId") Long invoiceId) {
        return invoiceService.deleteInvoice(invoiceId);
    }

    @GetMapping("/invoiceList")
    public ResponseResult invoiceList() {
        return invoiceService.invoiceList();
    }
}
