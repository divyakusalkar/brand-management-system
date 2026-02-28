package com.brandmanagement.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Response DTO for Brand.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrandDTO {

    private Long brandId;
    private String brandName;
    private Long chainId;
    private String chainName;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
