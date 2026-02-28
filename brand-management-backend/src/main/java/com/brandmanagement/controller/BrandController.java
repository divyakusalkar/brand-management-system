package com.brandmanagement.controller;

import com.brandmanagement.dto.BrandDTO;
import com.brandmanagement.dto.BrandRequestDTO;
import com.brandmanagement.service.BrandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/brands")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    /**
     * GET /api/brands
     * Returns all active brands. Optionally filter by chainId.
     */
    @GetMapping
    public ResponseEntity<List<BrandDTO>> getAllBrands(
            @RequestParam(required = false) Long chainId) {
        if (chainId != null) {
            return ResponseEntity.ok(brandService.getBrandsByChain(chainId));
        }
        return ResponseEntity.ok(brandService.getAllActiveBrands());
    }

    /**
     * GET /api/brands/{id}
     * Returns a single active brand by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BrandDTO> getBrandById(@PathVariable Long id) {
        return ResponseEntity.ok(brandService.getBrandById(id));
    }

    /**
     * POST /api/brands
     * Creates a new brand.
     */
    @PostMapping
    public ResponseEntity<BrandDTO> createBrand(@Valid @RequestBody BrandRequestDTO request) {
        BrandDTO created = brandService.createBrand(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * PUT /api/brands/{id}
     * Updates an existing brand.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BrandDTO> updateBrand(
            @PathVariable Long id,
            @Valid @RequestBody BrandRequestDTO request) {
        return ResponseEntity.ok(brandService.updateBrand(id, request));
    }

    /**
     * DELETE /api/brands/{id}
     * Soft-deletes a brand (sets is_active = false).
     * Blocked if brand is linked to an active Zone.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBrand(@PathVariable Long id) {
        brandService.deleteBrand(id);
        return ResponseEntity.noContent().build();
    }
}
