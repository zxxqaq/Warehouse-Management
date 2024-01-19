package com.sirius.controller;

import com.sirius.domain.ResponseResult;
import com.sirius.domain.dto.AddCompanyDto;
import com.sirius.domain.dto.UpdateCompanyDto;
import com.sirius.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/overview")
public class OverviewController {

    @Autowired
    private CompanyService companyService;

    @GetMapping("/companyList")
    public ResponseResult companyList() {
        return companyService.companyList();
    }

    @DeleteMapping("/{companyId}")
    public ResponseResult deleteCompany(@PathVariable("companyId") Long companyId) {
        return companyService.deleteCompany(companyId);
    }

    @PostMapping("/addCompany")
    public ResponseResult addCompany(@RequestBody AddCompanyDto addCompanyDto) {
        return companyService.addCompany(addCompanyDto);
    }

    @PutMapping("/updateCompany")
    public ResponseResult updateCompany(@RequestBody UpdateCompanyDto updateCompanyDto) {
        return companyService.updateCompany(updateCompanyDto);
    }

}
