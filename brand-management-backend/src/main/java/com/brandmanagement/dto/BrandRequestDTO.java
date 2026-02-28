package com.brandmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Request DTO for creating/updating a Brand.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrandRequestDTO {

    @NotBlank(message = "Brand name is required")
    @Size(max = 50, message = "Brand name must not exceed 50 characters")
    private String brandName;

    @NotNull(message = "Chain ID (Company) is required")
    private Long chainId;

    private Boolean isActive;
}
