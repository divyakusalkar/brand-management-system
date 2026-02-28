package com.brandmanagement.service;

import com.brandmanagement.dto.BrandDTO;
import com.brandmanagement.dto.BrandRequestDTO;

import java.util.List;

public interface BrandService {

    List<BrandDTO> getAllActiveBrands();

    List<BrandDTO> getBrandsByChain(Long chainId);

    BrandDTO getBrandById(Long brandId);

    BrandDTO createBrand(BrandRequestDTO request);

    BrandDTO updateBrand(Long brandId, BrandRequestDTO request);

    void deleteBrand(Long brandId);
}
