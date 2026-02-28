package com.brandmanagement.controller;

import com.brandmanagement.dto.ChainDTO;
import com.brandmanagement.service.ChainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/chains")
@RequiredArgsConstructor
public class ChainController {

    private final ChainService chainService;

    /**
     * GET /api/chains
     * Returns all active chains (companies) for dropdown population.
     */
    @GetMapping
    public ResponseEntity<List<ChainDTO>> getAllChains() {
        return ResponseEntity.ok(chainService.getAllActiveChains());
    }
}
