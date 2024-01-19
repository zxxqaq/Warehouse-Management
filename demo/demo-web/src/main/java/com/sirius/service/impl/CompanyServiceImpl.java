package com.sirius.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sirius.domain.ResponseResult;
import com.sirius.domain.dto.AddCompanyDto;
import com.sirius.domain.dto.UpdateCompanyDto;
import com.sirius.domain.entity.Company;
import com.sirius.domain.vo.CompanyListVo;
import com.sirius.enums.AppHttpCodeEnum;
import com.sirius.mapper.CompanyMapper;
import com.sirius.service.CompanyService;
import com.sirius.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (Company)表服务实现类
 *
 * @author makejava
 * @since 2024-01-16 16:10:37
 */
@Service("companyService")
public class CompanyServiceImpl extends ServiceImpl<CompanyMapper, Company> implements CompanyService {




    @Override
    public ResponseResult companyList() {
        List<Company> list = this.list();
        List<CompanyListVo> companyListVos = BeanCopyUtils.beanCopyList(list, CompanyListVo.class);
        return ResponseResult.okResult(companyListVos);
    }

    @Override
    public ResponseResult deleteCompany(Long companyId) {
        if (this.removeById(companyId)) {
            return ResponseResult.okResult();
        } else {
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public ResponseResult addCompany(AddCompanyDto addCompanyDto) {
        Company company = BeanCopyUtils.beanCopy(addCompanyDto, Company.class);
        if (this.save(company)) {
            return ResponseResult.okResult();
        } else {
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public ResponseResult updateCompany(UpdateCompanyDto updateCompanyDto) {
        Company company = BeanCopyUtils.beanCopy(updateCompanyDto, Company.class);
        if (this.updateById(company)) {
            return ResponseResult.okResult();
        } else {
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
        }
    }
}

