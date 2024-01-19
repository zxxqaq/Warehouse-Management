package com.sirius.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sirius.domain.ResponseResult;
import com.sirius.domain.dto.AddCompanyDto;
import com.sirius.domain.dto.UpdateCompanyDto;
import com.sirius.domain.entity.Company;


/**
 * (Company)表服务接口
 *
 * @author makejava
 * @since 2024-01-16 16:10:37
 */
public interface CompanyService extends IService<Company> {

    ResponseResult companyList();

    ResponseResult deleteCompany(Long companyId);

    ResponseResult addCompany(AddCompanyDto addCompanyDto);

    ResponseResult updateCompany(UpdateCompanyDto addCompanyDto);
}
