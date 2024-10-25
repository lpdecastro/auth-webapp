package com.lpdecastro.authwebapp.repository;

import com.lpdecastro.authwebapp.entity.Consent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsentRepository extends JpaRepository<Consent, Long> {
}
