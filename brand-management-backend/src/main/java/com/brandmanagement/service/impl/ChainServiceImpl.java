package com.brandmanagement.service.impl;

import com.brandmanagement.dto.ChainDTO;
import com.brandmanagement.entity.Chain;
import com.brandmanagement.repository.ChainRepository;
import com.brandmanagement.service.ChainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChainServiceImpl implements ChainService {

    private final ChainRepository chainRepository;

    @Override
    public List<ChainDTO> getAllActiveChains() {
        return chainRepository.findByIsActiveTrue()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private ChainDTO toDTO(Chain chain) {
        return ChainDTO.builder()
                .chainId(chain.getChainId())
                .chainName(chain.getChainName())
                .isActive(chain.getIsActive())
                .build();
    }
}
