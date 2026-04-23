package com.escapeplanner.repository;

import com.escapeplanner.domain.entity.Documento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentoRepository extends JpaRepository<Documento, Long> {
}
