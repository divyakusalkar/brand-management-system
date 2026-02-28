package com.brandmanagement.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Company / Chain entity.
 * One Chain can have many Brands.
 */
@Entity
@Table(name = "chain")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Chain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chain_id")
    private Long chainId;

    @Column(name = "chain_name", nullable = false, unique = true, length = 100)
    private String chainName;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder.Default
    @OneToMany(mappedBy = "chain", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Brand> brands = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
