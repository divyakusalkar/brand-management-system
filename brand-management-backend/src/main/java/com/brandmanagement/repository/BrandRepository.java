package com.brandmanagement.repository;

import com.brandmanagement.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {

    // All active brands
    List<Brand> findByIsActiveTrue();

    // Active brands filtered by chain
    List<Brand> findByChain_ChainIdAndIsActiveTrue(Long chainId);

    // Check duplicate: same brand name + same chain (case-insensitive), excluding a specific ID
    @Query("""
        SELECT COUNT(b) > 0 FROM Brand b
        WHERE LOWER(b.brandName) = LOWER(:brandName)
          AND b.chain.chainId = :chainId
          AND b.isActive = true
          AND (:excludeId IS NULL OR b.brandId <> :excludeId)
        """)
    boolean existsByBrandNameAndChainIgnoreCase(
        @Param("brandName") String brandName,
        @Param("chainId") Long chainId,
        @Param("excludeId") Long excludeId
    );

    // Active brand by ID
    Optional<Brand> findByBrandIdAndIsActiveTrue(Long brandId);
}
