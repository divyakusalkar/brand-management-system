package com.brandmanagement.repository;

import com.brandmanagement.entity.Chain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChainRepository extends JpaRepository<Chain, Long> {

    List<Chain> findByIsActiveTrue();
}
