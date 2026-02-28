package com.brandmanagement.service.impl;

import com.brandmanagement.dto.BrandDTO;
import com.brandmanagement.dto.BrandRequestDTO;
import com.brandmanagement.entity.Brand;
import com.brandmanagement.entity.Chain;
import com.brandmanagement.exception.BrandLinkedToZoneException;
import com.brandmanagement.exception.DuplicateBrandException;
import com.brandmanagement.exception.ResourceNotFoundException;
import com.brandmanagement.repository.BrandRepository;
import com.brandmanagement.repository.ChainRepository;
import com.brandmanagement.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final ChainRepository chainRepository;

    /* ─── Get all active brands ─────────────────────────────────────── */
    @Override
    public List<BrandDTO> getAllActiveBrands() {
        return brandRepository.findByIsActiveTrue()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /* ─── Get brands filtered by chain ─────────────────────────────── */
    @Override
    public List<BrandDTO> getBrandsByChain(Long chainId) {
        // Validate chain exists
        chainRepository.findById(chainId)
                .orElseThrow(() -> new ResourceNotFoundException("Chain", chainId));
        return brandRepository.findByChain_ChainIdAndIsActiveTrue(chainId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /* ─── Get brand by ID ───────────────────────────────────────────── */
    @Override
    public BrandDTO getBrandById(Long brandId) {
        Brand brand = brandRepository.findByBrandIdAndIsActiveTrue(brandId)
                .orElseThrow(() -> new ResourceNotFoundException("Brand", brandId));
        return toDTO(brand);
    }

    /* ─── Create brand ──────────────────────────────────────────────── */
    @Override
    @Transactional
    public BrandDTO createBrand(BrandRequestDTO request) {
        Chain chain = chainRepository.findById(request.getChainId())
                .orElseThrow(() -> new ResourceNotFoundException("Chain", request.getChainId()));

        // Duplicate check
        if (brandRepository.existsByBrandNameAndChainIgnoreCase(
                request.getBrandName(), request.getChainId(), null)) {
            throw new DuplicateBrandException(request.getBrandName(), chain.getChainName());
        }

        Brand brand = Brand.builder()
                .brandName(request.getBrandName().trim())
                .chain(chain)
                .isActive(true)
                .build();

        return toDTO(brandRepository.save(brand));
    }

    /* ─── Update brand ──────────────────────────────────────────────── */
    @Override
    @Transactional
    public BrandDTO updateBrand(Long brandId, BrandRequestDTO request) {
        Brand brand = brandRepository.findByBrandIdAndIsActiveTrue(brandId)
                .orElseThrow(() -> new ResourceNotFoundException("Brand", brandId));

        Chain chain = chainRepository.findById(request.getChainId())
                .orElseThrow(() -> new ResourceNotFoundException("Chain", request.getChainId()));

        // Duplicate check — exclude the current brand ID
        if (brandRepository.existsByBrandNameAndChainIgnoreCase(
                request.getBrandName(), request.getChainId(), brandId)) {
            throw new DuplicateBrandException(request.getBrandName(), chain.getChainName());
        }

        brand.setBrandName(request.getBrandName().trim());
        brand.setChain(chain);
        if (request.getIsActive() != null) {
            brand.setIsActive(request.getIsActive());
        }

        return toDTO(brandRepository.save(brand));
    }

    /* ─── Soft delete brand ─────────────────────────────────────────── */
    @Override
    @Transactional
    public void deleteBrand(Long brandId) {
        Brand brand = brandRepository.findByBrandIdAndIsActiveTrue(brandId)
                .orElseThrow(() -> new ResourceNotFoundException("Brand", brandId));

        // Check if brand is linked to any active Zone
        boolean linkedToZone = brand.getZones() != null &&
                brand.getZones().stream().anyMatch(z -> Boolean.TRUE.equals(z.getIsActive()));
        if (linkedToZone) {
            throw new BrandLinkedToZoneException(brand.getBrandName());
        }

        brand.setIsActive(false);
        brandRepository.save(brand);
    }

    /* ─── Mapper ─────────────────────────────────────────────────────── */
    private BrandDTO toDTO(Brand brand) {
        return BrandDTO.builder()
                .brandId(brand.getBrandId())
                .brandName(brand.getBrandName())
                .chainId(brand.getChain().getChainId())
                .chainName(brand.getChain().getChainName())
                .isActive(brand.getIsActive())
                .createdAt(brand.getCreatedAt())
                .updatedAt(brand.getUpdatedAt())
                .build();
    }
}
