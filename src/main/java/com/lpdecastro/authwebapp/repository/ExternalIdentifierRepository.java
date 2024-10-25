package com.lpdecastro.authwebapp.repository;

import com.lpdecastro.authwebapp.entity.ExternalIdentifier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExternalIdentifierRepository extends JpaRepository<ExternalIdentifier, Long> {
}
