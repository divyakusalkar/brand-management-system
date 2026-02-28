package com.brandmanagement.dto;

import lombok.*;

/**
 * Response DTO for Chain (Company).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChainDTO {

    private Long chainId;
    private String chainName;
    private Boolean isActive;
}
